package br.com.bluetook.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BluetooKDeviceTest {

    @Test
    fun `given no config, when build, then throw exception`() {
        assertThrows(BluetooKDevice.Builder.BluetooKDeviceBuilderException::class.java) {
            BluetooKDevice.Builder()
                .build()
        }
    }

    @Test
    fun `given device name and mac, when build, then throw exception`() {
        assertThrows(BluetooKDevice.Builder.BluetooKDeviceBuilderException::class.java) {
            BluetooKDevice.Builder()
                .deviceName("TestDeviceName")
                .deviceMAC("TestDeviceMAC")
                .build()
        }
    }

    @Test
    fun `given device name, when build, then return device`() {
        val device = BluetooKDevice.Builder()
            .deviceName("TestDeviceName")
            .build()

        assertNotNull(device)
    }

    @Test
    fun `given device MAC, when build, then return device`() {
        val device = BluetooKDevice.Builder()
            .deviceMAC("TestDeviceMAC")
            .build()

        assertNotNull(device)
    }
}