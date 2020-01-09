package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.test.R

class MainMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmain)

        val button = findViewById<Button>(R.id.login_button)
        button.setOnClickListener{
            val intent = Intent(this, Login1:: class.java)
            startActivity(intent)
        }

        val button1 = findViewById<Button>(R.id.register_button)
        button1.setOnClickListener{
            val intent = Intent(this, Register:: class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

        finish()
    }
}
