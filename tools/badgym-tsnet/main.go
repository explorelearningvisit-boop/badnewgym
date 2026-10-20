package main

import (
	"context"
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"strings"
	"sync"
	"syscall"
	"time"

	"tailscale.com/tsnet"
)

type StatusInfo struct {
	Connected    bool     `json:"connected"`
	IP           string   `json:"ip"`
	IPs          []string `json:"ips"`
	Hostname     string   `json:"hostname"`
	DNSName      string   `json:"dnsName"`
	BackendState string   `json:"backendState"`
	AuthURL      string   `json:"authUrl"`
	ListenPort   int      `json:"listenPort"`
	TargetAddr   string   `json:"targetAddr"`
	Error        string   `json:"error,omitempty"`
}

var (
	statusMu   sync.RWMutex
	currStatus StatusInfo
)

func setStatus(updater func(*StatusInfo)) {
	statusMu.Lock()
	defer statusMu.Unlock()
	updater(&currStatus)
}

func getStatus() StatusInfo {
	statusMu.RLock()
	defer statusMu.RUnlock()
	return currStatus
}

func main() {
	dir := flag.String("dir", "./tsnet_state", "Tailscale state directory")
	hostname := flag.String("hostname", "badgym-device", "Tailscale hostname")
	authKey := flag.String("authkey", "", "Tailscale auth key")
	listenPort := flag.Int("listen", 8787, "Port to listen on Tailscale network")
	targetAddr := flag.String("target", "127.0.0.1:8787", "Target local host:port to forward to")
	controlPort := flag.Int("control-port", 8788, "Local loopback control HTTP port")
	triggerURL := flag.String("trigger-url", "http://127.0.0.1:8787/health", "Local URL to trigger on connection")
	flag.Parse()

	log.Printf("[tsnet] Starting BAD GYM Embedded Tailscale daemon...")
	log.Printf("[tsnet] State dir: %s, Hostname: %s, Listen: :%d -> %s", *dir, *hostname, *listenPort, *targetAddr)

	if err := os.MkdirAll(*dir, 0700); err != nil {
		log.Printf("[tsnet] Warning creating state dir %s: %v", *dir, err)
	}

	setStatus(func(s *StatusInfo) {
		s.ListenPort = *listenPort
		s.TargetAddr = *targetAddr
		s.Hostname = *hostname
		s.BackendState = "Starting"
		s.IPs = []string{}
	})

	// Start local loopback control server
	go startControlServer(*controlPort, *triggerURL)

	userLogger := func(format string, args ...any) {
		msg := fmt.Sprintf(format, args...)
		log.Printf("[tsnet-log] %s", msg)
		if strings.Contains(msg, "https://login.tailscale.com/") {
			parts := strings.Fields(msg)
			for _, p := range parts {
				if strings.HasPrefix(p, "https://login.tailscale.com/") {
					setStatus(func(st *StatusInfo) {
						st.AuthURL = p
						st.BackendState = "NeedsLogin"
					})
				}
			}
		}
	}

	s := &tsnet.Server{
		Dir:      *dir,
		Hostname: *hostname,
		AuthKey:  *authKey,
		Logf:     userLogger,
		UserLogf: userLogger,
	}
	defer s.Close()

	// Monitor tailscale status in background
	go monitorStatus(s)

	// Listen on Tailnet
	ln, err := s.Listen("tcp", fmt.Sprintf(":%d", *listenPort))
	if err != nil {
		setStatus(func(st *StatusInfo) {
			st.Error = fmt.Sprintf("Listen error: %v", err)
			st.BackendState = "Error"
		})
		log.Fatalf("[tsnet] Failed to listen on :%d: %v", *listenPort, err)
	}
	defer ln.Close()

	log.Printf("[tsnet] Successfully listening on Tailscale port :%d. Ready to forward to %s", *listenPort, *targetAddr)

	// Trigger local app on startup to ensure BAD GYM is active
	go triggerLocalApp(*triggerURL)

	// Accept connections on HTTP / MCP port
	go func() {
		for {
			conn, err := ln.Accept()
			if err != nil {
				log.Printf("[tsnet] Accept error: %v", err)
				return
			}
			go handleConn(conn, *targetAddr, *triggerURL)
		}
	}()

	// Also listen on Tailnet port 5555 for wireless ADB forwarding
	go func() {
		adbLn, err := s.Listen("tcp", ":5555")
		if err != nil {
			log.Printf("[tsnet] Note: Could not listen on ADB :5555: %v", err)
			return
		}
		defer adbLn.Close()
		log.Printf("[tsnet] Successfully listening on Tailscale port :5555 (ADB bridge to 127.0.0.1:5555)")
		for {
			adbConn, err := adbLn.Accept()
			if err != nil {
				return
			}
			go handleConn(adbConn, "127.0.0.1:5555", "")
		}
	}()

	// Wait for exit signal
	sigCh := make(chan os.Signal, 1)
	signal.Notify(sigCh, syscall.SIGINT, syscall.SIGTERM)
	<-sigCh
	log.Printf("[tsnet] Shutting down embedded Tailscale daemon...")
}

