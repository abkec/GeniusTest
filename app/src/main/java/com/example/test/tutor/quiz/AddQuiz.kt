package com.example.test.tutor.quiz

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_quiz.*

class AddQuiz : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        var titleText = findViewById<EditText>(R.id.titleEditText)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add new quiz"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#03fcfc"))

        continueButton.setOnClickListener{

            val myIntent = Intent(this, AddQuizDetail::class.java)
            myIntent.putExtra("quizTitle", titleText.text)
            startActivity(myIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
