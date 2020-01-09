package com.example.test.student.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_stats.*
import org.w3c.dom.Text

class StatsFragment : Fragment() {

    private lateinit var statsViewModel: StatsViewModel
    lateinit var mDatabase2: DatabaseReference
    var statCourse = 0
    var statQuiz = 0
    lateinit var c : TextView
    lateinit var q : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statsViewModel =
            ViewModelProviders.of(this).get(StatsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stats, container, false)
        //val textView: TextView = root.findViewById(R.id.text_stats)
        statsViewModel.text.observe(this, Observer {
            //textView.text = it
        })

        c = root.findViewById<TextView>(R.id.text_course_finish)
        q = root.findViewById<TextView>(R.id.text_quiz_finish)


        readStats()
        return root
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

                statCourse = p0.child("course").value.toString().toInt()
                statQuiz = p0.child("quiz").value.toString().toInt()

                c.text = "Courses Finished : " + statCourse.toString()
                q.text = "Quizzes Finished : " + statQuiz.toString()
            }
        })
    }
}