package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI




private const val NOTIFICATION_TITLE = "At Work Trigger"
private const val NOTIFICATION_TEXT = "You should take a small break from work and go for a walk!"

class atWork constructor(private val contextHolder: ContextAPI): Trigger{

    override fun getNotificationTitle(): String = NOTIFICATION_TITLE

    override fun getNotificationMessage(): String = NOTIFICATION_TEXT

    override fun getNotificationIntent(): Intent? = null

   override fun isTriggered(): Boolean = contextHolder.atwork()
}

