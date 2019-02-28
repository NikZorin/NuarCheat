package com.example.nzorin.kotlinandroidtest.fragment


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_field.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.nzorin.kotlinandroidtest.*
import com.example.nzorin.kotlinandroidtest.entity.Target
import com.example.nzorin.kotlinandroidtest.component.CardButton
import com.example.nzorin.kotlinandroidtest.component.CornerButton
import com.example.nzorin.kotlinandroidtest.component.FieldRowLayout
import com.example.nzorin.kotlinandroidtest.component.MoveButton
import com.example.nzorin.kotlinandroidtest.entity.Game
import com.example.nzorin.kotlinandroidtest.entity.Player


private const val GAME = "GAME"

class FieldFragment : Fragment() {

    private lateinit var game: Game
    private var root: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        game = arguments.getSerializable(GAME) as Game
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_field, container, false)
        drawField()

        return root
    }

    private fun drawField() {
        drawTurnText()
        drawFieldLayout()
    }

    private fun drawFieldLayout() {
        val field = root?.llContainer as LinearLayout
        for (i in 0 until game.field.size + 2) {
            val row = FieldRowLayout(root?.context)
            for (j in 0 until game.field.size + 2) {
                val button = when {
                    isCardButton(i, j) -> {
                        buildCardButton(i, j)
                    }
                    isMoveButton(i, j) -> {
                        buildMoveButton(i, j)
                    }
                    else -> {
                        CornerButton(this.context)
                    }
                }
                row.addView(button)
            }
            field.addView(row)
        }
    }

    private fun drawTurnText() {
        val turnText = root?.turnTextView as TextView
        turnText.text = getString(R.string.player_turn_label, game.currentPlayer.name)
    }

    private fun buildMoveButton(i: Int, j: Int): Button {
        val btn = MoveButton(this.context, game.field.size, i, j)
        btn.setOnClickListener {
            moveField(i, j)
            redrawField()
        }
        return btn
    }

    private fun buildCardButton(i: Int, j: Int): Button {
        val btn = CardButton(this.context)
        btn.text = game.field.getCard(i - 1, j - 1).name
        if (!game.field.getCard(i - 1, j - 1).isAlive) {
            btn.setBackgroundColor(Color.RED)
        }
        btn.setOnClickListener {
            showChooseActionDialog(i, j)
        }
        return btn
    }

    private fun isMoveButton(i: Int, j: Int) = i != j && i + j != game.field.size + 1

    private fun isCardButton(i: Int, j: Int) =
            i > 0 && i < game.field.size + 1 && j > 0 && j < game.field.size + 1

    private fun redrawField() {
        clearField()
        drawField()
    }

    private fun clearField() {
        val field = root!!.llContainer as LinearLayout
        field.removeAllViews()
    }

    private fun moveField(i: Int, j: Int) {
        when {
            j == 0 -> game.move(Target.ROW, -1, i - 1)
            j == game.field.size + 1 -> game.move(Target.ROW, 1, i - 1)
            i == 0 -> game.move(Target.COLUMN, -1, j - 1)
            i == game.field.size + 1 -> game.move(Target.COLUMN, 1, j - 1)
        }
    }

    private fun showChooseActionDialog(i: Int, j: Int) {
        val items = arrayOf(getString(R.string.check_label), getString(R.string.kill_label))
        val builder = AlertDialog.Builder(this.context)
        with(builder) {
            setTitle(getString(R.string.choose_action_label))
            setItems(items) { _, index ->
                if (index == 0) {
                    showInterrogateDialog(i, j)
                } else {
                    showKillDialog(i, j)
                }
            }
            show()
        }
    }

    private fun showInterrogateDialog(i: Int, j: Int) {
        val items = Array(game.players.size) { index ->
            game.players[index].name
        }
        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this.context)

        with(builder) {
            setTitle(getString(R.string.caught_label))
            setMultiChoiceItems(items, null) { _, index, isChecked ->
                if (isChecked) {
                    selectedList.add(index)
                } else if (selectedList.contains(index)) {
                    selectedList.remove(Integer.valueOf(index))
                }
            }
            setPositiveButton(getString(R.string.done_label)) { _, _ ->
                val selected = arrayListOf<Player>()
                val notSelected = arrayListOf<Player>()
                for (index in 0 until game.players.size) {
                    if (selectedList.contains(index)) {
                        selected.add(game.players[index])
                    } else {
                        notSelected.add(game.players[index])
                    }
                }

                selected.remove(game.currentPlayer)
                notSelected.remove(game.currentPlayer)

                game.check(game.currentPlayer, i - 1, j - 1, selected, notSelected)
                redrawField()
            }

            show()
        }
    }

    private fun showKillDialog(i: Int, j: Int) {
        val items = Array<String>(game.players.size + 1) { index ->
            if (index == game.players.size) getString(R.string.nobody_label) else game.players[index].name
        }
        val builder = AlertDialog.Builder(this.context)
        with(builder) {
            setTitle(getString(R.string.died_label))
            setItems(items) { _, index ->
                if (index == game.players.size) {
                    game.kill(game.currentPlayer, i - 1, j - 1, null)
                } else {
                    game.kill(game.currentPlayer, i - 1, j - 1, game.players[index])
                }
                redrawField()
            }
            show()
        }
    }


}