func handleConn(conn net.Conn, targetAddr string, triggerURL string) {
	defer conn.Close()
	remoteAddr := conn.RemoteAddr().String()
	log.Printf("[tsnet] Incoming connection from Tailscale peer %s -> forwarding to %s", remoteAddr, targetAddr)

	// Trigger app wakeup
	go triggerLocalApp(triggerURL)

	targetConn, err := net.DialTimeout("tcp", targetAddr, 5*time.Second)
	if err != nil {
		log.Printf("[tsnet] Failed to connect to local target %s: %v", targetAddr, err)
		return
	}
	defer targetConn.Close()

	errCh := make(chan error, 2)
	go func() {
		_, err := io.Copy(targetConn, conn)
		errCh <- err
	}()
	go func() {
		_, err := io.Copy(conn, targetConn)
		errCh <- err
	}()
	<-errCh
	log.Printf("[tsnet] Connection from %s closed", remoteAddr)
}

func startControlServer(port int, triggerURL string) {
	mux := http.NewServeMux()
	mux.HandleFunc("/status", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.Header().Set("Access-Control-Allow-Origin", "*")
		json.NewEncoder(w).Encode(getStatus())
	})
	mux.HandleFunc("/trigger", func(w http.ResponseWriter, r *http.Request) {
		triggerLocalApp(triggerURL)
		w.Header().Set("Content-Type", "application/json")
		w.Header().Set("Access-Control-Allow-Origin", "*")
		fmt.Fprintf(w, `{"status":"triggered"}`)
	})
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.Header().Set("Access-Control-Allow-Origin", "*")
		fmt.Fprintf(w, `{"ok":true,"daemon":"badgym-tsnet"}`)
	})

	server := &http.Server{
		Addr:    fmt.Sprintf("127.0.0.1:%d", port),
		Handler: mux,
	}
	log.Printf("[tsnet] Control server listening on 127.0.0.1:%d", port)
	if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		log.Printf("[tsnet] Control server error: %v", err)
	}
}

func triggerLocalApp(triggerURL string) {
	if triggerURL == "" {
		return
	}
	client := &http.Client{Timeout: 2 * time.Second}
	resp, err := client.Get(triggerURL)
	if err == nil {
		resp.Body.Close()
		log.Printf("[tsnet] Triggered local app at %s (HTTP %d)", triggerURL, resp.StatusCode)
	} else {
		log.Printf("[tsnet] Trigger ping to %s: %v", triggerURL, err)
	}
}

func monitorStatus(s *tsnet.Server) {
	ctx := context.Background()
	for {
		time.Sleep(3 * time.Second)
		lc, err := s.LocalClient()
		if err != nil {
			continue
		}
		st, err := lc.Status(ctx)
		if err != nil {
			continue
		}
		setStatus(func(info *StatusInfo) {
			info.BackendState = st.BackendState
			if st.Self != nil {
				info.Hostname = st.Self.HostName
				info.DNSName = st.Self.DNSName
				var ips []string
				for _, ip := range st.Self.TailscaleIPs {
					ips = append(ips, ip.String())
				}
				info.IPs = ips
				if len(ips) > 0 {
					info.IP = ips[0]
					info.Connected = (st.BackendState == "Running")
				}
			}
			if st.AuthURL != "" {
				info.AuthURL = st.AuthURL
			}
		})
	}
}
