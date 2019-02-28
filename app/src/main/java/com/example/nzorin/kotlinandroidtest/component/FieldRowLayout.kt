package com.example.nzorin.kotlinandroidtest.component

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.widget.LinearLayout

class FieldRowLayout(context: Context?) : LinearLayout(context) {
    init {
        gravity = Gravity.CENTER
        layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT, 1.0f)
    }
}