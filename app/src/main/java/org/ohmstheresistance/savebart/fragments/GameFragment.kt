package org.ohmstheresistance.savebart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    lateinit var combination: String
    var totalGuesses = 10

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
            gameFragmentBinding.revealButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.resetButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.guessButton.id -> gameFragmentBinding.userGuessEdittext.append("0")

            gameFragmentBinding.deleteButton.id -> deleteLastEntry()
            gameFragmentBinding.hintButton.id -> displayHint()
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

                    val winningCombinationBundle =  Bundle()

                    val randomNumbersResponse = response.body!!.string()
                    Log.d("Random", randomNumbersResponse)

                    val separatedResponse = randomNumbersResponse.split("\\s+".toRegex())

                    val firstNumber = separatedResponse[0]
                    val secondNumber = separatedResponse[1]
                    val thirdNumber = separatedResponse[2]
                    val fourthNumber = separatedResponse[3]

                     combination = firstNumber + secondNumber + thirdNumber + fourthNumber



                    val numbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7")
                    listOf(numbers).shuffled()

                    val eightNumbersToDisplay = listOf(
                        numbers[6],
                        numbers[4],
                        numbers[2],
                        numbers[0],
                        firstNumber,
                        secondNumber,
                        thirdNumber,
                        fourthNumber
                    )
                    listOf(eightNumbersToDisplay).shuffled()

                    Handler(Looper.getMainLooper()).post {

                        gameFragmentBinding.combinationTextview.text = combination

                        gameFragmentBinding.firstNumberTextview.text = eightNumbersToDisplay[5]
                        gameFragmentBinding.secondNumberTextview.text = eightNumbersToDisplay[1]
                        gameFragmentBinding.thirdNumberTextview.text = eightNumbersToDisplay[3]
                        gameFragmentBinding.fourthNumberTextview.text = eightNumbersToDisplay[0]
                        gameFragmentBinding.fifthNumberTextview.text = eightNumbersToDisplay[7]
                        gameFragmentBinding.sixthNumberTextview.text = eightNumbersToDisplay[2]
                        gameFragmentBinding.seventhNumberTextview.text = eightNumbersToDisplay[4]
                        gameFragmentBinding.eighthNumberTextview.text = eightNumbersToDisplay[6]

                    }
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
    private fun displayHint(){
        val hints = listOf(
            "There is no chance the number to guess is negative.",
            "C'mon! The combination is 4 digits long.",
            "At least one of the numbers above is in the combo.",
            "You have $totalGuesses guesses remaining!",
            "Haha! Not happening!",
            "I could but where's the fun in that?",
            "It's only 4 digits. You got this!",
            "I would've solved it already.",
            "Okay a " + combination[0] + " is included somewhere.",
            "FINE! There's a " + combination[2] + " include somewhere.",
            "You're running out of time!") .random()



        gameFragmentBinding.dispayHintsAndGameStatusTextview.text = hints
        gameFragmentBinding.dispayHintsAndGameStatusTextview.setTextColor(resources.getColor(R.color.hintColor))
    }
}
