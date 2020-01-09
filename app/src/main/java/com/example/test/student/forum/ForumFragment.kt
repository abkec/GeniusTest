package com.example.test.student.forum

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.classes.Forum
import com.example.test.classes.Quiz
import com.example.test.student.quiz.QuizFragment
import com.example.test.student.quiz.QuizQuestion
import com.example.test.student.quiz.QuizViewModel
import com.example.test.tutor.forum.ForumContentTutor
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.forum_list_layout.view.*

class ForumFragment : Fragment() {

    lateinit var mRecylerView : RecyclerView
    lateinit var mRecylerView2 : RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabase2 : DatabaseReference
    private lateinit var forumViewModel: ForumViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forumViewModel =
            ViewModelProviders.of(this).get(ForumViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_forum, container, false)

        var add : Button = root.findViewById(R.id.btn_AddQuestion2)

        mRecylerView = root.findViewById(R.id.forumRecycler)
        mRecylerView.setLayoutManager(LinearLayoutManager(context))
        mDatabase = FirebaseDatabase.getInstance().getReference("Forum")

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
        mRecylerView2 = root.findViewById(R.id.myQuestions)
        mRecylerView2.setLayoutManager(LinearLayoutManager(context))
        mDatabase2 = mDb.child("Forum")

        logRecyclerView()


        add.setOnClickListener{

            val intent = Intent(context, ForumAdd::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Forum, ForumViewHolder>(

            Forum::class.java,
            R.layout.forum_list_layout,
            ForumViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : ForumViewHolder?, model: Forum?, position:Int) {
                viewHolder?.itemView?.tv_question?.text = model?.title
                viewHolder?.itemView?.forumID?.text = model?.id
                viewHolder?.itemView?.tv_questionAns2?.text = model?.total + "\nComments"
                viewHolder?.itemView?.tv_question2?.text = model?.question
                viewHolder?.itemView?.forumimage?.text = model?.image
            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter

        var FirebaseRecyclerAdapter2 = object : FirebaseRecyclerAdapter<Forum, ForumViewHolder2>(

            Forum::class.java,
            R.layout.forum2_list_layout,
            ForumViewHolder2::class.java,
            mDatabase2
        ) {

            override fun populateViewHolder(viewHolder2 : ForumViewHolder2?, model: Forum?, position:Int) {
                viewHolder2?.itemView?.tv_question?.text = model?.title
                viewHolder2?.itemView?.forumID?.text = model?.id
                viewHolder2?.itemView?.tv_questionAns2?.text = model?.total + "\nComments"
                viewHolder2?.itemView?.tv_question2?.text = model?.question
                viewHolder2?.itemView?.forumimage?.text = model?.image
            }
        }

        mRecylerView2.adapter = FirebaseRecyclerAdapter2

    }

    class ForumViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        init {

            itemView!!.setOnClickListener {

                val intent = Intent(itemView.context, ForumContent::class.java)
                intent.putExtra("id", itemView.forumID.text)
                intent.putExtra("image", itemView.forumimage.text)
                intent.putExtra("title", itemView.tv_question2.text)
                intent.putExtra("question", itemView.tv_question.text)
                itemView.context.startActivity(intent)

            }
        }
    }

    class ForumViewHolder2(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        init {

            itemView!!.setOnClickListener {

                val listItems = arrayOf("View forum question", "Delete forum question")

                val builder = androidx.appcompat.app.AlertDialog.Builder(itemView.context)

                builder.setTitle("What do you want to perform")

                builder.setSingleChoiceItems(listItems,-1){ dialog: DialogInterface?, i: Int ->

                    if(i == 0)
                    {
                        dialog?.cancel()
                        val intent = Intent(itemView.context, ForumContent::class.java)
                        intent.putExtra("id", itemView.forumID.text)
                        intent.putExtra("image", itemView.forumimage.text)
                        intent.putExtra("title", itemView.tv_question2.text)
                        intent.putExtra("question", itemView.tv_question.text)
                        itemView.context.startActivity(intent)
                    }

                    if(i == 1)
                    {
                        val mDatabase2 = FirebaseDatabase.getInstance().getReference("Forum")
                        val mDatabaseRef2 = mDatabase2.child(itemView?.forumID?.text.toString())
                        mDatabaseRef2.removeValue()


                        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
                        val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
                        val m= mDb.child("Forum").child(itemView?.forumID?.text.toString())
                        m.removeValue()

                        Toast.makeText(itemView.context,"Forum question deleted !", Toast.LENGTH_SHORT).show()
                        dialog?.cancel()
                    }


                }

                val dialog = builder.create()
                dialog.show()

            }
        }
    }

}