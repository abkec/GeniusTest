package com.example.test.student.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Course Page"
    }
    val text: LiveData<String> = _text
}