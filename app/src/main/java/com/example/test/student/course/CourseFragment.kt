package com.example.test.student.course

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.classes.Course
import com.example.test.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.course_list_layout.view.*

class CourseFragment : Fragment() {

    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference
    private lateinit var courseViewModel: CourseViewModel


    lateinit var mDatabase2 : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseViewModel =
            ViewModelProviders.of(this).get(CourseViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_course, container, false)

        mRecylerView = view.findViewById(R.id.courseRecycler)
        mRecylerView.setLayoutManager(GridLayoutManager(context, 2))
        mDatabase = FirebaseDatabase.getInstance().getReference("Course")

        logRecyclerView()



        return view

    }


    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Course, CourseViewHolder>(

            Course::class.java,
            R.layout.course_list_layout,
            CourseViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : CourseViewHolder?, model: Course?, position:Int) {
                viewHolder?.itemView?.test?.text = model?.title
                viewHolder?.itemView?.courseID?.text = model?.id
                Picasso.with(context).load(model?.image?.toUri()).into(viewHolder?.itemView?.courseImg)
                viewHolder?.itemView?.courseID2?.text = model?.color
                viewHolder?.itemView?.test?.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model?.color)))
            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter
    }

    class CourseViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        init {

            itemView!!.setOnClickListener{

                val intent = Intent(itemView.context, CourseContent::class.java)
                intent.putExtra("title", itemView.test.text)
                intent.putExtra("id", itemView.courseID.text)
                intent.putExtra("color", itemView.courseID2.text)
                intent.putExtra("status","y")
                itemView.context.startActivity(intent)
            }
        }
    }

}