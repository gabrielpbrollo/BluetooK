package br.com.bluetook.contract

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetooKDevice {

    fun connect(device: BluetoothDevice)

    fun disconnect()

    suspend fun sendData()

    suspend fun receivedData(): Flow<String>

    suspend fun getState(): Flow<DeviceState>

    enum class DeviceState {
        RAW,
        CONNECTED,
        DISCONNECTED
    }
}