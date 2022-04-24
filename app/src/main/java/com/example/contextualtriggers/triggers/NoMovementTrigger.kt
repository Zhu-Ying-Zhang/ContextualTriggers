package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextInterface
import com.example.contextualtriggers.context.util.isNightTime

private const val NOTIFICATION_TITLE = "No Movement Trigger"
private const val NOTIFICATION_TEXT = "Hey! Don't sit, stand up and go for a walk!"

class NoMovementTrigger constructor(private val contextHolder: ContextInterface): Trigger{

    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String = NOTIFICATION_TEXT

    override fun getNotificationIntent(): Intent? = null

    override suspend fun isTriggered(): Boolean {
        if(contextHolder.isInEvent() || isNightTime())
            return false

        return contextHolder.noMovement()
    }
}