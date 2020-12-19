package br.com.bluetook.contract

import android.Manifest
import android.bluetooth.BluetoothSocket
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

    fun clientAccepted(): Flow<BluetoothSocket>

    fun getState(): Flow<ServerState>

    fun getCurrentState(): ServerState

    enum class ServerState {
        RAW,
        STARTED,
        ACCEPTING,
        STOPPED
    }
}