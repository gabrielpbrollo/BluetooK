package br.com.bluetook.contract

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import br.com.bluetook.implementation.client.DefaultBluetootKClientImpl
import kotlinx.coroutines.flow.Flow

interface BluetooKClient {

    companion object {
        fun create(device: BluetoothDevice, isAndroid: Boolean): BluetooKClient {
            return DefaultBluetootKClientImpl(device, isAndroid)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    suspend fun connect()

    fun disconnect()

    suspend fun sendString(data: String)

    suspend fun sendBytes(data: ByteArray)

    fun receiveData(): Flow<Byte>

    fun getState(): Flow<ClientState>

    fun getCurrentState(): ClientState

    enum class ClientState {
        RAW,
        CONNECTED,
        DISCONNECTED
    }
}