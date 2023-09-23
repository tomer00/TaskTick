package com.tomer.tasktick.modals

data class Task(
    val id: Int = 0,
    val des: String,
    val timeCreated: Long,
    val timeDone: Long = 0L,
    val priority:Int,//1=low 2=medium 3=urgent
    var isDone: Boolean = false
)
