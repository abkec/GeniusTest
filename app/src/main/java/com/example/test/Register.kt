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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private var studEmail: EditText? = null
private var studPassword: EditText? = null
private var studConfirmPassword: EditText? = null
private var studFirstName: EditText? = null
private var studLastName: EditText? = null
private var studBirthdate: EditText? = null
private var crtAccButton: Button? = null

//Fire base Reference
private var mDataBaseReference:DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

private val TAG = "Register"

//Global Variable
private var email: String? = null
private var password: String? = null
private var confirm_pass: String? = null
private var firstname: String? = null
private var lastname: String? = null
private var birthdate: String? = null


class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val button = findViewById<Button>(R.id.log_in)
        button.paintFlags = button.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button1 = findViewById<Button>(R.id.condition)
        button1.paintFlags = button1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button2 = findViewById<Button>(R.id.log_in)
        button2.setOnClickListener{
            val intent = Intent(this, Login1:: class.java)
            startActivity(intent)
        }

        initialise()
    }

    private fun initialise()
    {
        studEmail = findViewById<View>(R.id.email_text_register) as EditText
        studPassword = findViewById<View>(R.id.pw_text_register) as EditText
        studConfirmPassword  = findViewById<View>(R.id.pw_confirm_register) as EditText
        studFirstName = findViewById<View>(R.id.first_name) as EditText
        studLastName = findViewById<View>(R.id.last_name) as EditText
        studBirthdate = findViewById<View>(R.id.birth_date) as EditText
        crtAccButton = findViewById<View>(R.id.register_account_button) as Button

        mDatabase = FirebaseDatabase.getInstance()
        mDataBaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        crtAccButton!!.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount()
    {
        email = studEmail?.text.toString()
        password = studPassword?.text.toString()
        confirm_pass = studConfirmPassword?.text.toString()
        firstname = studFirstName?.text.toString()
        lastname = studLastName?.text.toString()
        birthdate = studBirthdate?.text.toString()

      if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
                confirm_pass) && !TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(
              birthdate)) {

          val dialog = setProgressDialog(this, "Registering User...")
          dialog.show()

          mAuth!!
              .createUserWithEmailAndPassword(email!!, password!!)
              .addOnCompleteListener(this) { task ->
                  dialog.hide()

                  if (task.isSuccessful) {
                      // Sign in success, update UI with the signed-in user's information
                      Log.d(TAG, "createUserWithEmail:success")

                      val userId = mAuth!!.currentUser!!.uid

                      //Verify email
                      verifyEmail()

                      //update user profile information
                      val currentUserDb = mDataBaseReference!!.child(userId)
                      currentUserDb.child("firstName").setValue(firstname)
                      currentUserDb.child("lastName").setValue(lastname)
                      currentUserDb.child("birthDate").setValue(birthdate)


                      //
                      updateUserInfoAndUI()
                  } else
                  {
                      // If sign in fails, display a message to the user.
                      Log.w(TAG, "createUserWithEmail:failure", task.exception)
                      Toast.makeText(this@Register, "Authentication failed.",
                          Toast.LENGTH_SHORT).show()
                  }
              }
      }else
          {
              Toast.makeText(this, "Enter All The Details", Toast.LENGTH_SHORT).show()
          }
    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Register,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this@Register,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


        private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@Register, StudentMenu::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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
}
