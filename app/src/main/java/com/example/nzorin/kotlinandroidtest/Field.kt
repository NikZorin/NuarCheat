package com.example.nzorin.kotlinandroidtest

import android.content.Context
import java.io.IOException
import java.io.Serializable
import kotlin.collections.ArrayList
import org.json.JSONObject



class Field(val size: Int, val context: Context) : Serializable {
    var cards: Array<Array<Card?>>? = null
        private set
    var names: ArrayList<String> = arrayListOf()

    init {
        readNamesFromJson()
        initCards()
    }

    fun move(target: Target, direction: Int, index: Int) {
        if (target == Target.COLUMN) {
            moveColumn(direction, index)
        } else {
            moveRow(direction, index)
        }
    }

    private fun readNamesFromJson() {
        var json: String? = null
        try {
            val `is` = context.getAssets().open("names.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        val obj = JSONObject(json)
        names.addAll(obj.getString("names").split(","))
    }

    private fun moveColumn(direction: Int, index: Int) {
        if (direction > 0) {
            moveDown(index)
        } else {
            moveUp(index)
        }
    }

    private fun moveRow(direction: Int, index: Int) {
        if (direction > 0) {
            moveRight(index)
        } else {
            moveLeft(index)
        }
    }

    private fun moveDown(index: Int) {
        val temp = cards!![size - 1][index]
        for (i in size - 1 downTo 1) {
            cards!![i][index] = cards!![i - 1][index]
        }
        cards!![0][index] = temp
    }

    private fun moveUp(index: Int) {
        val temp = cards!![0][index]
        for (i in 0 until size - 1) {
            cards!![i][index] = cards!![i + 1][index]
        }
        cards!![size - 1][index] = temp
    }

    private fun moveRight(index: Int) {
        val temp = cards!![index][size - 1]
        for (i in size - 1 downTo 1) {
            cards!![index][i] = cards!![index][i - 1]
        }
        cards!![index][0] = temp
    }

    public fun getCard(x: Int, y: Int): Card {
        return cards!![x][y]!!;
    }

    public fun getSurrounding(x: Int, y: Int): ArrayList<Card> {
        val surrounding = arrayListOf<Card>();
        if (x > 0 && cards!![x - 1][y]!!.isAlive) {
            surrounding.add(cards!![x - 1][y]!!)
        }
        if (y > 0 && cards!![x][y - 1]!!.isAlive) {
            surrounding.add(cards!![x][y - 1]!!)
        }
        if (x < size - 1 && cards!![x + 1][y]!!.isAlive) {
            surrounding.add(cards!![x + 1][y]!!)
        }
        if (y < size - 1 && cards!![x][y + 1]!!.isAlive) {
            surrounding.add(cards!![x][y + 1]!!)
        }
        if (x > 0 && y > 0 && cards!![x - 1][y - 1]!!.isAlive) {
            surrounding.add(cards!![x - 1][y - 1]!!)
        }
        if (x < size - 1 && y < size - 1 && cards!![x + 1][y + 1]!!.isAlive) {
            surrounding.add(cards!![x + 1][y + 1]!!)
        }
        if (x > 0 && y < size - 1 && cards!![x - 1][y + 1]!!.isAlive) {
            surrounding.add(cards!![x - 1][y + 1]!!)
        }
        if (x < size - 1 && y > 0 && cards!![x + 1][y - 1]!!.isAlive) {
            surrounding.add(cards!![x + 1][y - 1]!!)
        }
        return surrounding;
    }

    private fun moveLeft(index: Int) {
        val temp = cards!![index][0]
        for (i in 0 until size - 1) {
            cards!![index][i] = cards!![index][i + 1]
        }
        cards!![index][size - 1] = temp
    }

    private fun initCards() {
        cards = Array<Array<Card?>>(size) { arrayOfNulls<Card>(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                cards!![i][j] = Card(names[i * size + j])
            }
        }
    }
}
