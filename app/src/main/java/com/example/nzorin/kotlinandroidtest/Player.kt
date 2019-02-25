package com.example.nzorin.kotlinandroidtest

import java.io.Serializable
import java.util.ArrayList

class Player(var name: String?) : Serializable {
    var possibleCards: List<Card> = ArrayList()
    var notPossibleCards: List<Card> = ArrayList()
    var nextPlayer: Player? = null

    fun getInfo(): String {
        val builder = StringBuilder()
        builder.append("$name:\n")
        for (card in MergeUtils.cutLists(possibleCards, notPossibleCards)) {
            builder.append("${card.name} | ")
        }
        return builder.toString()
    }
}
