package br.com.bluetook.model

import br.com.bluetook.contract.BluetooKDevice
import java.lang.Exception

class BluetooKDevice private constructor(builder: Builder):
    BluetooKDevice {

    override fun isPaired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun connect() {
        TODO("Not yet implemented")
    }

    class Builder {

        private var deviceName: String? = null
        private var deviceMAC: String? = null
        private var startServer: Boolean = false

        fun deviceName(deviceName: String) = apply {
            if (deviceMAC.isNullOrBlank().not()) {
                throw BluetooKDeviceBuilderException("deviceMAC already informed")
            }

            this.deviceName = deviceName
        }

        fun deviceMAC(deviceMAC: String) = apply {
            if (deviceName.isNullOrBlank().not()) {
                throw BluetooKDeviceBuilderException("deviceName already informed")
            }

            this.deviceMAC = deviceMAC
        }

        fun startServer(startServer: Boolean) = apply {
            this.startServer = startServer
        }

        fun build(): BluetooKDevice {
            if (deviceName.isNullOrBlank() && deviceMAC.isNullOrBlank()) {
                throw BluetooKDeviceBuilderException("deviceName or deviceMAC not informed")
            }

            return BluetooKDevice(this)
        }

        class BluetooKDeviceBuilderException(message: String): Exception(message)
    }
}