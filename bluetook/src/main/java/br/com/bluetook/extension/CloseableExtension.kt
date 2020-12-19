package br.com.bluetook.extension

import java.io.Closeable

internal fun Closeable.safeClose() {
    try {
        this.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}