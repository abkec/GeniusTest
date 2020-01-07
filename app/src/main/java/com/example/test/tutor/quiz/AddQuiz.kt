package com.example.test.tutor.quiz

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.TutorMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_quiz.*
import org.w3c.dom.Text
import java.net.URL

class AddQuiz : AppCompatActivity() {

    lateinit var imgurl : Uri
    lateinit var mStorageRef : StorageReference
    lateinit var img : ImageView
    lateinit var btnUpload : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quiz)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        var titleText = findViewById<EditText>(R.id.titleEditText)
        var quizText = findViewById<EditText>(R.id.quizEditText)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add new quiz"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#03fcfc"))

        continueButton.setOnClickListener{

            if (TextUtils.isEmpty(titleText.text) || TextUtils.isEmpty(quizText.text)) {

                Toast.makeText(this,"Please fill in all textbox!", Toast.LENGTH_SHORT).show()
            }

            else {

                val myIntent = Intent(this, AddQuizDetail::class.java)
                myIntent.putExtra("quizTitle", titleText.text.toString())
                myIntent.putExtra("quizTime", quizText.text.toString())
                myIntent.putExtra("quizImg", imgurl.toString())
                startActivity(myIntent)
            }
        }

        btnUpload = findViewById<Button>(R.id.btnUploadPhoto)
        img = findViewById<ImageView>(R.id.quizImage)

        btnUpload.setOnClickListener{

            filechooser()
        }

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

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
