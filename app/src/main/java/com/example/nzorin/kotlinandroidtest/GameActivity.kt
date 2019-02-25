package com.example.nzorin.kotlinandroidtest

import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.content.Context
import android.support.v7.widget.ThemedSpinnerAdapter
import android.content.res.Resources.Theme

import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.fragment_players_info.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class GameActivity : AppCompatActivity() {

    private var game: Game? = null
    var playerList : ArrayList<Player> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val playerNames = intent.extras.get("players") as ArrayList<String>
        createPlayers(playerNames)

        game = Game(playerList, GameUtils.getFieldSize(playerList.size), this)

        // Setup spinner
        spinner.adapter = MyAdapter(
                toolbar.context,
                arrayOf("Игровое Поле", "Игроки"))

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                val newFragment = if (position == 0) FieldFragment() else PlayersInfoFragment()
                val args = Bundle()
                args.putSerializable("GAME", game)
                newFragment.arguments = args

                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private class MyAdapter(context: Context, objects: Array<String>) : ArrayAdapter<String>(context, R.layout.list_item, objects), ThemedSpinnerAdapter {
        private val mDropDownHelper: ThemedSpinnerAdapter.Helper = ThemedSpinnerAdapter.Helper(context)

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                val inflater = mDropDownHelper.dropDownViewInflater
                view = inflater.inflate(R.layout.list_item, parent, false)
            } else {
                view = convertView
            }

            view.text1.text = getItem(position)

            return view
        }

        override fun getDropDownViewTheme(): Theme? {
            return mDropDownHelper.dropDownViewTheme
        }

        override fun setDropDownViewTheme(theme: Theme?) {
            mDropDownHelper.dropDownViewTheme = theme
        }
    }

    private fun createPlayers(names: ArrayList<String>) {
        for(i in 0..names.size - 1) {
            createPlayer(names[i], i)
        }
        playerList[names.size - 1].nextPlayer = playerList[0]
    }

    private fun createPlayer(name: String, pos: Int) {
        val newPlayer = Player(name)
        if (playerList.size > 0) {
            playerList[pos - 1].nextPlayer = newPlayer
        }
        playerList.add(newPlayer)
    }
}
