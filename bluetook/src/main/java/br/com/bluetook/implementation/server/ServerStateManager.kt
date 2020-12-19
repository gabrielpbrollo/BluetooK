package br.com.bluetook.implementation.server

import br.com.bluetook.contract.BluetooKServer

class ServerStateManager {

    //TODO LOCK ON THIS?
    private var state: BluetooKServer.ServerState = BluetooKServer.ServerState.RAW

    var onStateChanged: ((BluetooKServer.ServerState) -> Unit)? = null

    fun getCurrentState() = state

    fun isAccepting() = state == BluetooKServer.ServerState.ACCEPTING

    fun setStarted() {
        state = BluetooKServer.ServerState.STARTED
        onStateChanged?.invoke(state)
    }

    fun setStopped() {
        state = BluetooKServer.ServerState.STOPPED
        onStateChanged?.invoke(state)
    }

    fun setAccepting() {
        state = BluetooKServer.ServerState.ACCEPTING
        onStateChanged?.invoke(state)
    }
}