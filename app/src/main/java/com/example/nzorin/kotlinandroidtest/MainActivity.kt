package com.example.nzorin.kotlinandroidtest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var playerNum : Int = 0
    var playerList : ArrayList<String> = arrayListOf()
    var currentIndex : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val radioGroup = findViewById<RadioGroup>(R.id.playersNumberRadioGroup)
        val startButton = findViewById<Button>(R.id.newGameButton)

        radioGroup.setOnCheckedChangeListener { group, i ->
            when (i) {
                R.id.playersNum3 -> playerNum = 3
                R.id.playersNum4 -> playerNum = 4
                R.id.playersNum5 -> playerNum = 5
                R.id.playersNum6 -> playerNum = 6
                R.id.playersNum8 -> playerNum = 8
                R.id.playersNum9 -> playerNum = 9
            }
        }

        startButton.setOnClickListener {
            if (playerNum != 0) {
                createPlayers()
            }
        }
    }

    private fun createPlayers() {
        if (currentIndex < playerNum) {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Создание игроков")
            builder.setMessage("Введите имя ${currentIndex + 1}-го игрока")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") {
                dialogInterface, k -> playerList.add(editText.text.toString())
                currentIndex++
                createPlayers()
            }
            builder.show()
        } else {
            startNewGame()
        }
    }

    private fun startNewGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("players", playerList)
        startActivity(intent)
    }
}
