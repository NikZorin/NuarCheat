package com.example.nzorin.kotlinandroidtest

object GameUtils {
    fun getFieldSize(playerNumber: Int): Int {
        var res: Int = 0;
        when (playerNumber) {
            3, 4 -> res = 5
            5, 6 -> res = 6
            8, 9 -> res = 7
        }
        return res;
    }
}