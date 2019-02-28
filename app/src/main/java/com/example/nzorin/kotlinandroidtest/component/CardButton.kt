package com.example.nzorin.kotlinandroidtest.component

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.LinearLayout

class CardButton(context: Context?) : Button(context) {
    init {
        layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT, 1.0f)
        textSize = 8f
    }
}