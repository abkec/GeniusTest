package com.example.test.student.quiz

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.quiz_question.*
import android.content.Intent
import android.os.CountDownTimer
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.test.R
import com.google.firebase.database.*


class QuizQuestion : AppCompatActivity() {

    lateinit var myTimer : CountDownTimer
    lateinit var timerTxt : TextView
    var totalAnswered = 0
    var correct = 0
    var wrong = 0
    lateinit var mDatabase : DatabaseReference
    lateinit var radio: RadioButton
    var totalQuestion = 0
    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.quiz_question)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24px)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#ffff00"))
        totalQuestion = intent.getStringExtra("count").toInt()
        time = intent.getStringExtra("time").toInt()
        timerTxt = findViewById(R.id.timer)

        updateQuestion()
        reverseTimer(time,timerTxt)
    }

    private fun updateQuestion() {

        totalAnswered++
        supportActionBar?.title = "Question " + String.format("%d",totalAnswered)

        if(totalAnswered > totalQuestion) {

            myTimer.cancel()

            Toast.makeText(
                applicationContext,
                "Quiz Completed.",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            val myIntent = Intent(this, QuizResult::class.java)
            myIntent.putExtra("score", correct)
            myIntent.putExtra("totalQuestion", totalQuestion)
            myIntent.putExtra("wrong", wrong)
            myIntent.putExtra("totalAnswered", totalAnswered-1)
            myIntent.putExtra("id", intent.getStringExtra("id"))
            myIntent.putExtra("count", intent.getStringExtra("count"))
            startActivity(myIntent)
        }

        else {

            mDatabase = FirebaseDatabase.getInstance().getReference("QuizDetail")
            val mDatabaseRef = mDatabase.child(intent.getStringExtra("id")).child(totalAnswered.toString())

            mDatabaseRef.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {

                    firstAnswerRadioButton.text = p0.child("opt1").value as String
                    secondAnswerRadioButton.text = p0.child("opt2").value as String
                    thirdAnswerRadioButton.text = p0.child("opt3").value as String
                    fourthAnswerRadioButton.text = p0.child("opt4").value as String
                    questionText.text = p0.child("question").value as String

                    questionRadioGroup.setOnCheckedChangeListener(
                        RadioGroup.OnCheckedChangeListener { group, checkedId ->
                            radio = findViewById(checkedId)
                        })

                    submitButton.setOnClickListener() { view: View ->

                        if (submitButton.text == "Submit") {


                            var id: Int = questionRadioGroup.checkedRadioButtonId

                            if (id==-1) {

                                Toast.makeText(applicationContext,"On button click :" +
                                        " nothing selected",
                                    Toast.LENGTH_SHORT).show()
                            }

                            else {

                                if (radio.text == p0.child("answer").value.toString()) {

                                    Snackbar.make(findViewById(R.id.drawerLayout), "Correct answer! ", Snackbar.LENGTH_SHORT).show()
                                    submitButton.setBackgroundColor(Color.GREEN)
                                    correct++
                                }

                                else {

                                    Snackbar.make(findViewById(R.id.drawerLayout), "Sorry, the answer is '" + p0.child("answer").value.toString() + "'", Snackbar.LENGTH_SHORT).show()
                                    submitButton.setBackgroundColor(Color.RED)
                                    wrong++
                                }

                                submitButton.text = "Continue"
                            }

                        }

                        else {

                            submitButton.setBackgroundColor(Color.parseColor("#60A9FF"))
                            radio.isChecked = false
                            submitButton.text = "Submit"
                            updateQuestion()
                        }
                    }
                }
            })

        }
    }

    fun reverseTimer(seconds: Int, tv: TextView) {

        myTimer = object : CountDownTimer(seconds.toLong() * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                var seconds = (millisUntilFinished / 1000).toInt()
                var minutes = seconds / 60
                seconds %= 60

                tv.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds))
            }

            override fun onFinish() {

                Toast.makeText(
                    applicationContext,
                    "Times out.",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

                val myIntent = Intent(this@QuizQuestion, QuizResult::class.java)
                myIntent.putExtra("score", correct)
                myIntent.putExtra("totalQuestion", totalQuestion)
                myIntent.putExtra("wrong", wrong)
                myIntent.putExtra("totalAnswered", totalAnswered-1)
                myIntent.putExtra("id", intent.getStringExtra("id"))
                startActivity(myIntent)
            }
        }.start()
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this@QuizQuestion,
            R.style.AlertDialogCustom
        )

        // Set the alert dialog title
        builder.setTitle("Exit?")

        // Display a message on alert dialog
        builder.setMessage("The quiz progress will be lost!")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Yes"){ _, _ ->
            // Do something when user press the positive button
            Toast.makeText(applicationContext,"Quiz cancelled.",Toast.LENGTH_SHORT).show()
            myTimer.cancel()
            finish()
            super.onBackPressed()
        }

        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }
}
