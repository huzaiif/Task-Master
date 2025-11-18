package com.example.taskmaster.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val noteTitle: String,
    val noteDesc: String,

    // NEW FIELD - stores task time (epoch millis)
    val taskTime: Long,

    // NEW FIELD - 1 = High , 2 = Medium , 3 = Low
    val priority: Int

) : Parcelable
