package com.example.test.classes

import android.net.Uri

data class Topic(
    var content: String? = "",
    var name: String? = "",
    var image : Uri? = null
)