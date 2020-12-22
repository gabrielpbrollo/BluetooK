package br.com.bluetook

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.bluetook.contract.BluetooKClient
import br.com.bluetook.helper.BluetooKHelper
import br.com.bluetook.implementation.UUIDHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BluetooKClientSample : AppCompatActivity() {

    private var client: BluetooKClient? = null

    //View
    private val tvReceivedData: TextView by lazy { findViewById(R.id.tvReceivedData) }
    private val etEmittingValue: EditText by lazy { findViewById(R.id.etEmittingValue) }
    private val etTargetName: EditText by lazy { findViewById(R.id.etTargetName) }
    private val btSend: Button by lazy { findViewById(R.id.btSend) }
    private val btConnect: Button by lazy { findViewById(R.id.btConnect) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetook_client_sample)

        initListeners()
    }

    private fun initListeners() {
        btSend.setOnClickListener { sendData() }
        btConnect.setOnClickListener { connectToServer() }
    }

    private fun sendData() {
        val data = etEmittingValue.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            client?.sendString(data)
        }
    }

    private fun connectToServer() {
        val targetName = etTargetName.text.toString()

        try {
            client = BluetooKClient.create(getBluetoothDeviceBy(targetName), UUIDHelper.DEFAULT_UUID_DEVICE)

            lifecycleScope.launch(Dispatchers.IO) {
                client?.connect()

                client?.receiveData()?.collect {
                    appendInformation("Received data ${it.toChar()}")
                }
            }

            lifecycleScope.launch(Dispatchers.IO) {
                client?.getState()?.collect {
                    appendInformation("State $it")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun appendInformation(info: String) {
        runOnUiThread {
            tvReceivedData.append(info)
            tvReceivedData.append("\n")
        }
    }

    private fun getBluetoothDeviceBy(targetName: String): BluetoothDevice {
        return BluetooKHelper().getDeviceByName(targetName)
            ?: throw RuntimeException("Device not found with $targetName")
    }

    override fun onDestroy() {
        super.onDestroy()

        client?.disconnect()
    }
}