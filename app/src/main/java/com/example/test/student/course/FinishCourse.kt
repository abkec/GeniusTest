package com.example.test.student.course

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.test.Login1
import com.example.test.R
import com.example.test.StudentMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FinishCourse : AppCompatActivity() {

    lateinit var mDatabase2: DatabaseReference
    var statCourse = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_course)

        readStats()

        val btnFinish = findViewById<Button>(R.id.btnFinishCourse)

        btnFinish.setOnClickListener {

            val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
            val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
            mDb.child("Stats").child("course").setValue(statCourse.plus(1).toString())

            finish()
            val intent = Intent(this, StudentMenu:: class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)


        }

    }

    private fun readStats() {

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
        mDatabase2 = mDb.child("Stats")
        val mDatabaseRef2 = mDatabase2

        mDatabaseRef2.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    statCourse = p0.child("course").value.toString().toInt()
                }
            }
        })
    }

}
