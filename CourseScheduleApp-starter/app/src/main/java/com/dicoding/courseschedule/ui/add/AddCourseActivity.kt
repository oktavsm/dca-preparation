package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private var startTime = ""
    private var endTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.title = getString(R.string.add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
        viewModel.saved.observe(this) { event ->
            when (event.getContentIfNotHandled()) {
                true -> finish()
                false -> Toast.makeText(this, R.string.input_empty_message, Toast.LENGTH_SHORT).show()
                null -> Unit
            }
        }

        findViewById<View>(R.id.ib_start_time).setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, START_TIME_PICKER)
        }
        findViewById<View>(R.id.ib_end_time).setOnClickListener {
            TimePickerFragment().show(supportFragmentManager, END_TIME_PICKER)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                viewModel.insertCourse(
                    courseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString().trim(),
                    day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition,
                    startTime = startTime,
                    endTime = endTime,
                    lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString().trim(),
                    note = findViewById<TextInputEditText>(R.id.ed_note).text.toString().trim()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val selectedTime = String.format("%02d:%02d", hour, minute)
        when (tag) {
            START_TIME_PICKER -> {
                startTime = selectedTime
                findViewById<TextView>(R.id.tv_start_time).text = selectedTime
            }
            END_TIME_PICKER -> {
                endTime = selectedTime
                findViewById<TextView>(R.id.tv_end_time).text = selectedTime
            }
        }
    }

    companion object {
        private const val START_TIME_PICKER = "start_time_picker"
        private const val END_TIME_PICKER = "end_time_picker"
    }
}
