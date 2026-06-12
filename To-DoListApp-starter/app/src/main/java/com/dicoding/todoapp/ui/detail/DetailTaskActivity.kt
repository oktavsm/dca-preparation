package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        val taskId = intent.getIntExtra(TASK_ID, 0)
        viewModel.setTaskId(taskId)
        viewModel.task.observe(this, Observer(this::showTask))

        findViewById<Button>(R.id.btn_delete_task).setOnClickListener {
            viewModel.deleteTask().invokeOnCompletion {
                runOnUiThread { finish() }
            }
        }
    }

    private fun showTask(task: Task) {
        findViewById<TextInputEditText>(R.id.detail_ed_title).setText(task.title)
        findViewById<TextInputEditText>(R.id.detail_ed_description).setText(task.description)
        findViewById<TextInputEditText>(R.id.detail_ed_due_date).setText(
            DateConverter.convertMillisToString(task.dueDateMillis)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
