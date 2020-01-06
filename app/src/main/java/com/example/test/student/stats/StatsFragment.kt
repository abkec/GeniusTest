package com.example.test.student.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.test.R

class StatsFragment : Fragment() {

    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statsViewModel =
            ViewModelProviders.of(this).get(StatsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stats, container, false)
        val textView: TextView = root.findViewById(R.id.text_stats)
        statsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}