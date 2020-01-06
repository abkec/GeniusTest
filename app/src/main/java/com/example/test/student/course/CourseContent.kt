package com.example.test.student.course

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.test.R
import com.google.firebase.database.*

class CourseContent : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference
    lateinit var tvOverview: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)




        readCourse()








        /*Testing for button click to another page*/

        val btnBinary = findViewById<Button>(R.id.btnTopicOne)
        val btnStartCourse = findViewById<Button>(R.id.btnStartCourse)

        btnStartCourse.setOnClickListener {
            val intent = Intent(this, ComputerScience::class.java)
            startActivity(intent)
        }


        btnBinary.setOnClickListener {
            val intent = Intent(this, ComputerScience::class.java)
            startActivity(intent)
         }

    }

    private fun readCourse() {

        mDatabase = FirebaseDatabase.getInstance().getReference("Course")
        val mDatabaseRef = mDatabase.child(intent.getStringExtra("id"))
        tvOverview = findViewById(R.id.tv_overview_content)


            mDatabaseRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    tvOverview.text = p0.child("desc").value as String
                }
            }
        })

    }

    override fun onBackPressed() {


        super.onBackPressed()
        this.finish()
    }
}
