package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK
import java.util.concurrent.TimeUnit

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = getParcelableExtra(intent, HABIT, Habit::class.java)

        if (habit != null){
            findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

            val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

            viewModel.setInitialTime(habit.minutesFocus)
            viewModel.currentTimeString.observe(this) {
                findViewById<TextView>(R.id.tv_count_down).text = it
            }
            viewModel.eventCountDownFinish.observe(this) {
                updateButtonState(false)
            }

            findViewById<Button>(R.id.btn_start).setOnClickListener {
                updateButtonState(true)
                viewModel.startTimer()
                startNotificationWork(habit)
            }

            findViewById<Button>(R.id.btn_stop).setOnClickListener {
                WorkManager.getInstance(this).cancelUniqueWork(notificationWorkName(habit.id))
                viewModel.resetTimer()
            }
        }

    }

    private fun startNotificationWork(habit: Habit) {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(habit.minutesFocus, TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    HABIT_ID to habit.id,
                    HABIT_TITLE to habit.title
                )
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniqueWork(
                notificationWorkName(habit.id),
                androidx.work.ExistingWorkPolicy.REPLACE,
                request
            )
    }

    private fun notificationWorkName(habitId: Int): String {
        return "$NOTIF_UNIQUE_WORK-$habitId"
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}
