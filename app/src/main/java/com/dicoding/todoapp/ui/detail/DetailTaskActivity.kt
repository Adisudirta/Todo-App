package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        val title = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val description = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val dueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)

        val taskId = intent.getIntExtra(TASK_ID, 0)

        detailTaskViewModel.setTaskId(taskId)
        detailTaskViewModel.task.observe(this) {
            title.text = Editable.Factory.getInstance().newEditable(it.title)
            description.text = Editable.Factory.getInstance().newEditable(it.description)
            dueDate.text = Editable.Factory.getInstance()
                .newEditable(DateConverter.convertMillisToString(it.dueDateMillis))
        }

        btnDelete.setOnClickListener {
            detailTaskViewModel.task.removeObservers(this)
            detailTaskViewModel.deleteTask()
            onBackPressed()
        }
    }
}