package com.example.test.tutor.quiz


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.test.R
import kotlinx.android.synthetic.main.fragment_tutor_course.view.*
import kotlinx.android.synthetic.main.fragment_tutor_quiz.view.*

/**
 * A simple [Fragment] subclass.
 */
class TutorQuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tutor_quiz, container, false)

        view.btnAddQuiz.setOnClickListener {
            val intent = Intent(activity, AddQuiz ::class.java)
            startActivity(intent)
        }








//        view.imageBtnComputerScience.setOnClickListener {
//
//            val listItems = arrayOf("View Course", "Edit Course", "Delete Course")
//
//            val builder = AlertDialog.Builder(context)
//
//            builder.setTitle("What do you want to perform")
//
//            builder.setSingleChoiceItems(listItems,-1){dialog: DialogInterface?, i: Int ->
//
//                if(i == 0)
//                {
////                    val intent = Intent(activity, CourseContent::class.java)
////                    startActivity(intent)
//                }
//
//                if(i == 1)
//                {
////                    val intent = Intent(activity, EditCourse::class.java)
////                    startActivity(intent)
//                }
//
//                if(i == 2)
//                {
//                    val delBuilder = AlertDialog.Builder(context)
//                    delBuilder.setCancelable(true)
//
//                    delBuilder.setTitle("Delete Course")
//                    delBuilder.setMessage("Are you sure want to Delete ?")
//
//                    delBuilder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
//                        dialog.cancel()
//                    })
//
//                    delBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
//                        dialog.cancel()
//                    })
//
//                    val dialog = delBuilder.create()
//                    dialog.show()
//                }
//
//
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }

        return view
    }


}
