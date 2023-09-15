package com.tomer.tasktick.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tomer.tasktick.modals.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class Database :RoomDatabase() {
    abstract fun taskDao(): Dao
}