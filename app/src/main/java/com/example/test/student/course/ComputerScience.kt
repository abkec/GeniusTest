package com.example.test.student.course

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.example.test.R

class ComputerScience : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_science)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)


        val btnNextTopic = findViewById<Button>(R.id.btnNextTopic)



    }

    override fun onSupportNavigateUp(): Boolean {

        super.onBackPressed()
        return true
    }
}
