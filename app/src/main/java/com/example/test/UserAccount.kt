package com.example.test

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user__account.*
import java.util.regex.Pattern

private val TAG = "UserAccount"

//Firebase Reference
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

private lateinit var auth:FirebaseAuth

private val passRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{8,}\$")

//UI elements
private var textEmail: TextView? = null

class UserAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user__account)


        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#FFEA00"))
        toolbar?.title = "Account Info"

        initialise()

        button_acc.setOnClickListener {
            changePassword()
        }

    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        finish()
    }

    private fun initialise() {

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()

        textEmail = findViewById<View>(R.id.current_email_user1) as TextView
    }

    private fun changePassword() {

        var count = 0
        if (current_password.text.isNotEmpty() && new_password.text.isNotEmpty() && new_password_confirm.text.isNotEmpty()) {

            if (!passRegex.matcher(new_password.text).matches() || !passRegex.matcher(new_password_confirm.text).matches()) {
                Toast.makeText(this, "Password needs to be at least 8 characters including letters", Toast.LENGTH_SHORT).show()
            }else {
                count++
            }

            if(new_password.text.toString(). equals (new_password_confirm.text.toString()))
            {
                count++

            }else
            {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
            }

            if (count == 2) {

                val user = auth.currentUser
                if(user != null && user.email !=null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, current_password.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT).show()
                                user?.updatePassword(new_password.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password changed.", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            val intent = Intent(this, Login1:: class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)

                                        }
                                    }

                            }else
                            {
                                Toast.makeText(this, "Re-authentication failed. Current Password is incorrect.", Toast.LENGTH_SHORT).show()
                            }
                        }

                }else
                {
                    startActivity(Intent(this,Login1::class.java))
                    finish()
                }
            }


        }else
        {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart(){
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textEmail!!.text = snapshot.child("email").value as String

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }





}
