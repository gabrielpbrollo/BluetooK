package br.com.bluetook.implementation.client

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import br.com.bluetook.contract.BluetooKClient
import br.com.bluetook.contract.BluetooKClient.ClientState
import br.com.bluetook.contract.exception.BluetooKDeviceIsNotConnected
import br.com.bluetook.extension.safeClose
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.util.*


class DefaultBluetootKClientImpl(
    private val device: BluetoothDevice,
    private val isAndroid: Boolean
) : BluetooKClient {

    companion object {
        private val UUID_ANDROID_DEVICE: UUID =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")
        private val UUID_OTHER_DEVICE: UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private lateinit var socket: BluetoothSocket
    private lateinit var inStream: InputStream
    private lateinit var outStream: OutputStream

    private val state = ClientStateManager()


    override suspend fun connect() {

        this.socket = if (isAndroid) {
            device.createRfcommSocketToServiceRecord(UUID_ANDROID_DEVICE)
        } else {
            device.createRfcommSocketToServiceRecord(UUID_OTHER_DEVICE)
        }

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
        socket.safeClose()
        inStream.safeClose()
        outStream.safeClose()

        state.setDisconnected()
    }

    override suspend fun sendString(data: String) {
        sendBytes(data.toByteArray(StandardCharsets.UTF_8))
    }

    override suspend fun sendBytes(data: ByteArray) {
        requireConnected()

        outStream.write(data)
    }

    override fun receiveData(): Flow<Byte> {
        requireConnected()

        return flow {
            try {
                while (state.isConnected()) {
                    emit(inStream.read().toByte())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
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
}