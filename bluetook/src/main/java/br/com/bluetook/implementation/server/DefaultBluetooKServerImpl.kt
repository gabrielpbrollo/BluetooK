package br.com.bluetook.implementation.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import br.com.bluetook.contract.BluetooKServer
import br.com.bluetook.contract.BluetooKServer.ServerData
import br.com.bluetook.contract.BluetooKServer.ServerState
import br.com.bluetook.contract.exception.BluetooKServerIsNotStarted
import br.com.bluetook.extension.safeClose
import br.com.bluetook.implementation.UUIDHelper
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException


class DefaultBluetooKServerImpl : BluetooKServer {

    companion object {
        private const val NAME_SECURE = "BluetooK Server"
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var serverSocket: BluetoothServerSocket
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val state = ServerStateManager()

    override fun start() {
        try {
            serverSocket = if (/*TODO need? isAndroid*/false) {
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    NAME_SECURE, UUIDHelper.UUID_ANDROID_DEVICE
                )
            } else {
                bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    NAME_SECURE, UUIDHelper.UUID_OTHER_DEVICE
                )
            }

            state.setStarted()

            startToAccept()
        } catch (e: IOException) {
            stop()
        }
    }

    private fun startToAccept() {
        scope.launch {
            while (state.isStarted()) {
                try {
                    onClientAccepted(serverSocket.accept())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun onClientAccepted(client: BluetoothSocket) {

    }

    override fun stop() {
        scope.cancel()
        serverSocket.safeClose()

        state.setStopped()
    }

    override fun receiveData(): Flow<ServerData> = callbackFlow {
//        requireStarted()
//
//        try {
//            while (state.isStarted()) {
//                emit(inStream.read().toByte())
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }


    }

    override fun getState(): Flow<ServerState> = callbackFlow {
        offer(state.getCurrentState())

        state.onStateChanged = { deviceState ->
            offer(deviceState)
        }

        awaitClose { state.onStateChanged = null }
    }

    override fun getCurrentState(): ServerState = state.getCurrentState()

    private fun requireStarted() {
        if (!state.isStarted()) {
            throw BluetooKServerIsNotStarted
        }
    }
}