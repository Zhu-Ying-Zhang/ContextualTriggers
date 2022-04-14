package com.example.contextualtriggers.context

interface ContextAPI {
    fun noMovement(): Boolean

    fun getBatteryLevel(): Int
}