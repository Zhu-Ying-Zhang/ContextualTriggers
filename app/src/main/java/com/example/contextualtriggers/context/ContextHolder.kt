package com.example.contextualtriggers.context

class ContextHolder: ContextAPI {

    var noMovement = false

    override fun noMovement(): Boolean = noMovement
}