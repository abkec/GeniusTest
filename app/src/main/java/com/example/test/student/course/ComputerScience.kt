package com.example.test.student.course

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.test.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_computer_science.*

class ComputerScience : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_science)

        var color = intent.getStringExtra("color")
        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor(color))

        toolbar.title = intent.getStringExtra("title")
        setSupportActionBar(toolbar)


        readCourse()

    }

    private fun readCourse() {

        mDatabase = FirebaseDatabase.getInstance().getReference("Course")
        val mDatabaseRef = mDatabase.child(intent.getStringExtra("id2"))
        var count = 1

        mDatabaseRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    val childrenCount = p0.child("Topic").getChildrenCount().toInt()

                    Picasso.with(this@ComputerScience).load(p0.child("Topic").child(count.toString()).child("image").value.toString().toUri()).into(imageViewTopic)
                    textView_TopicTitle.text = p0.child("Topic").child(count.toString()).child("name").value as String
                    textView_TopicContent.text = p0.child("Topic").child(count.toString()).child("content").value as String

                    btnNextTopic.setOnClickListener {

                        // p0.getChildrenCount().toInt()

                        if (count ==  childrenCount) {

                            val intent = Intent(this@ComputerScience, FinishCourse::class.java)
                            startActivity(intent)
                        }

                        else {

                            count++
                            Picasso.with(this@ComputerScience).load(p0.child("Topic").child(count.toString()).child("image").value.toString().toUri()).into(imageViewTopic)
                            textView_TopicTitle.text = p0.child("Topic").child(count.toString()).child("name").value as String
                            textView_TopicContent.text = p0.child("Topic").child(count.toString()).child("content").value as String

                        }

                    }

                }
            }
        })

    }

    override fun onBackPressed() {

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {

        super.onBackPressed()
        return true
    }
}
