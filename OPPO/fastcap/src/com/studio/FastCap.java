package com.studio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FastCap {
    private static int maxWidth = 420;
    private static int quality = 40;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--server")) {
            int port = args.length > 1 ? Integer.parseInt(args[1]) : 19876;
            if (args.length > 2) maxWidth = Integer.parseInt(args[2]);
            if (args.length > 3) quality = Integer.parseInt(args[3]);
            runServer(port);
            return;
        }

        // Thumbnail generator mode: FastCap thumb <in_path> <out_path> [w] [q]
        if (args.length > 0 && args[0].equals("thumb")) {
            String inPath = args.length > 1 ? args[1] : "";
            String outPath = args.length > 2 ? args[2] : "/data/local/tmp/thumb.jpg";
            int w = args.length > 3 ? Integer.parseInt(args[3]) : 280;
            int q = args.length > 4 ? Integer.parseInt(args[4]) : 50;
            try {
                byte[] thumbBytes = captureAndCompress(inPath, w, q);
                if (thumbBytes != null) {
                    FileOutputStream fos = new FileOutputStream(outPath);
                    fos.write(thumbBytes);
                    fos.flush();
                    fos.close();
                    System.out.println("THUMB_OK:" + thumbBytes.length);
                } else {
                    System.err.println("ERR: Thumb failed");
                    System.exit(1);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
            return;
        }

        // One-shot CLI screen capture mode: FastCap <in_png> <out_path> [width] [quality]
        long t0 = System.currentTimeMillis();
        String inPath = args.length > 0 ? args[0] : "/data/local/tmp/_sc.png";
        String outPath = args.length > 1 ? args[1] : "/data/local/tmp/screen.jpg";
        int width = args.length > 2 ? Integer.parseInt(args[2]) : 420;
        int q = args.length > 3 ? Integer.parseInt(args[3]) : 40;

        try {
            byte[] jpegBytes = captureAndCompress(inPath, width, q);
            if (jpegBytes != null) {
                FileOutputStream fos = new FileOutputStream(outPath);
                fos.write(jpegBytes);
                fos.flush();
                fos.close();
                long t1 = System.currentTimeMillis();
                System.out.println("FASTCAP_OK:" + jpegBytes.length + " in " + (t1 - t0) + "ms");
            } else {
                System.err.println("ERR: Compression failed");
                System.exit(1);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static byte[] captureAndCompress(String inPath, int targetWidth, int q) {
        try {
            File inFile = new File(inPath);
            if (!inFile.exists() || inFile.length() == 0) {
                // If input file is missing and looks like temporary capture, try screencap
                if (inPath.contains("_sc") || inPath.contains("_fastcap")) {
                    Process p = Runtime.getRuntime().exec(new String[]{"screencap", "-p", inPath});
                    p.waitFor();
                } else {
                    return null;
                }
            }

            Bitmap bmp = null;
            String lower = inPath.toLowerCase();

            // 1. Video files: extract frame thumbnail via MediaMetadataRetriever
            if (lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".mov") || 
                lower.endsWith(".3gp") || lower.endsWith(".webm") || lower.endsWith(".avi")) {
                try {
                    android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
                    mmr.setDataSource(inPath);
                    bmp = mmr.getFrameAtTime(1000000, android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    if (bmp == null) {
                        bmp = mmr.getFrameAtTime();
                    }
                    mmr.release();
                } catch (Throwable t) {
                    // fallback to standard decoding
                }
            }

            // 2. Image files or video fallback: sub-sampled BitmapFactory decoding
            if (bmp == null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(inPath, opts);

                if (opts.outWidth <= 0 || opts.outHeight <= 0) {
                    return null;
                }

                int sampleSize = 1;
                while (opts.outWidth / (sampleSize * 2) >= targetWidth) {
                    sampleSize *= 2;
                }
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = sampleSize;
                opts.inPreferredConfig = targetWidth >= 200 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;

                bmp = BitmapFactory.decodeFile(inPath, opts);
            }

            if (bmp == null) {
                return null;
            }

            Bitmap target = bmp;
            if (bmp.getWidth() > targetWidth) {
                int targetHeight = (int) ((float) bmp.getHeight() * ((float) targetWidth / bmp.getWidth()));
                target = Bitmap.createScaledBitmap(bmp, targetWidth, targetHeight, true);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            target.compress(Bitmap.CompressFormat.JPEG, Math.max(20, Math.min(100, q)), baos);
            if (target != bmp) {
                target.recycle();
            }
            bmp.recycle();

            return baos.toByteArray();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void runServer(int port) {
        System.out.println("FASTCAP_SERVER_STARTING:" + port);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("FASTCAP_SERVER_READY:" + port);
            String tmpPng = "/data/local/tmp/_fastcap_" + port + ".png";

            while (true) {
                try {
                    Socket sock = server.accept();
                    InputStream is = sock.getInputStream();
                    OutputStream os = sock.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line = reader.readLine();
                    if (line == null || line.equals("QUIT")) {
                        sock.close();
                        if ("QUIT".equals(line)) break;
                        continue;
                    }

                    if ("PING".equals(line)) {
                        dos.writeInt(4);
                        dos.write("PONG".getBytes());
                        dos.flush();
                        sock.close();
                        continue;
                    }

                    // Handle thumbnail generation: "THUMB <remote_path> [w] [q]"
                    if (line.startsWith("THUMB ")) {
                        String[] tParts = line.split("\\s+", 4);
                        String imgPath = tParts.length > 1 ? tParts[1] : "";
                        int tw = 180;
                        int tq = 25;
                        if (tParts.length > 2) {
                            try { tw = Integer.parseInt(tParts[2]); } catch (Exception ignored) {}
                        }
                        if (tParts.length > 3) {
                            try { tq = Integer.parseInt(tParts[3]); } catch (Exception ignored) {}
                        }

                        byte[] thumbBytes = captureAndCompress(imgPath, tw, tq);
                        if (thumbBytes != null && thumbBytes.length > 0) {
                            dos.writeInt(thumbBytes.length);
                            dos.write(thumbBytes);
                            dos.flush();
                        } else {
                            dos.writeInt(0);
                            dos.flush();
                        }
                        sock.close();
                        continue;
                    }

                    // Screen capture request: "CAP [width] [quality]"
                    int w = maxWidth;
                    int q = quality;
                    String[] parts = line.split("\\s+");
                    if (parts.length > 1) {
                        try { w = Integer.parseInt(parts[1]); } catch (Exception ignored) {}
                    }
                    if (parts.length > 2) {
                        try { q = Integer.parseInt(parts[2]); } catch (Exception ignored) {}
                    }

                    // Execute screencap
                    Process p = Runtime.getRuntime().exec(new String[]{"screencap", "-p", tmpPng});
                    p.waitFor();

                    byte[] jpeg = captureAndCompress(tmpPng, w, q);
                    new File(tmpPng).delete();

                    if (jpeg != null && jpeg.length > 0) {
                        dos.writeInt(jpeg.length);
                        dos.write(jpeg);
                        dos.flush();
                    } else {
                        dos.writeInt(0);
                        dos.flush();
                    }

                    sock.close();
                } catch (Throwable err) {
                    err.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("FASTCAP_SERVER_STOPPED");
    }
}
