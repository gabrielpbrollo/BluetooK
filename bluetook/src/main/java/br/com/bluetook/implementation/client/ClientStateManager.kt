package br.com.bluetook.implementation.client

import br.com.bluetook.contract.BluetooKClient.ClientState

class ClientStateManager {

    //TODO LOCK ON THIS?
    private var state: ClientState = ClientState.RAW

    var onStateChanged: ((ClientState) -> Unit)? = null

    fun getCurrentState() = state

    fun isConnected() = state == ClientState.CONNECTED

    fun setConnected() {
        state = ClientState.CONNECTED
        onStateChanged?.invoke(state)
    }

    fun setDisconnected() {
        state = ClientState.DISCONNECTED
        onStateChanged?.invoke(state)
    }
}