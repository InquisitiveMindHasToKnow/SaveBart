package org.ohmstheresistance.savebart.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.DataBindingUtil
import okhttp3.*
import okio.IOException
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.GameFragmentBinding

class GameFragment : Fragment(), View.OnClickListener, View.OnTouchListener{

    lateinit var gameFragmentBinding: GameFragmentBinding
    lateinit var combination: String
    var totalGuesses = 10
    var matchCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gameFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        initializeButtons()
        getRandomNumbers()

        return gameFragmentBinding.root
    }

    private fun initializeButtons() {
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

        when (view?.id) {

            gameFragmentBinding.zeroButton.id -> gameFragmentBinding.userGuessEdittext.append("0")
            gameFragmentBinding.oneButton.id -> gameFragmentBinding.userGuessEdittext.append("1")
            gameFragmentBinding.twoButton.id -> gameFragmentBinding.userGuessEdittext.append("2")
            gameFragmentBinding.threeButton.id -> gameFragmentBinding.userGuessEdittext.append("3")
            gameFragmentBinding.fourButton.id -> gameFragmentBinding.userGuessEdittext.append("4")
            gameFragmentBinding.fiveButton.id -> gameFragmentBinding.userGuessEdittext.append("5")
            gameFragmentBinding.sixButton.id -> gameFragmentBinding.userGuessEdittext.append("6")
            gameFragmentBinding.sevenButton.id -> gameFragmentBinding.userGuessEdittext.append("7")
            gameFragmentBinding.revealButton.id -> gameFragmentBinding.userGuessEdittext.append("0")

            gameFragmentBinding.resetButton.id -> resetGame()
            gameFragmentBinding.deleteButton.id -> deleteLastEntry()
            gameFragmentBinding.hintButton.id -> displayHint()
            gameFragmentBinding.guessButton.id -> {

                if (totalGuesses > 0) {

                    if (gameFragmentBinding.userGuessEdittext.text.toString().length in 1..3) {

                        gameFragmentBinding.feedbackTextview.text =
                            resources.getText(R.string.enter_four_digits)
                        return
                    }

                    if (gameFragmentBinding.userGuessEdittext.text.toString().isEmpty()) {
                        gameFragmentBinding.feedbackTextview.text =
                            resources.getText(R.string.enter_valid_entry)
                        return
                    }

                    totalGuesses--
                    gameFragmentBinding.remainingGuessCountTextview.text = totalGuesses.toString()
                    gameFragmentBinding.userGuessEdittext.text.clear()

                    animateBartLinear()
                }
            }
        }
    }

    private fun getRandomNumbers() {

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

                    val winningCombinationBundle = Bundle()

                    val randomNumbersResponse = response.body!!.string()
                    Log.d("Random", randomNumbersResponse)

                    val separatedResponse = randomNumbersResponse.split("\\s+".toRegex())

                    val firstNumber = separatedResponse[0]
                    val secondNumber = separatedResponse[1]
                    val thirdNumber = separatedResponse[2]
                    val fourthNumber = separatedResponse[3]

                    combination = firstNumber + secondNumber + thirdNumber + fourthNumber


                    val numbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7")
                    listOf(numbers).random()

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
                    listOf(eightNumbersToDisplay).random()

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

    private fun deleteLastEntry() {
        val guessLength = gameFragmentBinding.userGuessEdittext.text.length

        if (guessLength > 0) {
            gameFragmentBinding.userGuessEdittext.text.delete(guessLength - 1, guessLength)
        }
    }

    private fun displayHint() {
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
            "You're running out of time!"
        ).random()

        gameFragmentBinding.dispayHintsAndGameStatusTextview.text = hints
        gameFragmentBinding.dispayHintsAndGameStatusTextview.setTextColor(resources.getColor(R.color.hintColor))
    }

    private fun resetGame() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager?.beginTransaction()?.detach(this)?.commitNow()
            fragmentManager?.beginTransaction()?.attach(this)?.commitNow()
        } else {
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit();
        }
        gameFragmentBinding.userGuessEdittext.text.clear()
        totalGuesses = 10
    }

    private fun disableButtons() {

        gameFragmentBinding.zeroButton.isEnabled = false
        gameFragmentBinding.oneButton.isEnabled = false
        gameFragmentBinding.twoButton.isEnabled = false
        gameFragmentBinding.threeButton.isEnabled = false
        gameFragmentBinding.fourButton.isEnabled = false
        gameFragmentBinding.fiveButton.isEnabled = false
        gameFragmentBinding.sixButton.isEnabled = false
        gameFragmentBinding.sevenButton.isEnabled = false
        gameFragmentBinding.revealButton.isEnabled = false
        gameFragmentBinding.guessButton.isEnabled = false
        gameFragmentBinding.deleteButton.isEnabled = false
        gameFragmentBinding.hintButton.isEnabled = false
    }

    private fun animateBrick(brick: ImageView) {
        brick.startAnimation(
            AnimationUtils.loadAnimation(
                context, R.anim.slide_out_right
            )
        )
        Handler().postDelayed({ brick.visibility = View.INVISIBLE }, 200)
    }

    private fun animateBartLinear(){

        if(totalGuesses == 9){
            animateBrick(gameFragmentBinding.brickTenImageview)
        }
        if(totalGuesses == 8){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartchilling)})
            animateBrick(gameFragmentBinding.brickNineImageview)
        }
        if(totalGuesses == 7){
            animateBrick(gameFragmentBinding.brickEightImageview)
        }
        if(totalGuesses == 6){
            animateBrick(gameFragmentBinding.brickSevenImageview)
        }
        if(totalGuesses == 5){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartjumping)})
            animateBrick(gameFragmentBinding.brickSixImageview)
            }
        if(totalGuesses == 4){
            animateBrick(gameFragmentBinding.brickFiveImageview)
        }
        if(totalGuesses == 3){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartscared)})
            animateBrick(gameFragmentBinding.brickFourImageview)
        }
        if(totalGuesses == 2){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartscared)})
            animateBrick(gameFragmentBinding.brickThreeImageview)
        }
        if(totalGuesses == 1){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartscared)})
            animateBrick(gameFragmentBinding.brickTwoImageview)
        }
        if(totalGuesses == 0 && matchCounter != 4){
            gameFragmentBinding.personImageview.setImageDrawable(context?.let { getDrawable(it, R.drawable.bartfalling)})
            animateBrick(gameFragmentBinding.brickOneImageview)

            gameFragmentBinding.personImageview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_bottom))
            gameFragmentBinding.brickOneImageview.visibility = View.INVISIBLE

            gameFragmentBinding.dispayHintsAndGameStatusTextview.text = resources.getText(R.string.you_lost_text)

            disableButtons()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){

            MotionEvent.ACTION_DOWN -> v?.background = resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v?.background = resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }


}

