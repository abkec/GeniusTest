package com.example.test.tutor.quiz


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.test.R
import com.example.test.classes.Quiz
import com.example.test.student.quiz.QuizFragment
import com.example.test.student.quiz.QuizQuestion
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tutor_course.view.*
import kotlinx.android.synthetic.main.fragment_tutor_quiz.view.*
import kotlinx.android.synthetic.main.quiz_list_layout.view.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class TutorQuizFragment : Fragment() {

    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabase2 : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tutor_quiz, container, false)

        view.btnAddQuiz.setOnClickListener {
            val intent = Intent(activity, AddQuiz ::class.java)
            startActivity(intent)
        }

        mRecylerView = view.findViewById(R.id.tutorQuizRecycler)
        mRecylerView.setLayoutManager(LinearLayoutManager(context))
        mDatabase = FirebaseDatabase.getInstance().getReference("Quiz")

        logRecyclerView()

        return view
    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Quiz, TutorQuizViewHolder>(

            Quiz::class.java,
            R.layout.quiz_list_layout,
            TutorQuizViewHolder::class.java,
            mDatabase
        ) {

            @SuppressLint("Range")
            override fun populateViewHolder(viewHolder : TutorQuizViewHolder?, model: Quiz?, position:Int) {
                viewHolder?.itemView?.quizButton?.text = model?.title
                viewHolder?.itemView?.quizID?.text = model?.id
                Picasso.with(context).load(model?.image?.toUri()).into(viewHolder?.itemView?.quizImg)
                viewHolder?.itemView?.quizColor?.text = model?.color
                viewHolder?.itemView?.quizButton?.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(model?.color)))

            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter
    }

    class TutorQuizViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        lateinit var mDatabase2: DatabaseReference
        var totalQuestion = 0
        var time = ""

        init {

            itemView!!.setOnClickListener {

                val listItems = arrayOf("Edit Course", "Delete Course")

                val builder = AlertDialog.Builder(itemView.context, R.style.AlertDialogCustom)

                builder.setTitle("What do you want to perform")

                builder.setSingleChoiceItems(listItems,-1){dialog3: DialogInterface?, i: Int ->

                    if(i == 0)
                    {
                        dialog3?.cancel()
                        Toast.makeText(itemView.context,"Edit!", Toast.LENGTH_SHORT).show()
                    }

                    if(i == 1)
                    {
                        dialog3?.cancel()
                        val delBuilder = AlertDialog.Builder(itemView.context, R.style.AlertDialogCustom)
                        delBuilder.setCancelable(true)

                        delBuilder.setTitle("Delete Course")
                        delBuilder.setMessage("Are you sure want to Delete ?")

                        delBuilder.setNegativeButton("No", DialogInterface.OnClickListener { dialog2, i ->
                            dialog2.cancel()
                        })

                        delBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog2, i ->

                            mDatabase2 = FirebaseDatabase.getInstance().getReference("Quiz")
                            mDatabase2.child(itemView.quizID.text.toString()).removeValue()

                            mDatabase2 = FirebaseDatabase.getInstance().getReference("QuizDetail")
                            mDatabase2.child(itemView.quizID.text.toString()).removeValue()
                            dialog2.cancel()

                            Toast.makeText(itemView.context,itemView.quizButton.text.toString() + " deleted.", Toast.LENGTH_SHORT).show()
                        })

                        val dialog = delBuilder.create()
                        dialog.show()
                    }


                }

                val dialog = builder.create()
                dialog.show()


            }
        }

        private fun readQuestion(id: String) {

            mDatabase2 = FirebaseDatabase.getInstance().getReference("QuizDetail")
            val mDatabaseRef2 = mDatabase2.child(id)

            mDatabaseRef2.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {

                    totalQuestion = p0.childrenCount.toInt()
                }
            })
        }

    }


}
