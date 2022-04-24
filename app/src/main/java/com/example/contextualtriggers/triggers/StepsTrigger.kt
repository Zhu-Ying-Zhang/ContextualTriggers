package com.example.contextualtriggers.triggers

import android.content.Intent
import com.example.contextualtriggers.context.ContextAPI
import com.example.contextualtriggers.context.util.isNightTime
import java.util.*

class StepsTrigger(
    private val contextHolder: ContextAPI,
): Trigger {

    private val NOTIFICATION_TITLE = "Steps Trigger"

    private val NOTIFICATION_TEXT = "Your not on track to meet your daily steps goal, go for a walk" +
            "to get a few steps closer! Current steps: "

    private val TARGET_STEPS = 10000

    private var mSteps = 0

    override fun getNotificationTitle(): String {
        return NOTIFICATION_TITLE
    }

    override fun getNotificationMessage(): String {
        return "$NOTIFICATION_TEXT $mSteps"
    }

    override fun getNotificationIntent(): Intent? {
        return null
    }

    override suspend fun isTriggered(): Boolean {
        if(contextHolder.isInEvent() || isNightTime())
            return false
        mSteps = contextHolder.getSteps()
//        println("Steps walked today: $mSteps")
//        val averageSteps = getWeeklyAverage()
//        println("Average steps: $averageSteps")
//        val estimation = getEstimation(mSteps)
//        println("Estimation: $estimation")
//        return estimation < TARGET_STEPS
        return true
    }

    /**
     * Provides an estimation of how many steps the user will walk today based on the given number
     * of steps walked so far today.
     *
     * @param steps  steps walked so far today
     * @return estimated total number of steps
     */
    private fun getEstimation(steps: Int): Int {
        val millisInDay = (24 * 60 * 60 * 1000).toLong()
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        val startToday = cal.timeInMillis
        val timeToday = System.currentTimeMillis() - startToday
        val percentage = timeToday.toDouble() / millisInDay
        return (steps / percentage).toInt()
    }

    /**
     * Returns the average number of steps walked by the user over the previous week.
     *
     * @return average number of steps.
     */
    private suspend fun getWeeklyAverage(): Int {
        val stepsWeek: MutableList<Int> = ArrayList()
        for (i in 0..6) {
            val steps: Int = contextHolder.getSteps()
            if (steps > 0) {
                println((i + 1).toString() + " days ago: " + steps)
                stepsWeek.add(steps)
            }
        }
        var averageSteps = 0
        var count = 0
        for (steps in stepsWeek) {
            if (steps > 0) {
                averageSteps += steps
                count++
            }
        }
        if (count > 0) {
            averageSteps /= count
        } else {
            averageSteps = 0
        }
        return averageSteps
    }
}