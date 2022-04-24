package com.example.contextualtriggers.triggers

import android.content.Intent
import android.util.Log
import com.example.contextualtriggers.context.ContextAPI
import com.example.contextualtriggers.context.util.CurrentTime
import com.example.contextualtriggers.context.util.isNightTime
import java.util.*

class StepsTrigger(
    private val contextHolder: ContextAPI,
): Trigger {

    private val NOTIFICATION_TITLE = "Steps Trigger"

    private val NOTIFICATION_TEXT = "Your not on track to meet your daily steps goal, go for a walk" +
            "to get a few steps closer! Current steps: "

    private var steps = 0

    override fun getNotificationTitle(): String {
        return NOTIFICATION_TITLE
    }

    override fun getNotificationMessage(): String {
        return "$NOTIFICATION_TEXT $steps"
    }

    override fun getNotificationIntent(): Intent? {
        return null
    }

    override suspend fun isTriggered(): Boolean {
        if(contextHolder.isInEvent() || isNightTime())
            return false

        steps = contextHolder.getSteps()
        val prediction = getPrediction(steps)
        Log.d("Steps","Prediction: $prediction")
        return prediction < contextHolder.getGoal()
    }

    private fun getPrediction(steps: Int): Int {
        val currentTime = CurrentTime().subSequence(0, 2).toString()
        val percentage = currentTime.toDouble() / 24
        return (steps / percentage).toInt()
    }
}