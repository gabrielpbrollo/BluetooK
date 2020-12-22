package br.com.bluetook

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.bluetook.contract.BluetooKServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException

class BluetooKServerSample : AppCompatActivity() {

    private lateinit var server: BluetooKServer

    //View
    private val tvReceivedData: TextView by lazy { findViewById(R.id.tvReceivedData) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetook_server_sample)

        startServer()
    }

    private fun startServer() {
        server = BluetooKServer.create().apply {
            start()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            server.clientAccepted().collect {
                appendInformation("Accepted client ${it.remoteDevice.name}")
                receiveDataFromClient(it)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            server.getState().collect {
                appendInformation("State $it")
            }
        }
    }

    private fun receiveDataFromClient(client: BluetoothSocket) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                while (client.isConnected) {
                    appendInformation("Received ${client.inputStream.read()}")
                }
            } catch (ex: IOException) {
                appendInformation("Client Disconnected ${client.remoteDevice.name}")
            }
        }
    }

    private fun appendInformation(info: String) {
        runOnUiThread {
            tvReceivedData.append(info)
            tvReceivedData.append("\n")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        server.stop()
    }
}