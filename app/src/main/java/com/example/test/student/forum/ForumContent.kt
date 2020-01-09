package com.example.test.student.forum

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.classes.Comments
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.comment_list_layout.view.*
import kotlinx.android.synthetic.main.forum_list_layout.*
import kotlinx.android.synthetic.main.quiz_list_layout.view.*

class ForumContent : AppCompatActivity() {

    lateinit var mDatabase : DatabaseReference
    var totalCom = 0
    lateinit var mRecylerView : RecyclerView
    lateinit var forumID :String
    lateinit var addCom : EditText
    lateinit var addComButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum2)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#6cd1f0"))

        forumID = intent.getStringExtra("id")
        addCom = findViewById(R.id.addComment)
        addComButton = findViewById(R.id.addComBut)

        Picasso.with(this).load(intent.getStringExtra("image").toUri()).into(forumImage)

        var question = findViewById<TextView>(R.id.question)
        var title = findViewById<TextView>(R.id.titleQuestion)
        title.text = intent.getStringExtra("title")
        question.text = intent.getStringExtra("question")
        mRecylerView = findViewById(R.id.commentRecycler)
        mRecylerView.setLayoutManager(LinearLayoutManager(this))
        var mD = FirebaseDatabase.getInstance().getReference("Forum").child(forumID)
        mDatabase = FirebaseDatabase.getInstance().getReference("Forum").child(forumID).child("comments")

        logRecyclerView()
        getTotalComment()

        addComButton.setOnClickListener{

            if (TextUtils.isEmpty(addCom.text.toString())) {
                Toast.makeText(this,"Please enter some text.", Toast.LENGTH_SHORT).show()
            }

            else {

                mDatabase.child(totalCom.plus(1).toString()).child("com").setValue(addCom.text.toString())
                mDatabase.child(totalCom.plus(1).toString()).child("count").setValue("0")
                mDatabase.child(totalCom.plus(1).toString()).child("id").setValue(totalCom.plus(1).toString())
                mD.child("total").setValue(totalCom.plus(1).toString())
                Toast.makeText(this,"Comment added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        finish()
    }

    private fun getTotalComment() {

        val mDatabaseRef2 = mDatabase
        val lastQuery = mDatabaseRef2.orderByKey().limitToLast(1)
        lastQuery.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (data in p0.getChildren()) {

                    totalCom = data.getKey()!!.toInt()
                }

            }
        })
    }

    private fun logRecyclerView() {

        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Comments, ForumViewHolder>(

            Comments::class.java,
            R.layout.comment_list_layout,
            ForumViewHolder::class.java,
            mDatabase
        ) {

            override fun populateViewHolder(viewHolder : ForumViewHolder?, model: Comments?, position:Int) {
                viewHolder?.itemView?.commentList?.text = model?.com
                viewHolder?.itemView?.commentID?.text = model?.id
                viewHolder?.itemView?.commentCount?.text = model?.count
                viewHolder?.itemView?.commentID2?.text = forumID
            }
        }

        mRecylerView.adapter = FirebaseRecyclerAdapter

    }



    class ForumViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        init {

            itemView!!.setOnClickListener {

                val listItems = arrayOf("This answer is useful", "This answer is not useful")

                val builder = AlertDialog.Builder(itemView.context)

                builder.setTitle("What do you want to perform")

                builder.setSingleChoiceItems(listItems,-1){ dialog: DialogInterface?, i: Int ->

                    if(i == 0)
                    {
                        val mDatabase2 = FirebaseDatabase.getInstance().getReference("Forum")
                        val mDatabaseRef2 = mDatabase2.child(itemView?.commentID2?.text.toString()).child("comments").child(itemView?.commentID?.text.toString())
                        mDatabaseRef2.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                    var count = p0.child("count").value.toString().toInt().plus(1)
                                    mDatabaseRef2.child("count").setValue((count).toString())
                            }
                        })

                        dialog?.cancel()
                    }

                    if(i == 1)
                    {
                        val mDatabase2 = FirebaseDatabase.getInstance().getReference("Forum")
                        val mDatabaseRef2 = mDatabase2.child(itemView?.commentID2?.text.toString()).child("comments").child(itemView?.commentID?.text.toString())
                        mDatabaseRef2.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {

                                var count = p0.child("count").value.toString().toInt().minus(1)
                                mDatabaseRef2.child("count").setValue((count).toString())
                            }
                        })

                        dialog?.cancel()
                    }

                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }

}
