package com.example.nzorin.kotlinandroidtest


import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_field.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView


private const val GAME = "GAME"

class FieldFragment : Fragment() {

    private var game: Game? = null
    private var root: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        game = arguments.getSerializable(GAME) as Game?
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_field, container, false)
        drawField()

        return root
    }

    private fun getMoveText(i: Int, j: Int): String {
        var res = String()
        if (j == 0) {
            res = "˂"
        } else if (j == game!!.field.size + 1) {
            res = "˃"
        } else if (i == 0) {
            res = "˄"
        } else if (i == game!!.field.size + 1) {
            res = "˅"
        }
        return res
    }

    private fun drawField() {
        val turnText = root!!.turnTextView as TextView

        turnText.text = "Ход игрока ${game!!.currentPlayer!!.name}"

        val field = root!!.llContainer as LinearLayout

        for (i in 0 until game!!.field.size + 2) {
            val row = LinearLayout(root!!.context)
            row.gravity = Gravity.CENTER
            row.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1.0f)
            field.addView(row)

            for (j in 0 until game!!.field.size + 2) {

                if (i > 0 && i < game!!.field.size + 1 && j > 0 && j < game!!.field.size + 1) {
                    val btn = Button(this.context)
                    btn.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    btn.textSize = 8f
                    btn.setText(game!!.field.getCard(i - 1, j - 1).name)
                    if (!game!!.field.getCard(i - 1, j - 1).isAlive) {
                        btn.setBackgroundColor(Color.RED)
                    }
                    btn.setOnClickListener {
                        showChooseActionDialog(i, j)
                    }
                    row.addView(btn)
                } else if (i != j && i + j != game!!.field.size + 1) {
                    val btn = Button(root!!.context)
                    btn.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    btn.textSize = 20f
                    btn.setText(getMoveText(i, j))
                    btn.setOnClickListener {
                        moveField(i, j)
                        redrawField()
                    }
                    row.addView(btn)
                } else {
                    val btn = Button(this.context)
                    btn.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    btn.textSize = 8f
                    btn.setBackgroundColor(Color.TRANSPARENT)
                    btn.setText(" ")
                    row.addView(btn)
                }

            }
        }
    }

    private fun redrawField() {
        clearField()
        drawField()
    }

    private fun clearField() {
        val field = root!!.llContainer as LinearLayout
        field.removeAllViews()
    }

    private fun moveField(i: Int, j: Int) {
        if (j == 0) {
            game!!.move(Target.ROW, -1, i - 1)
        } else if (j == game!!.field.size + 1) {
            game!!.move(Target.ROW, 1, i - 1)
        } else if (i == 0) {
            game!!.move(Target.COLUMN, -1, j - 1)
        } else if (i == game!!.field.size + 1) {
            game!!.move(Target.COLUMN, 1, j - 1)
        }
    }

    private fun showChooseActionDialog(i: Int, j: Int) {
        val items = arrayOf("Допросить", "Убить")
        val builder = AlertDialog.Builder(this.context)
        with(builder) {
            setTitle("Выберите действие")
            setItems(items) { dialog, index ->
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
        val items = Array<String>(game!!.players.size) {
            index -> game!!.players[index].name!!
        }
        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this.context)

        with(builder) {
            setTitle("Кто попался на допросе")
            setMultiChoiceItems(items, null) { dialog, index, isChecked ->
                if (isChecked) {
                    selectedList.add(index)
                } else if (selectedList.contains(index)) {
                    selectedList.remove(Integer.valueOf(index))
                }
            }
            setPositiveButton("Готово") { dialogInterface, k ->
                val selected = arrayListOf<Player>()
                val notSelected = arrayListOf<Player>()
                for (index in 0 until game!!.players.size) {
                    if (selectedList.contains(index)) {
                        selected.add(game!!.players[index])
                    } else {
                        notSelected.add(game!!.players[index])
                    }
                }

                selected.remove(game!!.currentPlayer)
                notSelected.remove(game!!.currentPlayer)

                game!!.check(game!!.currentPlayer!!, i - 1, j - 1, selected, notSelected)
                redrawField()
            }

            show()
        }
    }

    private fun showKillDialog(i: Int, j: Int) {
        val items = Array<String>(game!!.players.size + 1) {
            index -> if(index == game!!.players.size) "Никто" else game!!.players[index].name!!
        }
        val builder = AlertDialog.Builder(this.context)
        with(builder) {
            setTitle("Кто умер")
            setItems(items) { dialog, index ->
                if (index == game!!.players.size) {
                    game!!.kill(game!!.currentPlayer!!, i - 1, j - 1, null)
                } else {
                    game!!.kill(game!!.currentPlayer!!, i - 1, j - 1, game!!.players[index])
                }
                redrawField()
            }
            show()
        }
    }


}
