package com.example.taskmaster.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmaster.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)


    // Default sorting: HIGH → LOW priority, then nearest due task first
    @Query("SELECT * FROM notes ORDER BY priority ASC, taskTime ASC")
    fun getAllNotes(): LiveData<List<Note>>


    // Search by title or description
    @Query("SELECT * FROM notes WHERE noteTitle LIKE :query OR noteDesc LIKE :query ORDER BY priority ASC, taskTime ASC")
    fun searchNote(query: String?): LiveData<List<Note>>


    // Filter only HIGH priority tasks
    @Query("SELECT * FROM notes WHERE priority = 1 ORDER BY taskTime ASC")
    fun getHighPriorityTasks(): LiveData<List<Note>>

    // Filter only MEDIUM priority tasks
    @Query("SELECT * FROM notes WHERE priority = 2 ORDER BY taskTime ASC")
    fun getMediumPriorityTasks(): LiveData<List<Note>>

    // Filter only LOW priority tasks
    @Query("SELECT * FROM notes WHERE priority = 3 ORDER BY taskTime ASC")
    fun getLowPriorityTasks(): LiveData<List<Note>>
}
