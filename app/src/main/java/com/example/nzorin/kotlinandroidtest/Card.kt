package com.example.nzorin.kotlinandroidtest

import java.io.Serializable

class Card(var name: String?) : Serializable{
    var isAlive = true

    override fun toString(): String {
        return "$name ||"
    }
}
