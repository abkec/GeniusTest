package com.example.test

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login1.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


private val TAG = "Login1"

//global variables
private var email: String? = null
private var password: String? = null

//UI elements

private var studEmail: EditText? = null
private var studPassword: EditText? = null
private var btnLogin: Button? = null
private var btnCreateAccount: Button? = null


//Firebase references
private var mAuth: FirebaseAuth? = null

class Login1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

        val button = findViewById<Button>(R.id.forgot_password)
        button.paintFlags = button.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button1 = findViewById<Button>(R.id.register2_button)
        button1.paintFlags = button1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button2 = findViewById<Button>(R.id.forgot_password)
        button2.setOnClickListener{

            val intent = Intent(this, Forgot_Password:: class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.register2_button)
        button3.setOnClickListener{

            finish()
            val intent = Intent(this, Register:: class.java)
            startActivity(intent)
        }

        initialise()
    }

    private fun initialise() {

        studEmail = findViewById<View>(R.id.email_text_login) as? EditText
        studPassword = findViewById<View>(R.id.pw_text) as? EditText
        btnLogin = findViewById<View>(R.id.login_button) as? Button

        mAuth = FirebaseAuth.getInstance()

        btnLogin!!.setOnClickListener { loginUser() }
    }

    private fun loginUser() {

        email = studEmail?.text.toString()
        password = studPassword?.text.toString()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            val dialog = setProgressDialog(this, "Logging In...")
            dialog.show()

            mAuth!!
                .signInWithEmailAndPassword(email!!,password!!)
                .addOnCompleteListener(this)
                {
                        task -> dialog.hide()

                    if(task.isSuccessful)
                    {
                        Log.d(TAG,"signInWithEmail:Success")
                        updateUI()
                    }else
                    {
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@Login1, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

                }
        }else
        {
            Toast.makeText(this, "Invalid Email/Password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {

        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
        var status: String

        mDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                status = snapshot.child("status").value.toString()

                if (status == "S") {

                    val intent = Intent(this@Login1, StudentMenu:: class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }

                else {
                    val intent = Intent(this@Login1, TutorMenu:: class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }


    //Alternative Progress Dialog
    fun setProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    override fun onBackPressed() {

        finish()
    }
}
