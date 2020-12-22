package br.com.bluetook.helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetooKHelper(private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) {

    fun bluetoothAvailable(): Boolean {
        try {
            if (!bluetoothAdapter.isEnabled) {
                println("bluetooth is disabled")
                return false
            }

            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
    }

    fun getPairedDevicesName(): List<String> {
        return getPairedDevices()
            .map { it.name }
            .toList()
    }

    fun getPairedDevicesAddress(): List<String> {
        return getPairedDevices()
            .map { it.address }
            .toList()
    }

    fun getPairedDevices() = bluetoothAdapter.bondedDevices

    fun getDeviceByName(name: String): BluetoothDevice? {
        return try {
            getPairedDevices()
                .first { it.name == name }
        } catch (ex: NoSuchElementException) {
            null
        }
    }

    fun getDeviceByAddress(address: String): BluetoothDevice? {
        return try {
            getPairedDevices()
                .first { it.address == address }
        } catch (ex: NoSuchElementException) {
            null
        }
    }
}