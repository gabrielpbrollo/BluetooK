package br.com.bluetook.contract

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import br.com.bluetook.implementation.server.DefaultBluetooKServerImpl
import kotlinx.coroutines.flow.Flow

interface BluetooKServer {

    companion object {
        fun create(): BluetooKServer {
            return DefaultBluetooKServerImpl()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun start()

    fun stop()

    fun receiveData(): Flow<ServerData>

    fun getState(): Flow<ServerState>

    fun getCurrentState(): ServerState

    data class ServerData(val client: BluetoothDevice, val data: Byte)

    enum class ServerState {
        RAW,
        STARTED,
        STOPPED
    }
}