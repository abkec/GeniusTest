package com.example.test.student.quiz

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.test.R
import kotlinx.android.synthetic.main.quiz_result.*


class QuizResult : Activity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_result)

        val myIntent = intent
        var correct = myIntent.getIntExtra("score", 0)
        var totalQuestion = myIntent.getIntExtra("totalQuestion", 0)
        var totalAnsweredQuestion = myIntent.getIntExtra("totalAnswered", 0)
        var wrongQuestion = myIntent.getIntExtra("wrong", 0)

        val totalQuestionText : TextView = findViewById(R.id.totalQuestionText)
        val totalAnsweredText : TextView = findViewById(R.id.totalAnswered)
        val correctText : TextView = findViewById(R.id.correctText)
        val wrongText : TextView = findViewById(R.id.wrongText)
        val skipText : TextView = findViewById(R.id.skip)
        val scoreText : TextView = findViewById(R.id.scoreText)

        var score = correct.toDouble().div(totalQuestion).times(100)

        totalQuestionText.text = "Total Questions: " + totalQuestion
        totalAnsweredText.text = "Answered Questions: " + totalAnsweredQuestion
        correctText.text = "Correct Answers: " + correct
        wrongText.text = "Wrong Answers: " + wrongQuestion
        skipText.text = "Missed Questions: " + totalQuestion.minus(totalAnsweredQuestion)
        scoreText.text = "Your score: " + score + " %"

        if (totalAnsweredQuestion == totalQuestion) {

            when {

                score <= 25 -> textView2.text = "Nice try, try harder next time!"
                score <= 50 -> textView2.text = "Not bad, keep it up next time!"
                score <= 75 -> textView2.text = "Good!"
                score <= 100 -> textView2.text = "Excellent! You are a GENIUS!"
            }
        }

        else
            textView2.text = "Oops... times up!"

        val backBut : Button = findViewById(R.id.back)

        backBut.setOnClickListener {

            finish()
        }
    }
}
