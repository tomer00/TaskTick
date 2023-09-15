package com.tomer.tasktick.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomer.tasktick.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {

    //region :: GLOBALS-->>

    private val b by lazy { ActivityTaskBinding.inflate(layoutInflater) }

    //endregion :: GLOBALS-->>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.apply {
            btBack.setOnClickListener {

            }
        }
    }


}