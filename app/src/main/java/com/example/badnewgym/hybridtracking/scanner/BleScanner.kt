package com.example.badnewgym.hybridtracking.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.example.badnewgym.hybridtracking.model.BleBeaconInfo
import java.nio.ByteBuffer
import java.util.UUID

class BleScanner(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        manager?.adapter
    }

    @SuppressLint("MissingPermission")
    fun scanBleBeaconsSync(durationMs: Long = 2000L): List<BleBeaconInfo> {
        val scanner = bluetoothAdapter?.bluetoothLeScanner
        if (scanner == null || bluetoothAdapter?.isEnabled != true) {
            return getSimulatedBleBeacons()
        }

        val discoveredBeacons = mutableMapOf<String, BleBeaconInfo>()
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.let { parseScanResult(it)?.let { beacon ->
                    discoveredBeacons[beacon.uuid + "_" + beacon.major + "_" + beacon.minor] = beacon
                } }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                results?.forEach { result ->
                    parseScanResult(result)?.let { beacon ->
                        discoveredBeacons[beacon.uuid + "_" + beacon.major + "_" + beacon.minor] = beacon
                    }
                }
            }
        }

        try {
            scanner.startScan(callback)
            Thread.sleep(durationMs)
            scanner.stopScan(callback)
        } catch (e: Exception) {
            return getSimulatedBleBeacons()
        }

        return discoveredBeacons.values.toList()
    }

    private fun parseScanResult(scanResult: ScanResult): BleBeaconInfo? {
        val scanRecord = scanResult.scanRecord ?: return null
        val bytes = scanRecord.bytes ?: return null

        // Look for iBeacon prefix: manufacturer data 0x004C (Apple), payload starts with 0x02, 0x15
        val manufacturerData = scanRecord.getManufacturerSpecificData(0x004C) ?: return parseRawBytes(bytes, scanResult.rssi)
        if (manufacturerData.size >= 23 && manufacturerData[0] == 0x02.toByte() && manufacturerData[1] == 0x15.toByte()) {
            val uuidBytes = manufacturerData.copyOfRange(2, 18)
            val uuid = bytesToUuid(uuidBytes).toString()
            val major = ((manufacturerData[18].toInt() and 0xFF) shl 8) or (manufacturerData[19].toInt() and 0xFF)
            val minor = ((manufacturerData[20].toInt() and 0xFF) shl 8) or (manufacturerData[21].toInt() and 0xFF)
            return BleBeaconInfo(
                uuid = uuid,
                major = major,
                minor = minor,
                rssi = scanResult.rssi
            )
        }
        return parseRawBytes(bytes, scanResult.rssi)
    }

    private fun parseRawBytes(bytes: ByteArray, rssi: Int): BleBeaconInfo? {
        // Search array for 0x02, 0x15 iBeacon header
        var startByte = 0
        while (startByte <= bytes.size - 23) {
            if (bytes[startByte] == 0x02.toByte() && bytes[startByte + 1] == 0x15.toByte()) {
                val uuidBytes = bytes.copyOfRange(startByte + 2, startByte + 18)
                val uuid = bytesToUuid(uuidBytes).toString()
                val major = ((bytes[startByte + 18].toInt() and 0xFF) shl 8) or (bytes[startByte + 19].toInt() and 0xFF)
                val minor = ((bytes[startByte + 20].toInt() and 0xFF) shl 8) or (bytes[startByte + 21].toInt() and 0xFF)
                return BleBeaconInfo(
                    uuid = uuid,
                    major = major,
                    minor = minor,
                    rssi = rssi
                )
            }
            startByte++
        }
        return null
    }

    private fun bytesToUuid(bytes: ByteArray): UUID {
        val buffer = ByteBuffer.wrap(bytes)
        val firstLong = buffer.long
        val secondLong = buffer.long
        return UUID(firstLong, secondLong)
    }

    private fun getSimulatedBleBeacons(): List<BleBeaconInfo> {
        return listOf(
            BleBeaconInfo(
                uuid = "fda50693-a4e2-4fb1-afcf-c6eb07647825",
                major = 1,
                minor = 102,
                rssi = -55
            )
        )
    }
}
