package com.tomer.tasktick.modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val des: String,
    @ColumnInfo(name = "created")
    val timeCreated: Long,
    val timeDone: Long = 0L,
    val priority:Int,//1=low 2=medium 3=urgent
    val isDone: Boolean = false
)
