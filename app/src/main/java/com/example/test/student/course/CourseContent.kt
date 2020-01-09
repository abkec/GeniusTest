package com.example.test.student.course

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.test.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_course_content.*

class CourseContent : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference
    lateinit var tvOverview: TextView
    var idCourse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)

        var linear : ScrollView = findViewById(R.id.cl1)

        var status = intent.getStringExtra("status")

        if (status == "n")
            btnStartCourse.visibility = View.INVISIBLE

        var color = intent.getStringExtra("color")
        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.title = intent.getStringExtra("title")
        linear.setBackgroundColor(Color.parseColor(color))



        mDatabase = FirebaseDatabase.getInstance().getReference("Course")


        readCourse()


        /*Testing for button click to another page*/


        val btnStartCourse = findViewById<Button>(R.id.btnStartCourse)

        btnStartCourse.setOnClickListener {
            val intent = Intent(this, ComputerScience::class.java)
            intent.putExtra("id2", idCourse)
            intent.putExtra("color", color)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        finish()
    }

    private fun readCourse() {

        idCourse = intent.getStringExtra("id")
        val mDatabaseRef = mDatabase.child(idCourse)
        tvOverview = findViewById(R.id.tv_overview_content)


        mDatabaseRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {
                    Picasso.with(this@CourseContent).load(p0.child("image").value.toString().toUri()).into(imageView_course)
                    tvOverview.text = p0.child("desc").value as String

                    var count = 1
                    val builder = StringBuilder()

                    for (a in p0.child("Topic").children) {

                        builder.append(p0.child("Topic").child(count.toString()).child("name").value as String)
                        builder.append("\n")
                        count++
                    }

                    topicCovered.text = builder.toString()

                }
            }
        })

    }

}
