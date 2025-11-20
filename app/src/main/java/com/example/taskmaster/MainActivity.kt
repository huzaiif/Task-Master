package com.example.taskmaster

import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaster.database.NoteDatabase
import com.example.taskmaster.notification.NotificationHelper
import com.example.taskmaster.repository.NoteRepository
import com.example.taskmaster.viewmodel.NoteViewModel
import com.example.taskmaster.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create notification channel ONCE
        NotificationHelper.createChannel(this)

        askExactAlarmPermission()

        setupViewModel()
    }

    private fun setupViewModel() {
        val noteRepository = NoteRepository(NoteDatabase(this))
        val factory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
    }


    private fun askExactAlarmPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val alarmManager = getSystemService(AlarmManager::class.java)

            // Already granted → Do nothing
            if (alarmManager.canScheduleExactAlarms()) return

            // Not granted → Request system page
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }
}
