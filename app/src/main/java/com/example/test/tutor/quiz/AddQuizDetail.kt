package com.example.test.tutor.quiz

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.test.classes.Question
import com.example.test.R
import kotlinx.android.synthetic.main.activity_add_quiz.*

class AddQuizDetail : AppCompatActivity() {

    private val questions: MutableList<Question> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz_detail)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        val questionTxt = findViewById<EditText>(R.id.quizQuestion)
        val option1 = findViewById<EditText>(R.id.opt1)
        val option2 = findViewById<EditText>(R.id.opt2)
        val option3 = findViewById<EditText>(R.id.opt3)
        val option4 = findViewById<EditText>(R.id.opt4)
        var quizTitle = intent.getStringExtra("quizTitle")

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Question"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#03fcfc"))

        continueButton.setOnClickListener{

            if (option1.text.toString() != "" && option2.text.toString() != "" && option3.text.toString() != "" && option4.text.toString() != "") {

                val question = Question(questionTxt.text.toString(), option1.text.toString(), option1.text.toString(),option1.text.toString(),option1.text.toString() )
                questions.add(question)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
