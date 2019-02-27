package com.example.nzorin.kotlinandroidtest

import android.text.SpannableStringBuilder
import java.io.Serializable

class Game(val players: List<Player>, fieldSize: Int) : Serializable {
    val field: Field
    var currentPlayer: Player? = null

    init {
        this.currentPlayer = players[0]
        this.field = Field(fieldSize)
    }

    fun move(target: Target, direction: Int, index: Int) {
        field.move(target, direction, index)
        changeTurn()
    }

    fun check(actor: Player, posX: Int, posY: Int, found: List<Player>, notFound: List<Player>) {
        val newCards = field.getSurrounding(posX, posY)
        actor.possibleCards = MergeUtils.mergeLists(actor.possibleCards, newCards)
        val enemyList = arrayListOf<Card>()
        enemyList.addAll(newCards)
        enemyList.add(field.getCard(posX, posY))
        for (f in found) {
            f.possibleCards = MergeUtils.mergeLists(f.possibleCards, enemyList)
        }
        for (n in notFound) {
            n.notPossibleCards = MergeUtils.unionLists(n.notPossibleCards, enemyList)
        }
        changeTurn()
    }

    fun kill(actor: Player, posX: Int, posY: Int, killed: Player?) {
        val newCards = field.getSurrounding(posX, posY)
        actor.possibleCards = MergeUtils.mergeLists(actor.possibleCards, newCards)

        for(player in players) {
            player.notPossibleCards = MergeUtils.unionLists(player.notPossibleCards, arrayListOf(field.getCard(posX, posY)))
        }

        if (killed != null) {
            killed.possibleCards = arrayListOf()
            killed.notPossibleCards = arrayListOf()
            field.getCard(posX, posY).isAlive = false
        }
        changeTurn()
    }

    fun getPlayersInfo() : SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        for(p in players) {
            builder.append(p.getInfo())
            builder.append("\n")
        }
        return builder
    }

    private fun changeTurn() {
        currentPlayer = currentPlayer!!.nextPlayer;
    }
}
