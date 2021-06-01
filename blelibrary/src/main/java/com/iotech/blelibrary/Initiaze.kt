package com.iotech.blelibrary

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import java.util.*
import kotlin.collections.ArrayList

class Initiaze {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning: Boolean = false
    private var mHandler: Handler? = null
    private var mLocationManager: LocationManager? = null
    private var timer: Timer? = null
    private var index = 0

    fun scanLeDevice(enable: Boolean) {
        if (!mBluetoothAdapter!!.isEnabled)
            return
        val mLeScanner = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter?.bluetoothLeScanner
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        if (!mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            enableLocationService()
        } else {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler?.postDelayed({
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        mLeScanner?.stopScan(scanCallback)
                    }
                    mScanning = false
//                    updateDeviceList()
                }, SCAN_PERIOD)
                mScanning = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val scanFilters = ArrayList<ScanFilter>()
                    scanFilters.add(
                        ScanFilter.Builder().setServiceUuid(
                            ParcelUuid.fromString(
                                "0000CC01-0000-1000-8000-00805F9B34FB"
                            )
                        ).build()
                    )
                    val scanSettings: ScanSettings =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                .build()
                        } else {
                            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                                .build()
                        }
                    mLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
                }
            } else {
                mScanning = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mLeScanner?.stopScan(scanCallback)
                }
            }
        }
    }

    private val scanCallback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        object : ScanCallback() {

            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                //            if (!localArrayList.contains(result.device)) {
                //                localArrayList.add(result.device)
                //            }
                Utils.d(result.device.name)
            }

            override fun onBatchScanResults(results: List<ScanResult>) {
                super.onBatchScanResults(results)
//                for (result in results) {
//                    if (!localArrayList.contains(result.device)) {
//                        localArrayList.add(result.device)
//                    }
//                }
            }
        }
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

//    private fun enableLocationService() {
//        AlertDialog.Builder(this)
//            .setMessage("Enable Location?")
//            .setPositiveButton(
//                "Yes"
//            ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
//            .setNegativeButton("No", null)
//            .show()
//    }

    //    private fun updateDeviceList() {
//        if (tempList.isEmpty()) {
//            tempList = localArrayList
//        }
//        finalArrayList.clear()
//        mListAdapter?.clearData()
//        localArrayList.let { finalArrayList.addAll(it) }
//        mListAdapter?.notifyDataSetChanged()
//        if (tempList.size == localArrayList.size) {
//            if (tempList.containsAll(localArrayList)) {
//                if (index != 0) {
//                    tempList = localArrayList
//                    localArrayList.clear()
//                }else{
//                    index = 1
//                }
//            }
//        }else{
//            localArrayList.clear()
//        }
//    }
    companion object {

        private const val REQUEST_ENABLE_BT = 1
        private const val MY_PERMISSIONS_REQUEST_READ_LOCATION = 2
        private const val REQUEST_ENABLE_LOCATION = 3

        // Stops scanning after 3 seconds.
        private const val SCAN_PERIOD: Long = 6500
    }
}