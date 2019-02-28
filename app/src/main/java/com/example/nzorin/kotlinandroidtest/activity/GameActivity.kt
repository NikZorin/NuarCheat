package com.example.nzorin.kotlinandroidtest.activity

import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.content.Context
import android.support.v7.widget.ThemedSpinnerAdapter
import android.content.res.Resources.Theme
import com.example.nzorin.kotlinandroidtest.*
import com.example.nzorin.kotlinandroidtest.entity.Game
import com.example.nzorin.kotlinandroidtest.entity.Player
import com.example.nzorin.kotlinandroidtest.fragment.FieldFragment
import com.example.nzorin.kotlinandroidtest.fragment.PlayersInfoFragment
import com.example.nzorin.kotlinandroidtest.utils.GameUtils

import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.json.JSONObject
import java.io.IOException

private const val GAME = "GAME"
private const val PLAYERS = "PLAYERS"

class GameActivity : AppCompatActivity() {

    private var game: Game? = null
    private var playerList : ArrayList<Player> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_game)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        readNamesFromJson()

        val playerNamesNotCasted = intent.extras.get(PLAYERS) as ArrayList<*>
        val playerNames = playerNamesNotCasted.filterIsInstance<String>() as ArrayList<String>
        createPlayers(playerNames)

        game = savedInstanceState?.getSerializable(GAME) as Game?
        if (game == null) {
            game = Game(playerList, GameUtils.getFieldSize(playerList.size))
        }

        spinner.adapter = MyAdapter(
                toolbar.context,
                arrayOf(getString(R.string.game_field_label), getString(R.string.players_tab_label)))

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                val newFragment = if (position == 0) FieldFragment() else PlayersInfoFragment()
                val args = Bundle()
                args.putSerializable(GAME, game)
                newFragment.arguments = args

                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, newFragment)
                        .commit()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(GAME, game)
    }

    private fun readNamesFromJson() {
        names = arrayListOf()
        var json: String? = null
        try {
            val `is` = applicationContext.assets.open(getString(R.string.names_file_path))
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

    private class MyAdapter(context: Context, objects: Array<String>) : ArrayAdapter<String>(context, R.layout.list_item, objects), ThemedSpinnerAdapter {
        private val mDropDownHelper: ThemedSpinnerAdapter.Helper = ThemedSpinnerAdapter.Helper(context)

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View

            view = if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                val inflater = mDropDownHelper.dropDownViewInflater
                inflater.inflate(R.layout.list_item, parent, false)
            } else {
                convertView
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
        for(i in 0 until names.size) {
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

    companion object {
        lateinit var names: ArrayList<String>
    }

}
