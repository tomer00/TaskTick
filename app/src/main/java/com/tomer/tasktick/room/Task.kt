package com.tomer.tasktick.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "created")
    val timeCreated: Long,
    val timeDone: Long,
    val priority:Int,
    val isDone: Boolean
)
