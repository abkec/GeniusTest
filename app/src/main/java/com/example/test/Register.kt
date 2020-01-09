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
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import java.util.regex.Pattern
import java.util.regex.Pattern.compile


private var studEmail: EditText? = null
private var studPassword: EditText? = null
private var studConfirmPassword: EditText? = null
private var studFirstName: EditText? = null
private var studLastName: EditText? = null
//private var studBirthdate: EditText? = null
private var crtAccButton: Button? = null
private var loginButton: Button? = null
//private var radiog : RadioGroup? = null
//private var radioUser: RadioButton? = null
//private var radioTutor: RadioButton? = null

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
//private var birthdate: String? = null
lateinit var radio : RadioButton
private var status : String = "T"



// Validation
private val emailRegex = compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)


private val passRegex = compile ("^(?=.*[0-9])(?=.*[a-z]).{8,}\$")



class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val button = findViewById<Button>(R.id.log_in)
        button.paintFlags = button.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        val button1 = findViewById<Button>(R.id.condition)
        button1.paintFlags = button1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

//        val button2 = findViewById<Button>(R.id.log_in)
//        button2.setOnClickListener{
//            val intent = Intent(this, Login1:: class.java)
//            startActivity(intent)
//        }


        user_selection.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {

            group, checkedId ->
            radio = findViewById(checkedId)

            if (radio.text == "Student")
                status = "S"
            else
                status = "T"


        })


        initialise()
    }

    private fun initialise()
    {
        studEmail = findViewById<View>(R.id.email_text_register) as EditText
        studPassword = findViewById<View>(R.id.pw_text_register) as EditText
        studConfirmPassword  = findViewById<View>(R.id.pw_confirm_register) as EditText
        studFirstName = findViewById<View>(R.id.first_name) as EditText
        studLastName = findViewById<View>(R.id.last_name) as EditText
        //studBirthdate = findViewById<View>(R.id.birth_date) as EditText
        crtAccButton = findViewById<View>(R.id.register_account_button) as Button
        loginButton = findViewById<View>(R.id.log_in) as Button

        mDatabase = FirebaseDatabase.getInstance()
        mDataBaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        crtAccButton!!.setOnClickListener { createNewAccount() }

        loginButton!!
            .setOnClickListener {
                finish()
                startActivity(Intent(this@Register,
                Login1::class.java)) }
    }

    private fun createNewAccount()
    {
        email = studEmail?.text.toString()
        password = studPassword?.text.toString()
        confirm_pass = studConfirmPassword?.text.toString()
        firstname = studFirstName?.text.toString()
        lastname = studLastName?.text.toString()
        //birthdate = studBirthdate?.text.toString()
        var check = 0


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
                confirm_pass))
        {
            if(!emailRegex.matcher(email).matches())
             {
                 Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_SHORT).show()

             }else {
                check++
            }
            if (!passRegex.matcher(password).matches()) {
                 Toast.makeText(this, "Password needs to be at least 8 characters including letters", Toast.LENGTH_SHORT).show()
             }else {
                check++
            }
            if (!password.equals(confirm_pass))
            {
                Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
            }else {
                check++
            }

            if (check ==3){
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
                            //currentUserDb.child("birthDate").setValue(birthdate)
                            currentUserDb.child("email").setValue(email)
                            //currentUserDb.child("password").setValue(password)
                            currentUserDb.child("address").setValue("")
                            currentUserDb.child("city").setValue("")
                            currentUserDb.child("state").setValue("")
                            currentUserDb.child("zip").setValue("")
                            currentUserDb.child("country").setValue("")
                            currentUserDb.child("status").setValue(status)

                            if (status == "S") {

                                currentUserDb.child("Stats").child("course").setValue("0")
                                currentUserDb.child("Stats").child("quiz").setValue("0")
                            }



                            //Update User and prompt to UI
                            updateUserInfoAndUI()
                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this@Register, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }else
        {
            Toast.makeText(this, "Missing Details. Please Enter All The Details", Toast.LENGTH_SHORT).show()
        }
    }



//    private fun createNewAccount()
//    {
//        email = studEmail?.text.toString()
//        password = studPassword?.text.toString()
//        confirm_pass = studConfirmPassword?.text.toString()
//        firstname = studFirstName?.text.toString()
//        lastname = studLastName?.text.toString()
//        birthdate = studBirthdate?.text.toString()
//
//
//      if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
//                confirm_pass) && !TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(
//              birthdate)) {
//
//          val dialog = setProgressDialog(this, "Registering User...")
//          dialog.show()
//
//          mAuth!!
//              .createUserWithEmailAndPassword(email!!, password!!)
//              .addOnCompleteListener(this) { task ->
//                  dialog.hide()
//
//
//                  if (task.isSuccessful) {
//                      // Sign in success, update UI with the signed-in user's information
//                      Log.d(TAG, "createUserWithEmail:success")
//
//                      val userId = mAuth!!.currentUser!!.uid
//
//                      //Verify email
//                      verifyEmail()
//
//                      //update user profile information
//                      val currentUserDb = mDataBaseReference!!.child(userId)
//                      currentUserDb.child("firstName").setValue(firstname)
//                      currentUserDb.child("lastName").setValue(lastname)
//                      currentUserDb.child("birthDate").setValue(birthdate)
//                      currentUserDb.child("email").setValue(email)
//                      //currentUserDb.child("password").setValue(password)
//                      currentUserDb.child("address").setValue("")
//                      currentUserDb.child("city").setValue("")
//                      currentUserDb.child("state").setValue("")
//                      currentUserDb.child("zip").setValue("")
//                      currentUserDb.child("country").setValue("")
//                      currentUserDb.child("status").setValue(status)
//
//
//                      //Update User and prompt to UI
//                      updateUserInfoAndUI()
//                  } else
//                  {
//                      // If sign in fails, display a message to the user.
//                      Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                      Toast.makeText(this@Register, "Authentication failed.",
//                          Toast.LENGTH_SHORT).show()
//                  }
//              }
//      }else
//          {
//              Toast.makeText(this, "Enter All The Details", Toast.LENGTH_SHORT).show()
//          }
//    }

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

            val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
            val mDb = FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
            var status: String

            mDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    status = snapshot.child("status").value.toString()

                    if (status == "S") {

                        val intent = Intent(this@Register, StudentMenu:: class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }

                    else  {
                        val intent = Intent(this@Register, TutorMenu:: class.java)
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
