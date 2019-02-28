package com.example.nzorin.kotlinandroidtest.component

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.LinearLayout

class MoveButton(context: Context?, var fieldSize: Int, i: Int, j: Int) : Button(context) {
    init {
        layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT, 1.0f)
        textSize = 8f
        text = getMoveText(i, j)
    }

    private fun getMoveText(i: Int, j: Int): String {
        var res = String()
        when {
            j == 0 -> res = "˂"
            j == fieldSize + 1 -> res = "˃"
            i == 0 -> res = "˄"
            i == fieldSize + 1 -> res = "˅"
        }
        return res
    }
}