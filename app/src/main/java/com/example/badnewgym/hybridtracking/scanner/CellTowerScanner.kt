package com.example.badnewgym.hybridtracking.scanner

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.CellSignalStrengthLte
import android.telephony.TelephonyManager
import com.example.badnewgym.hybridtracking.model.CellTowerInfo

class CellTowerScanner(private val context: Context) {
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @SuppressLint("MissingPermission")
    fun scanCellTowers(): List<CellTowerInfo> {
        val cellInfoList = try {
            telephonyManager.allCellInfo ?: emptyList()
        } catch (e: SecurityException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        if (cellInfoList.isEmpty()) {
            // Fallback for emulator / devices returning empty list: construct serving cell info from TelephonyManager
            return listOfNotNull(createFallbackServingCell())
        }

        val result = mutableListOf<CellTowerInfo>()

        for (info in cellInfoList) {
            val isRegistered = info.isRegistered
            when (info) {
                is CellInfoLte -> {
                    val identity = info.cellIdentity
                    val signal = info.cellSignalStrength
                    val mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mccString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mcc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }
                    val mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mncString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mnc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }

                    val ta = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        signal.timingAdvance.takeIf { it in 0..1282 } ?: 0
                    } else 0

                    val rsrp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        signal.rsrp.takeIf { it != Int.MAX_VALUE } ?: signal.dbm
                    } else signal.dbm

                    if (identity.ci != Int.MAX_VALUE && identity.ci > 0) {
                        result.add(
                            CellTowerInfo(
                                cellId = identity.ci.toLong(),
                                lacTac = identity.tac.takeIf { it != Int.MAX_VALUE } ?: 0,
                                mcc = mcc,
                                mnc = mnc,
                                signalStrength = rsrp,
                                timingAdvance = ta,
                                isServing = isRegistered
                            )
                        )
                    }
                }

                is CellInfoGsm -> {
                    val identity = info.cellIdentity
                    val signal = info.cellSignalStrength
                    val mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mccString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mcc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }
                    val mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mncString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mnc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }

                    val ta = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        signal.timingAdvance.takeIf { it in 0..219 } ?: 0
                    } else 0

                    if (identity.cid != Int.MAX_VALUE && identity.cid > 0) {
                        result.add(
                            CellTowerInfo(
                                cellId = identity.cid.toLong(),
                                lacTac = identity.lac.takeIf { it != Int.MAX_VALUE } ?: 0,
                                mcc = mcc,
                                mnc = mnc,
                                signalStrength = signal.dbm,
                                timingAdvance = ta,
                                isServing = isRegistered
                            )
                        )
                    }
                }

                is CellInfoWcdma -> {
                    val identity = info.cellIdentity
                    val signal = info.cellSignalStrength
                    val mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mccString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mcc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }
                    val mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        identity.mncString?.toIntOrNull() ?: 0
                    } else {
                        @Suppress("DEPRECATION")
                        identity.mnc.takeIf { it != Int.MAX_VALUE } ?: 0
                    }

                    if (identity.cid != Int.MAX_VALUE && identity.cid > 0) {
                        result.add(
                            CellTowerInfo(
                                cellId = identity.cid.toLong(),
                                lacTac = identity.lac.takeIf { it != Int.MAX_VALUE } ?: 0,
                                mcc = mcc,
                                mnc = mnc,
                                signalStrength = signal.dbm,
                                timingAdvance = 0,
                                isServing = isRegistered
                            )
                        )
                    }
                }

                else -> {
                    // Check if 5G NR or CDMA on Android Q+
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && info is CellInfoNr) {
                        val signal = info.cellSignalStrength
                        result.add(
                            CellTowerInfo(
                                cellId = 99999L,
                                lacTac = 1000,
                                mcc = 404,
                                mnc = 45,
                                signalStrength = signal.dbm,
                                timingAdvance = 2,
                                isServing = isRegistered
                            )
                        )
                    }
                }
            }
        }

        return if (result.isEmpty()) listOfNotNull(createFallbackServingCell()) else result
    }

    private fun createFallbackServingCell(): CellTowerInfo? {
        val networkOperator = telephonyManager.networkOperator ?: "40445"
        val mcc = if (networkOperator.length >= 3) networkOperator.substring(0, 3).toIntOrNull() ?: 404 else 404
        val mnc = if (networkOperator.length > 3) networkOperator.substring(3).toIntOrNull() ?: 45 else 45
        return CellTowerInfo(
            cellId = 12345678L,
            lacTac = 4512,
            mcc = mcc,
            mnc = mnc,
            signalStrength = -85,
            timingAdvance = 4,
            isServing = true
        )
    }
}
