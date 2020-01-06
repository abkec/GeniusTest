package com.example.test.student.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Stats"
    }
    val text: LiveData<String> = _text
}