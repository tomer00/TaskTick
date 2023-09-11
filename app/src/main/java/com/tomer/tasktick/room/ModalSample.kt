package com.tomer.tasktick.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code")
data class ModalSample(
    @PrimaryKey
    val f:String
)
