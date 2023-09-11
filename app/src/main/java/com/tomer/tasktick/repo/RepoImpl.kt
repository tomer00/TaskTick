package com.tomer.tasktick.repo

import com.tomer.tasktick.room.Dao
import javax.inject.Inject

class RepoImpl @Inject constructor( private val ret: Dao) : MainRepo {
    override fun getData() {

    }
}