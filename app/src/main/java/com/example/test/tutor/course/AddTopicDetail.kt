package com.example.test.tutor.course

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri

import com.example.test.R
import com.example.test.TutorMenu
import com.example.test.classes.Question
import com.example.test.classes.Topic
import com.example.test.classes.Tutor
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_course_detail.*


class AddTopicDetail : AppCompatActivity() {

    lateinit var courseImgUrl : Uri
    var total = 0
    var count = 0
    private val topics: MutableList<Topic> = mutableListOf()
    lateinit var nextBtn : Button
    lateinit var prevBtn : Button
    lateinit var topicTxt : EditText
    lateinit var topicContent : EditText
    lateinit var mDataBaseReference: DatabaseReference
    lateinit var mDataBaseReference2: DatabaseReference
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mStorageRef: FirebaseStorage
    lateinit var mDatabase2: FirebaseDatabase
    lateinit var img : ImageView
    lateinit var btnUpload : Button
    var totalCourse = 0
    lateinit var topicImg : Uri

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course_detail)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar

        nextBtn = findViewById(R.id.nextButton)
        prevBtn = findViewById(R.id.prevButton)
        topicTxt = findViewById(R.id.courseTopic)
        topicContent = findViewById(R.id.topicContent)

        //from add course
        var courseTitle = intent.getStringExtra("courseTitle")
        var courseOverview = intent.getStringExtra("courseOverview")
        var courseColor = intent.getStringExtra("courseColor")
        courseImgUrl = intent.getStringExtra("courseImg").toUri()
        // end add course

        btnUpload = findViewById<Button>(R.id.btnUploadPhoto2)
        img = findViewById<ImageView>(R.id.imageView2)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Topic"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor(courseColor))

        readQuestion()



        finishButton.setOnClickListener{

            if (total == 0) {

                Toast.makeText(this,"You must add at least one topic.", Toast.LENGTH_SHORT).show()
            }

            else {

                val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

                builder.setTitle("Add Course")
                builder.setMessage("Confirm adding $courseTitle?")
                builder.setPositiveButton("Yes"){dialog, which ->

                    mStorageRef = FirebaseStorage.getInstance()
                    mDatabase = FirebaseDatabase.getInstance()

                    mDataBaseReference = mDatabase!!.reference!!.child("Course")
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("id").setValue(totalCourse.plus(1).toString())
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("title").setValue(courseTitle)
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("desc").setValue(courseOverview)
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("color").setValue(courseColor)


                    if (courseImgUrl != null) {

                        var fileReference : StorageReference = mStorageRef.reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(courseImgUrl))
                        var imgRef = mDataBaseReference.child(totalCourse.plus(1).toString())

                        val ref = fileReference
                        var a = fileReference.putFile(courseImgUrl)
                        val ab = a.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("avbc", task.result.toString())
                                imgRef.child("image").setValue(task.result.toString())
                            } else {
                                // Handle failures
                                // ...
                            }
                        }

                    }

                    mDatabase = FirebaseDatabase.getInstance()
                    var mDataBaseReference2 = mDatabase!!.reference!!.child("Course").child(totalCourse.plus(1).toString()).child("Topic")


                    var qCount = 1
                    topics.forEach {

                        mDataBaseReference2.child(qCount.toString()).child("content").setValue(topics[qCount-1].content)
                        mDataBaseReference2.child(qCount.toString()).child("name").setValue(topics[qCount-1].name)

                        if (topics[qCount-1].image != null) {

                            var fileReference2: StorageReference = mStorageRef.reference.child(
                                System.currentTimeMillis().toString() + "." + getFileExtension(
                                    topics[qCount - 1].image!!
                                )
                            )
                            var imgRef2 = mDataBaseReference2.child(qCount.toString())

                            val ref = fileReference2
                            var a = fileReference2.putFile(topics[qCount - 1].image!!)
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
                        }

                        else {

                            mDataBaseReference2.child(qCount.toString()).child("image").setValue(topics[qCount-1].name)

                        }
                        qCount++
                    }

                    val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
                    val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
                    mDataBaseReference = mDb.child("Course")
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("id").setValue(totalCourse.plus(1).toString())
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("title").setValue(courseTitle)
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("desc").setValue(courseOverview)
                    mDataBaseReference.child(totalCourse.plus(1).toString()).child("color").setValue(courseColor)


                    if (courseImgUrl != null) {

                        var fileReference : StorageReference = mStorageRef.reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(courseImgUrl))
                        var imgRef = mDataBaseReference.child(totalCourse.plus(1).toString())

                        val ref = fileReference
                        var a = fileReference.putFile(courseImgUrl)
                        val ab = a.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("avbc", task.result.toString())
                                imgRef.child("image").setValue(task.result.toString())
                            } else {
                                // Handle failures
                                // ...
                            }
                        }

                    }

                    mDatabase = FirebaseDatabase.getInstance()
                    mDataBaseReference2 = mDb.child("Course").child(totalCourse.plus(1).toString()).child("Topic")


                    qCount = 1
                    topics.forEach {

                        mDataBaseReference2.child(qCount.toString()).child("content").setValue(topics[qCount-1].content)
                        mDataBaseReference2.child(qCount.toString()).child("name").setValue(topics[qCount-1].name)

                        if (topics[qCount-1].image != null) {

                            var fileReference2: StorageReference = mStorageRef.reference.child(
                                System.currentTimeMillis().toString() + "." + getFileExtension(
                                    topics[qCount - 1].image!!
                                )
                            )
                            var imgRef2 = mDataBaseReference2.child(qCount.toString())

                            val ref = fileReference2
                            var a = fileReference2.putFile(topics[qCount - 1].image!!)
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
                        }

                        else {

                            mDataBaseReference2.child(qCount.toString()).child("image").setValue(topics[qCount-1].name)

                        }
                        qCount++
                    }

                    Toast.makeText(this,"Course added!", Toast.LENGTH_SHORT).show()

                    finish()
                    val intent = Intent(this, TutorMenu:: class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                }
                builder.setNeutralButton("Cancel"){_,_ ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        continueButton.setOnClickListener{

            if (count == topics.size) {

                if (topicTxt.text.toString() != "" && topicContent.text.toString() != "") {

                    var ans: String
                    val topic:Topic

                    if (img.drawable == null) {
                        topic = Topic(topicContent.text.toString(), topicTxt.text.toString(), null)
                    }

                    else {

                        topic = Topic(topicContent.text.toString(), topicTxt.text.toString(), topicImg)
                    }

                    topics.add(topic)
                    total++
                    count++

                    clearText()
                    topicNum.text = "Topic " + total.plus(1)
                    Snackbar.make(findViewById(R.id.drawerLayout), "Topic added. ", Snackbar.LENGTH_SHORT).show()
                    prevBtn.visibility = View.VISIBLE
                }

                else {

                    Toast.makeText(this,"Please fill in all details.", Toast.LENGTH_SHORT).show()
                }
            }

            else {

                if (topicTxt.text.toString() != "" && topicContent.text.toString() != "") {

                    val topic:Topic

                    if (img.drawable == null) {
                        topic = Topic(topicContent.text.toString(), topicTxt.text.toString(), null)
                    }

                    else {

                        topic = Topic(topicContent.text.toString(), topicTxt.text.toString(), topicImg)
                    }

                    topics.set(count, topic)
                    Snackbar.make(findViewById(R.id.drawerLayout), "Topic updated. ", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        nextBtn.setOnClickListener{

            if (count.plus(1) < topics.size) {

                count++
                setAllText(count)
                continueButton.text = "Update Topic"
            }

            else if (count.plus(1) == topics.size){
                count++
                clearText()
                topicNum.text = "Question " + total.plus(1)
                continueButton.text = "Add Topic"
            }
        }

        prevBtn.setOnClickListener{

            if (count.minus(1) >= 0) {

                count--
                setAllText(count)
                continueButton.text = "Update Topic"
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

            topicImg = data.getData()!!
            Picasso.with(this).load(topicImg).into(img)
        }

    }

    private fun getFileExtension (uri: Uri) : String? {

        var cR : ContentResolver = contentResolver
        var mime : MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun readQuestion() {

        mDatabase2 = FirebaseDatabase.getInstance()

        val mDatabaseRef2 = mDatabase2!!.reference!!.child("Course")
        val lastQuery = mDatabaseRef2.orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (data in p0.getChildren()) {

                    totalCourse = data.getKey()!!.toInt()
                }

            }
        })
    }

    private fun clearText() {

        topicTxt.setText("")
        topicContent.setText("")
        img.setImageResource(0)
    }

    private fun setAllText(counter : Int) {

        var displayTopic = topics[counter]
        topicTxt.setText(displayTopic.name)
        topicContent.setText(displayTopic.content)

        Picasso.with(this).load(displayTopic.image).into(img)

        topicNum.text = "Topic " + counter.plus(1)
    }

    override fun onBackPressed() {

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
