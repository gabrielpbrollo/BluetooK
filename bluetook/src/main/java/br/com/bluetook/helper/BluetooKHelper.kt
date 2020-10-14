package br.com.bluetook.helper

import android.bluetooth.BluetoothAdapter

class BluetooKHelper(private val bluetoothAdapter: BluetoothAdapter) {

    fun bluetoothAvailable(): Boolean {
        try {
            if (bluetoothAdapter.isEnabled.not()) {
                println("bluetooth is disabled")
                return false
            }

            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
    }
}