package com.example.test.student.quiz

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.classes.Quiz
import com.example.test.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.quiz_list_layout.view.*


class QuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel
    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        quizViewModel =
            ViewModelProviders.of(this).get(QuizViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        mRecylerView = view.findViewById(R.id.quizRecycler)
        mRecylerView.setLayoutManager(LinearLayoutManager(context))
        mDatabase = FirebaseDatabase.getInstance().getReference("Quiz")

        logRecyclerView()



        return view
    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Quiz, QuizViewHolder>(

            Quiz::class.java,
            R.layout.quiz_list_layout,
            QuizViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : QuizViewHolder?, model: Quiz?, position:Int) {
                viewHolder?.itemView?.quizButton?.text = model?.title
                viewHolder?.itemView?.quizID?.text = model?.id

            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter
    }



    class QuizViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        lateinit var mDatabase2 : DatabaseReference
        var totalQuestion = 0

        init {

            itemView!!.setOnClickListener{

                readQuestion(itemView.quizID.text.toString())

                val builder = AlertDialog.Builder(itemView.context, R.style.AlertDialogCustom)

                builder.setTitle("Quiz Time")
                builder.setMessage("Start " + itemView.quizButton.text +"?")
                builder.setPositiveButton("Yes"){dialog, which ->

                    val intent = Intent(itemView.context, QuizQuestion::class.java)
                    intent.putExtra("title", itemView.quizButton.text)
                    intent.putExtra("id", itemView.quizID.text)
                    intent.putExtra("count", totalQuestion.toString())
                    itemView.context.startActivity(intent)
                    Toast.makeText(itemView.context,"Start!", Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("Cancel"){_,_ ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()



            }
        }

        private fun readQuestion(id: String) {

            mDatabase2 = FirebaseDatabase.getInstance().getReference("QuizDetail")
            val mDatabaseRef2 = mDatabase2.child(id)

            mDatabaseRef2.addValueEventListener(object: ValueEventListener {
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