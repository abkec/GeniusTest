package com.example.test.tutor.course
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_course.*

class AddCourse : AppCompatActivity() {

    //internal lateinit var sp :Spinner
    lateinit var mRecylerView : RecyclerView
    lateinit var mDatabase : DatabaseReference

    lateinit var imgurl : Uri
    lateinit var img : ImageView
    lateinit var btnUpload : Button
    var mainColor = "#f23863"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        mDatabase = FirebaseDatabase.getInstance().getReference("Course")

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Add Course"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor(mainColor))

        btnAdd.setOnClickListener {

            if (TextUtils.isEmpty(ti_courseName.text) || TextUtils.isEmpty(ti_overview.text) || !::imgurl.isInitialized) {

                Toast.makeText(this,"Please fill in all textbox and add one picture!", Toast.LENGTH_SHORT).show()
            }

            else {

                val myIntent = Intent(this, AddTopicDetail::class.java)
                myIntent.putExtra("courseTitle", ti_courseName.text.toString())
                myIntent.putExtra("courseOverview", ti_overview.text.toString())
                myIntent.putExtra("courseImg", imgurl.toString())
                myIntent.putExtra("courseColor", mainColor)
                startActivity(myIntent)
            }
        }

        purpleColor2.setOnClickListener{

            mainColor = "#f23bff"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        blueColor2.setOnClickListener{

            mainColor = "#4c8fed"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        greenColor2.setOnClickListener{

            mainColor = "#29e348"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        orangeColor2.setOnClickListener{

            mainColor = "#f2c42c"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        redColor2.setOnClickListener{

            mainColor = "#f23863"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        mintColor2.setOnClickListener{

            mainColor = "#3fe8be"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        miColor2.setOnClickListener{

            mainColor = "#ffcccc"
            toolbar.setBackgroundColor(Color.parseColor(mainColor))
        }

        btnUpload = findViewById<Button>(R.id.btnUploadPhoto)
        img = findViewById<ImageView>(R.id.imageView)

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

    override fun onBackPressed() {

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

}
