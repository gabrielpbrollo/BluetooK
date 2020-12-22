package br.com.bluetook.implementation.client

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import br.com.bluetook.contract.BluetooKClient
import br.com.bluetook.contract.BluetooKClient.ClientState
import br.com.bluetook.contract.exception.BluetooKDeviceIsNotConnected
import br.com.bluetook.contract.exception.BluetoothIsNotEnabled
import br.com.bluetook.extension.getUUID
import br.com.bluetook.extension.safeClose
import br.com.bluetook.helper.BluetooKHelper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.util.*

class DefaultBluetootKClientImpl private constructor(
    private val device: BluetoothDevice,
    private val uuid: UUID
) : BluetooKClient {

    companion object {
        fun new(device: BluetoothDevice, uuid: UUID): DefaultBluetootKClientImpl {
            return DefaultBluetootKClientImpl(device, uuid)
        }

        fun withAcceptedClient(clientSocket: BluetoothSocket): DefaultBluetootKClientImpl {
            return DefaultBluetootKClientImpl(
                clientSocket.remoteDevice,
                clientSocket.getUUID()
            ).apply {
                setAcceptedConnection(clientSocket)
            }
        }
    }

    private fun setAcceptedConnection(clientSocket: BluetoothSocket) {
        this.socket = clientSocket
        this.inStream = clientSocket.inputStream
        this.outStream = clientSocket.outputStream
        this.state.setConnected()
    }

    private val TAG = "DefaultBluetootKClientImpl"

    private lateinit var socket: BluetoothSocket
    private lateinit var inStream: InputStream
    private lateinit var outStream: OutputStream

    private val state = ClientStateManager()

    override suspend fun connect() {
        requireBluetoothEnabled()

        if (state.isConnected()) {
            Log.d(TAG, "connect: Already connected")
            return
        }

        this.socket = device.createRfcommSocketToServiceRecord(uuid)

        try {
            socket.connect()

            inStream = socket.inputStream
            outStream = socket.outputStream

            state.setConnected()
        } catch (ex: Exception) {
            disconnect()
        }
    }

    override fun disconnect() {
        if (this::socket.isInitialized) {
            //making sure it can be closed
            socket.safeClose()
            inStream.safeClose()
            outStream.safeClose()
        }

        state.setDisconnected()
    }

    override suspend fun sendString(data: String) {
        sendBytes(data.toByteArray(StandardCharsets.UTF_8))
    }

    override suspend fun sendBytes(data: ByteArray) {
        requireConnected()

        try {
            outStream.write(data)
        } catch (ex: Exception) {
            Log.e(TAG, "sendBytes: ${ex.message}")
            disconnect()
        }
    }

    override fun receiveData(): Flow<Byte> {
        requireConnected()

        return flow {
            try {
                while (state.isConnected()) {
                    emit(inStream.read().toByte())
                }
            } catch (ex: Exception) {
                disconnect()
            }
        }
    }

    override fun getState(): Flow<ClientState> = callbackFlow {
        offer(state.getCurrentState())

        state.onStateChanged = { deviceState ->
            offer(deviceState)
        }

        awaitClose { state.onStateChanged = null }
    }

    override fun getCurrentState(): ClientState = state.getCurrentState()

    private fun requireConnected() {
        if (!state.isConnected()) {
            throw BluetooKDeviceIsNotConnected
        }
    }

    private fun requireBluetoothEnabled() {
        if (!BluetooKHelper().bluetoothAvailable()) {
            throw BluetoothIsNotEnabled
        }
    }
}