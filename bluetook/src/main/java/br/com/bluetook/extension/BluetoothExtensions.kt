package br.com.bluetook.extension

import android.bluetooth.BluetoothSocket
import br.com.bluetook.implementation.UUIDHelper
import java.util.*

fun BluetoothSocket.getUUID(): UUID {
    return try {
        this.remoteDevice.uuids[0].uuid
    } catch (ex: Exception) {
        UUIDHelper.DEFAULT_UUID_DEVICE
    }
}