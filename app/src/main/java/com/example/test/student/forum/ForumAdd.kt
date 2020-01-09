package com.example.test.student.forum

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ForumAdd : AppCompatActivity() {

    lateinit var imgurl : Uri
    lateinit var mStorageRef : FirebaseStorage
    lateinit var img : ImageView
    lateinit var btnUpload : Button
    lateinit var mDatabase : DatabaseReference
    lateinit var title : EditText
    lateinit var body : EditText
    lateinit var submitBtn : Button
    lateinit var cancelBtn : Button
    var totalForum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_question)

        readForum()
        title = findViewById(R.id.question_text)
        body = findViewById(R.id.question_text2)
        submitBtn = findViewById(R.id.submit)
        cancelBtn = findViewById(R.id.cancel)

        mDatabase = FirebaseDatabase.getInstance().getReference("Forum")
        mStorageRef = FirebaseStorage.getInstance()

        submitBtn.setOnClickListener{

            if (TextUtils.isEmpty(title.text.toString()) || TextUtils.isEmpty(body.text.toString()) ) {
                Toast.makeText(this,"Please fill in your question details.", Toast.LENGTH_SHORT).show()
            }

            else {

                if (!::imgurl.isInitialized)
                    mDatabase.child(totalForum.plus(1).toString()).child("image").setValue("")
                else {

                    var fileReference : StorageReference = mStorageRef.reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imgurl))
                    var imgRef = mDatabase.child(totalForum.plus(1).toString())

                    val ref = fileReference
                    var a = fileReference.putFile(imgurl)
                    val ab = a.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            imgRef.child("image").setValue(task.result.toString())
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                }

                mDatabase.child(totalForum.plus(1).toString()).child("title").setValue(title.text.toString())
                mDatabase.child(totalForum.plus(1).toString()).child("question").setValue(body.text.toString())
                mDatabase.child(totalForum.plus(1).toString()).child("total").setValue("0")
                mDatabase.child(totalForum.plus(1).toString()).child("id").setValue(totalForum.plus(1).toString())

                val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
                val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
                mDatabase = mDb.child("Forum")
                mDatabase.child(totalForum.plus(1).toString()).child("title").setValue(title.text.toString())
                mDatabase.child(totalForum.plus(1).toString()).child("question").setValue(body.text.toString())
                mDatabase.child(totalForum.plus(1).toString()).child("total").setValue("0")
                mDatabase.child(totalForum.plus(1).toString()).child("id").setValue(totalForum.plus(1).toString())

                Toast.makeText(this,"Forum question added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        cancelBtn.setOnClickListener{

            Toast.makeText(this,"Forum question cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnUpload = findViewById<Button>(R.id.upl)
        img = findViewById<ImageView>(R.id.forumImg)

        btnUpload.setOnClickListener{

            filechooser()
        }
    }

    private fun getFileExtension (uri: Uri) : String? {

        var cR : ContentResolver = contentResolver
        var mime : MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun filechooser() {

        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {

            imgurl = data.getData()!!
            Picasso.with(this).load(imgurl).into(img)
        }

    }

    private fun readForum() {

        val mDatabase2 = FirebaseDatabase.getInstance()
        val mDataBaseReference2 = mDatabase2!!.reference!!.child("Forum")

        val lastQuery = mDataBaseReference2.orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (data in p0.getChildren()) {

                    totalForum = data.getKey()!!.toInt()
                }

            }
        })
    }
}
