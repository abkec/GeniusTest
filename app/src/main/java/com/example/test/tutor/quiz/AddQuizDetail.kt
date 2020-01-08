package com.example.test.tutor.quiz

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.test.classes.Question
import com.example.test.R
import com.example.test.TutorMenu
import com.example.test.student.quiz.QuizQuestion
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_quiz.*
import kotlinx.android.synthetic.main.activity_add_quiz.continueButton
import kotlinx.android.synthetic.main.activity_add_quiz_detail.*
import kotlinx.android.synthetic.main.quiz_list_layout.view.*
import kotlinx.android.synthetic.main.quiz_question.*
import kotlin.coroutines.Continuation

class AddQuizDetail : AppCompatActivity() {

    lateinit var imgurl : Uri
    var total = 0
    var count = 0
    private val questions: MutableList<Question> = mutableListOf()
    lateinit var nextBtn : Button
    lateinit var prevBtn : Button
    lateinit var questionTxt : EditText
    lateinit var option1 : EditText
    lateinit var option2 : EditText
    lateinit var option3 : EditText
    lateinit var option4 : EditText
    lateinit var mDataBaseReference: DatabaseReference
    lateinit var mDataBaseReference2: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mStorageRef: FirebaseStorage
    lateinit var mDatabase2: FirebaseDatabase
    lateinit var img : ImageView
    lateinit var btnUpload : Button
    var totalQuiz = 0
    lateinit var quizImg : Uri

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz_detail)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar

        nextBtn = findViewById(R.id.nextButton)
        prevBtn = findViewById(R.id.prevButton)
        questionTxt = findViewById(R.id.quizQuestion)
        option1 = findViewById(R.id.opt1)
        option2 = findViewById(R.id.opt2)
        option3 = findViewById(R.id.opt3)
        option4 = findViewById(R.id.opt4)
        var quizTitle = intent.getStringExtra("quizTitle")
        var quizTime = intent.getStringExtra("quizTime")
        var quizColor = intent.getStringExtra("quizColor")
        quizImg = intent.getStringExtra("quizImg").toUri()
        btnUpload = findViewById<Button>(R.id.btnUploadPhoto2)
        img = findViewById<ImageView>(R.id.imageView2)
        var radio: RadioButton

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Question"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor(quizColor))

        readQuestion()

        answerRadio.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radio = findViewById(checkedId)
            })

        finishButton.setOnClickListener{

            if (total == 0) {

                Toast.makeText(this,"You must add at least one question.", Toast.LENGTH_SHORT).show()
            }

            else {

                val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

                builder.setTitle("Add Quiz")
                builder.setMessage("Confirm adding $quizTitle?")
                builder.setPositiveButton("Yes"){dialog, which ->

                    mStorageRef = FirebaseStorage.getInstance()
                    mDatabase = FirebaseDatabase.getInstance()

                    mDataBaseReference = mDatabase!!.reference!!.child("Quiz")
                    mDataBaseReference.child(totalQuiz.toString()).child("id").setValue(totalQuiz.toString())
                    mDataBaseReference.child(totalQuiz.toString()).child("title").setValue(quizTitle)
                    mDataBaseReference.child(totalQuiz.toString()).child("time").setValue(quizTime)
                    mDataBaseReference.child(totalQuiz.toString()).child("color").setValue(quizColor)


                    if (quizImg != null) {

                        var fileReference : StorageReference = mStorageRef.reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(quizImg))
                        var imgRef = mDataBaseReference.child(totalQuiz.toString())

                        val ref = fileReference
                        var a = fileReference.putFile(quizImg)
                        val ab = a.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                imgRef.child("image").setValue(task.result.toString())
                            } else {
                                // Handle failures
                                // ...
                            }
                        }

                    }

                    mDatabase = FirebaseDatabase.getInstance()
                    val mDataBaseReference2 = mDatabase!!.reference!!.child("QuizDetail")


                    var qCount = 1
                    questions.forEach {

                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("answer").setValue(questions[qCount-1].answer)
                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("opt1").setValue(questions[qCount-1].opt1)
                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("opt2").setValue(questions[qCount-1].opt2)
                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("opt3").setValue(questions[qCount-1].opt3)
                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("opt4").setValue(questions[qCount-1].opt4)
                        mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString()).child("question").setValue(questions[qCount-1].question)

                        var fileReference2 : StorageReference = mStorageRef.reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(
                            questions[qCount-1].image!!
                        ))
                        var imgRef2 = mDataBaseReference2.child(totalQuiz.toString()).child(qCount.toString())

                        val ref = fileReference2
                        var a = fileReference2.putFile(questions[qCount-1].image!!)
                        val ab = a.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                imgRef2.child("image").setValue(task.result.toString())
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                        qCount++
                    }

                    finish()
                    val intent = Intent(this, TutorMenu::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Quiz added!", Toast.LENGTH_SHORT).show()

                }
                builder.setNeutralButton("Cancel"){_,_ ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        continueButton.setOnClickListener{

            if (count == questions.size) {

                if (option1.text.toString() != "" && option2.text.toString() != "" && option3.text.toString() != "" && option4.text.toString() != "" && answerRadio.checkedRadioButtonId != -1) {

                    var ans: String

                    if (firstButton.isChecked)
                        ans = option1.text.toString()
                    else if (firstButton2.isChecked)
                        ans = option2.text.toString()
                    else if (firstButton3.isChecked)
                        ans = option3.text.toString()
                    else
                        ans = option4.text.toString()

                    val question = Question(questionTxt.text.toString(), option1.text.toString(), option2.text.toString(),option3.text.toString(),option4.text.toString(), ans, imgurl)
                    questions.add(question)
                    total++
                    count++

                    clearText()
                    questionNum.text = "Question " + total.plus(1)
                    Snackbar.make(findViewById(R.id.drawerLayout), "Question added. ", Snackbar.LENGTH_SHORT).show()
                    prevBtn.visibility = View.VISIBLE
                }

                else {

                    Toast.makeText(this,"Please fill in all details.", Toast.LENGTH_SHORT).show()
                }
            }

            else {

                if (option1.text.toString() != "" && option2.text.toString() != "" && option3.text.toString() != "" && option4.text.toString() != "" && answerRadio.checkedRadioButtonId != -1) {

                    var ans: String

                    if (firstButton.isChecked)
                        ans = option1.text.toString()
                    else if (firstButton2.isChecked)
                        ans = option2.text.toString()
                    else if (firstButton3.isChecked)
                        ans = option3.text.toString()
                    else
                        ans = option4.text.toString()

                    val question = Question(questionTxt.text.toString(), option1.text.toString(), option2.text.toString(),option3.text.toString(),option4.text.toString(), ans)
                    questions.set(count, question)
                    Snackbar.make(findViewById(R.id.drawerLayout), "Question updated. ", Snackbar.LENGTH_SHORT).show()
                    img.setImageResource(0)
                }
            }
        }

        nextBtn.setOnClickListener{

            if (count.plus(1) < questions.size) {

                count++
                setAllText(count)
                continueButton.text = "Update Question"
            }

            else if (count.plus(1) == questions.size){
                count++
                clearText()
                questionNum.text = "Question " + total.plus(1)
                continueButton.text = "Add Question"
            }
        }

        prevBtn.setOnClickListener{

            if (count.minus(1) >= 0) {

                count--
                setAllText(count)
                continueButton.text = "Update Question"
            }
        }

        btnUpload.setOnClickListener{

            filechooser()
        }
    }

    private fun filechooser() {

        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {

            imgurl = data.getData()!!
            Picasso.with(this).load(imgurl).into(img)
        }

    }

    private fun getFileExtension (uri: Uri) : String? {

        var cR : ContentResolver = contentResolver
        var mime : MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun readQuestion() {

        mDatabase2 = FirebaseDatabase.getInstance()
        mDataBaseReference2 = mDatabase2!!.reference!!.child("Quiz")

        mDataBaseReference2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                totalQuiz = p0.childrenCount.toInt().plus(1)
            }
        })
    }

    private fun clearText() {

        questionTxt.setText("")
        option1.setText("")
        option2.setText("")
        option3.setText("")
        option4.setText("")
        img.setImageResource(0)
    }

    private fun setAllText(counter : Int) {

        var displayQuestion = questions[counter]
        questionTxt.setText(displayQuestion.question)
        option1.setText(displayQuestion.opt1)
        option2.setText(displayQuestion.opt2)
        option3.setText(displayQuestion.opt3)
        option4.setText(displayQuestion.opt4)
        Picasso.with(this).load(displayQuestion.image).into(img)

        if (displayQuestion.opt1 == displayQuestion.answer)
            firstButton.setChecked(true)
        else if (displayQuestion.opt2 == displayQuestion.answer)
            firstButton2.setChecked(true)
        else if (displayQuestion.opt3 == displayQuestion.answer)
            firstButton3.setChecked(true)
        else
            firstButton4.setChecked(true)

        questionNum.text = "Question " + counter.plus(1)
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
