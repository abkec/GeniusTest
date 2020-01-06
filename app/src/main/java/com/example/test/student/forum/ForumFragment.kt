package com.example.test.student.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.test.R

class ForumFragment : Fragment() {

    private lateinit var forumViewModel: ForumViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forumViewModel =
            ViewModelProviders.of(this).get(ForumViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_forum, container, false)
        val textView: TextView = root.findViewById(R.id.text_forum)
        forumViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}