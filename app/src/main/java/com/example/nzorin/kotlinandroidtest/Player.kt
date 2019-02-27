package com.example.nzorin.kotlinandroidtest

import android.text.SpannableStringBuilder
import java.io.Serializable
import java.util.ArrayList

class Player(var name: String?) : Serializable {
    var possibleCards: List<Card> = ArrayList()
    var notPossibleCards: List<Card> = ArrayList()
    var nextPlayer: Player? = null

    fun getInfo(): SpannableStringBuilder {
        val possibleList = MergeUtils.cutLists(possibleCards, notPossibleCards)
        val builder = SpannableStringBuilder()
        builder.append("$name(${possibleList.size}):\n")
        for (card in possibleList) {
            builder.append(card.getInfo())
        }
        builder.append("\n")
        return builder
    }
}
