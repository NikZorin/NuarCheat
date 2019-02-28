package com.example.nzorin.kotlinandroidtest.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nzorin.kotlinandroidtest.entity.Game
import com.example.nzorin.kotlinandroidtest.R
import kotlinx.android.synthetic.main.fragment_players_info.view.*

private const val GAME = "GAME"

class PlayersInfoFragment : Fragment() {

    private lateinit var game: Game

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        game = arguments.getSerializable(GAME) as Game

        val root = inflater.inflate(R.layout.fragment_players_info, container, false)
        root.section_label.text = game.getPlayersInfo()
        return root
    }


}
