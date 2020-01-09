package com.example.test

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_user_settings.*
import kotlinx.android.synthetic.main.activity_user__account.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.test.classes.User

private var mDatabase: FirebaseDatabase? = null
private var mDatabaseReference: DatabaseReference? = null
private var mAuth: FirebaseAuth? = null
private lateinit var auth: FirebaseAuth
private lateinit var status : String

class MainUserSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user_settings)

        initialise()
        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setBackgroundColor(Color.parseColor("#fcba03"))
        toolbar?.title = "Account Settings | Genius"

        val button = findViewById<Button>(R.id.chg_pic)
        button.paintFlags = button.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth!!.currentUser
        mDatabase = FirebaseDatabase.getInstance()
        val mReference = mDatabase!!.reference!!.child("Users").child(mUser!!.uid)
        auth = FirebaseAuth.getInstance()


//        mReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                status = snapshot.child("status").value.toString()
//
//                if (status == "S") {
//
//
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })



        val button2 = findViewById<Button>(R.id.personal_info)
        button2.setOnClickListener{

            mReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    status = snapshot.child("status").value.toString()

                    if (status == "S") {

                        val intent = Intent(this@MainUserSettings, PersonalUser:: class.java)
                        startActivity(intent)
                    }

                    else  {
                        val intent = Intent(this@MainUserSettings, PersonalTutor:: class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        }

        val button3 = findViewById<Button>(R.id.account_info)
        button3.setOnClickListener{

            mReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    status = snapshot.child("status").value.toString()

                    if (status == "S") {

                        val intent = Intent(this@MainUserSettings, UserAccount:: class.java)
                        startActivity(intent)
                    }

                    else  {
                        val intent = Intent(this@MainUserSettings, TutorAccount:: class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        }

        val button4 = findViewById<Button>(R.id.delete_button)

        button4.setOnClickListener{
            val builder = AlertDialog.Builder(this@MainUserSettings, R.style.AlertDialogCustom)
            builder.setTitle("Are you sure?")
            builder.setMessage("Deleting this account will result in completely removing your account from the system.")
            builder.setPositiveButton("DELETE"){ dialog, id ->
                deleteUser()
                Toast.makeText(this@MainUserSettings, "Deleted", Toast.LENGTH_SHORT).show()
            }
            //performing cancel action
            builder.setNegativeButton("CANCEL") {
                    dialog, id -> dialog.cancel()
                    Toast.makeText(this@MainUserSettings, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            builder.show()
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
    }

    private fun deleteUser() {
        val user = auth.currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val mUserReference = mDatabaseReference!!.child(user!!.uid)
                    mUserReference.removeValue()
                    Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    startActivity(Intent(this,Login1::class.java))
                    finish()
                }else
                {
                    Toast.makeText(this, "Delete Fail", Toast.LENGTH_SHORT).show()
                }
            }

    }




//    private fun deleteUser() {
//
//        val user = auth.currentUser
//
//        if(user !=null && user.email != null)
//        {
//            val credential = EmailAuthProvider
//                .getCredential(user.email!!)
//
//// Prompt the user to re-provide their sign-in credentials
//            user?.reauthenticate(credential)
//                ?.addOnCompleteListener {
//                    if(it.isSuccessful)
//                    {
//                        Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
//                        user?.delete()
//                            ?.addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show()
//                                    auth.signOut()
//                                    startActivity(Intent(this,Login1::class.java))
//                                    finish()
//                                }
//                            }
//
//                    }else
//                    {
//                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//
//    }
}
