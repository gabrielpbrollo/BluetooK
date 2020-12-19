package br.com.bluetook

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bluetook.contract.BluetooKClient
import br.com.bluetook.contract.BluetooKServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BluetooKSample : AppCompatActivity() {

    private lateinit var server: BluetooKServer
    private lateinit var client: BluetooKClient

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetoothk_sample)

        sampleServer()
        sampleClient()
    }

    private fun sampleClient() {
        client = BluetooKClient.create(getBluetoothDeviceBy(), false)

        coroutineScope.launch {
            client.connect()

            client.receiveData().collect {
                println("Client emitted data ${it.toChar()}")
            }

            client.getState().collect {
                println("Client state $it")
            }

            client.sendString("Test")
        }
    }

    private fun getBluetoothDeviceBy(): BluetoothDevice {
        //Create methods to help get bluetooth device by MAC or name
        TODO("Not yet implemented")
    }

    private fun sampleServer() {
        server = BluetooKServer.create().apply {
            start()
        }

        coroutineScope.launch {
            server.clientAccepted().collect {
                println("Server accepted client ${it.remoteDevice.name}")
            }
        }

        coroutineScope.launch {
            server.getState().collect {
                println("Server state $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        client.disconnect()
        server.stop()
    }
}