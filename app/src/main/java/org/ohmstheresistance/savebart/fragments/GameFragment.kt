package org.ohmstheresistance.savebart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.GameFragmentBinding

class GameFragment : Fragment(), View.OnClickListener {

    lateinit var gameFragmentBinding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gameFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        initializeButtons()

        return gameFragmentBinding.root
    }

    private fun initializeButtons(){
        gameFragmentBinding.zeroButton.setOnClickListener(this)
        gameFragmentBinding.oneButton.setOnClickListener(this)
        gameFragmentBinding.twoButton.setOnClickListener(this)
        gameFragmentBinding.threeButton.setOnClickListener(this)
        gameFragmentBinding.fourButton.setOnClickListener(this)
        gameFragmentBinding.fiveButton.setOnClickListener(this)
        gameFragmentBinding.sixButton.setOnClickListener(this)
        gameFragmentBinding.sevenButton.setOnClickListener(this)
        gameFragmentBinding.hintButton.setOnClickListener(this)
        gameFragmentBinding.revealButton.setOnClickListener(this)
        gameFragmentBinding.resetButton.setOnClickListener(this)
        gameFragmentBinding.guessButton.setOnClickListener(this)
        gameFragmentBinding.deleteButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when (view?.id){

            gameFragmentBinding.zeroButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.oneButton.id -> gameFragmentBinding.userGuessEdittext.append("1")
            gameFragmentBinding.twoButton.id -> gameFragmentBinding.userGuessEdittext.append("2")
            gameFragmentBinding.threeButton.id -> gameFragmentBinding.userGuessEdittext.append("3")
            gameFragmentBinding.fourButton.id -> gameFragmentBinding.userGuessEdittext.append("4")
            gameFragmentBinding.fiveButton.id -> gameFragmentBinding.userGuessEdittext.append("5")
            gameFragmentBinding.sixButton.id -> gameFragmentBinding.userGuessEdittext.append("6")
            gameFragmentBinding.sevenButton.id -> gameFragmentBinding.userGuessEdittext.append("7")
            gameFragmentBinding.hintButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.revealButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.resetButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.guessButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.deleteButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
        }

    }
}
