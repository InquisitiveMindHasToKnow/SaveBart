package org.ohmstheresistance.savebart.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import okhttp3.*
import okio.IOException
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
        getRandomNumbers()

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

            gameFragmentBinding.deleteButton.id -> deleteLastEntry()
        }
    }

    private fun getRandomNumbers(){

        val okHttpClient = OkHttpClient()

        val baseOfNumbers = "10"
        val col = "1"
        val num = "4"
        val minNum = "0"
        val maxNum = "7"
        val format = "plain"
        val rnd = "new"

        val url =
            "https://www.random.org/integers/?num=$num&min=$minNum&max=$maxNum&col=$col&base=$baseOfNumbers&format=$format&rnd=$rnd"

        val request = Request.Builder()
            .url(url)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    val randomNumbersResponse = response.body!!.string()
                    Log.d("Random", randomNumbersResponse)

                }
            }
        })
    }

    private fun deleteLastEntry(){
        val guessLength = gameFragmentBinding.userGuessEdittext.text.length

        if(guessLength > 0){
            gameFragmentBinding.userGuessEdittext.text.delete(guessLength - 1, guessLength)
        }
    }
}
