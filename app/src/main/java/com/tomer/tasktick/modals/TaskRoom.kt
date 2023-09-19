package com.tomer.tasktick.modals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    val des: String,
    @ColumnInfo(name = "created")
    val timeCreated: String,
    @ColumnInfo(name = "comp")
    val timeDone: String = "",
    val priority: Int,//1=low 2=medium 3=urgent
    @ColumnInfo(name = "isDone")
    val isDone: Boolean = false
)
