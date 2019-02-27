package com.example.nzorin.kotlinandroidtest

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import java.io.Serializable

class Card(var name: String?) : Serializable{
    var isAlive = true

    fun getInfo(): SpannableString {
        val spanString = SpannableString("$name ")
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, name?.length ?: 0, 0)
        return spanString
    }
}
