package br.com.bluetook.contract

import kotlinx.coroutines.flow.Flow

interface BluetooKServer {

    fun start()

    fun stop()

    suspend fun receivedData() : Flow<String>

    suspend fun getState(): Flow<ServerState>

    enum class ServerState {
        RAW,
        STARTED,
        STOPPED
    }
}