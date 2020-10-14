package br.com.bluetook.contract

interface IBluetooKDevice {

    fun isPaired(): Boolean

    fun connect()

    fun dataReceiver(): Flow<>
}