package org.ohmstheresistance.savebart.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.*
import okio.IOException
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.adapters.PrevGuessesAdapter
import org.ohmstheresistance.savebart.databinding.GameFragmentBinding
import org.ohmstheresistance.savebart.dialogs.NoMoreGuessesDialog
import org.ohmstheresistance.savebart.dialogs.TimerRanOutDialog
import org.ohmstheresistance.savebart.dialogs.UserRevealedComboDialog
import org.ohmstheresistance.savebart.dialogs.UserWonTheGameDialog
import java.util.*
import kotlin.collections.ArrayList

class GameFragment : Fragment(), View.OnClickListener, View.OnTouchListener {

    lateinit var gameFragmentBinding: GameFragmentBinding
    lateinit var combination: String
    private var totalGuesses = 10
    private var numberMatchCounter = 0
    private var rightsGuesses = ArrayList<Int>()

    val COUNTDOWN_TIMER_IN_MILLIS = 90000
    var timeLeftInMillis = 0
    lateinit var countDownTimer: CountDownTimer

    private var comboList = ArrayList<String>()
    private var prevGuessesEnteredList = ArrayList<String>()
    val winningCombinationBundle = Bundle()

    private lateinit var prevGuessesAdapter: PrevGuessesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        gameFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        initializeButtons()
        checkInternetConnection()

        return gameFragmentBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
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

        gameFragmentBinding.zeroButton.setOnTouchListener(this)
        gameFragmentBinding.oneButton.setOnTouchListener(this)
        gameFragmentBinding.twoButton.setOnTouchListener(this)
        gameFragmentBinding.threeButton.setOnTouchListener(this)
        gameFragmentBinding.fourButton.setOnTouchListener(this)
        gameFragmentBinding.fiveButton.setOnTouchListener(this)
        gameFragmentBinding.sixButton.setOnTouchListener(this)
        gameFragmentBinding.sevenButton.setOnTouchListener(this)
        gameFragmentBinding.hintButton.setOnTouchListener(this)
        gameFragmentBinding.revealButton.setOnTouchListener(this)
        gameFragmentBinding.resetButton.setOnTouchListener(this)
        gameFragmentBinding.guessButton.setOnTouchListener(this)
        gameFragmentBinding.deleteButton.setOnTouchListener(this)


        gameFragmentBinding.prevGuessRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        prevGuessesAdapter = PrevGuessesAdapter(prevGuessesEnteredList, comboList, rightsGuesses)
        gameFragmentBinding.prevGuessRecycler.adapter = prevGuessesAdapter
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

            gameFragmentBinding.revealButton.id -> revealCombination()
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

                    animateBartLinear()
                }
            }
        }
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
            "Nope! Not happening!",
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
        prevGuessesEnteredList.clear()
        rightsGuesses.clear()
        comboList.clear()

        totalGuesses = 10
        numberMatchCounter = 0
        countDownTimer.cancel()

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

    private fun animateBartLinear() {

        checkWhatMessageToDisplay()

        if (totalGuesses == 9 && numberMatchCounter != 4) {
            animateBrick(gameFragmentBinding.brickTenImageview)
        }
        if (totalGuesses == 8) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartchilling
                )
            })
            animateBrick(gameFragmentBinding.brickNineImageview)
        }
        if (totalGuesses == 7) {
            animateBrick(gameFragmentBinding.brickEightImageview)
        }
        if (totalGuesses == 6) {
            animateBrick(gameFragmentBinding.brickSevenImageview)
        }
        if (totalGuesses == 5) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartjumping
                )
            })
            animateBrick(gameFragmentBinding.brickSixImageview)
        }
        if (totalGuesses == 4) {
            animateBrick(gameFragmentBinding.brickFiveImageview)
        }
        if (totalGuesses == 3) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartscared
                )
            })
            animateBrick(gameFragmentBinding.brickFourImageview)
        }
        if (totalGuesses == 2) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartscared
                )
            })
            animateBrick(gameFragmentBinding.brickThreeImageview)
        }
        if (totalGuesses == 1) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartscared
                )
            })
            animateBrick(gameFragmentBinding.brickTwoImageview)
        }
        if (totalGuesses == 0 && numberMatchCounter != 4) {
            gameFragmentBinding.personImageview.setImageDrawable(context?.let {
                getDrawable(
                    it,
                    R.drawable.bartfalling
                )
            })
            animateBrick(gameFragmentBinding.brickOneImageview)

            gameFragmentBinding.personImageview.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.exit_bottom
                )
            )
            gameFragmentBinding.brickOneImageview.visibility = View.INVISIBLE

            gameFragmentBinding.combinationLinear.visibility = View.VISIBLE
            gameFragmentBinding.feedbackTextview.text = resources.getText(R.string.no_more_guesses_feedback)
            gameFragmentBinding.dispayHintsAndGameStatusTextview.text = resources.getText(R.string.you_lost_text)
            gameFragmentBinding.userGuessEdittext.setBackgroundColor(resources.getColor(R.color.low_guesses_color))
            gameFragmentBinding.userGuessEdittext.setText(combination)

            val noMoreGuessesDialog = NoMoreGuessesDialog()
            noMoreGuessesDialog.arguments = winningCombinationBundle
            noMoreGuessesDialog.setTargetFragment(this, 1)
            activity?.let { fragmentManager?.let { it -> noMoreGuessesDialog.show(it, "NoMoreGuessesDialog") } }

            disableButtons()
        }
    }
    private fun checkWhatMessageToDisplay() {
        prevGuessesEnteredList.add(gameFragmentBinding.userGuessEdittext.text.toString())

        matchCounter(combination, gameFragmentBinding.userGuessEdittext.text.toString())
        rightsGuesses.add(numberMatchCounter)

        prevGuessesAdapter.setData(prevGuessesEnteredList)
        prevGuessesAdapter.setComboInfo(comboList)
        prevGuessesAdapter.setCorrectItems(rightsGuesses)

        when (numberMatchCounter) {
            1 -> {
                gameFragmentBinding.feedbackTextview.text =
                    resources.getText(R.string.one_entry_correct)
                gameFragmentBinding.userGuessEdittext.setText("")
                numberMatchCounter = 0

            }
            2 -> {
                gameFragmentBinding.feedbackTextview.text =
                    resources.getText(R.string.two_entries_correct)
                gameFragmentBinding.userGuessEdittext.setText("")
                numberMatchCounter = 0

            }
            3 -> {
                gameFragmentBinding.feedbackTextview.text =
                    resources.getText(R.string.three_entries_correct)
                gameFragmentBinding.userGuessEdittext.setText("")
                numberMatchCounter = 0

            }
            4 -> {
                userWon()
            }
            else -> {
                gameFragmentBinding.feedbackTextview.text = resources.getText(R.string.incorrect)
                gameFragmentBinding.userGuessEdittext.setText("")
            }
        }
    }

    private fun matchCounter(combo: String, entry: String): Int {
        for (i in combo.indices) {
            if (combo[i] == entry[i]) {
                numberMatchCounter++
            }
        }
        return numberMatchCounter
    }

    private fun revealCombination() {

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.RevealDialog)
        alertDialog.setTitle("Reveal combination?")
        alertDialog.setMessage("Completing this action will result in a loss!")
        alertDialog.setPositiveButton("Confirm",
            DialogInterface.OnClickListener { dialog, which ->

                countDownTimer.cancel()

                gameFragmentBinding.combinationLinear.visibility = View.VISIBLE
                gameFragmentBinding.feedbackTextview.text = resources.getText(R.string.revealed_answer_feedback)
                gameFragmentBinding.dispayHintsAndGameStatusTextview.text = resources.getText(R.string.you_lost_text)

                gameFragmentBinding.userGuessEdittext.setBackgroundColor(resources.getColor(R.color.low_guesses_color))
                gameFragmentBinding.userGuessEdittext.setText(combination)

                val userRevealedComboDialog = UserRevealedComboDialog()
                userRevealedComboDialog.setTargetFragment(this, 1)
                userRevealedComboDialog.arguments = winningCombinationBundle
                activity?.let {
                    fragmentManager?.let { it ->
                        userRevealedComboDialog.show(
                            it,
                            "UserRevealedComboDialog"
                        )
                    }
                }
                disableButtons()

                animateBrick(gameFragmentBinding.brickOneImageview)
                animateBrick(gameFragmentBinding.brickTwoImageview)
                animateBrick(gameFragmentBinding.brickThreeImageview)
                animateBrick(gameFragmentBinding.brickFourImageview)
                animateBrick(gameFragmentBinding.brickFiveImageview)
                animateBrick(gameFragmentBinding.brickSixImageview)
                animateBrick(gameFragmentBinding.brickSevenImageview)
                animateBrick(gameFragmentBinding.brickEightImageview)
                animateBrick(gameFragmentBinding.brickNineImageview)
                animateBrick(gameFragmentBinding.brickTenImageview)

                makeBricksInvisible(gameFragmentBinding.brickOneImageview)
                makeBricksInvisible(gameFragmentBinding.brickTwoImageview)
                makeBricksInvisible(gameFragmentBinding.brickThreeImageview)
                makeBricksInvisible(gameFragmentBinding.brickFourImageview)
                makeBricksInvisible(gameFragmentBinding.brickFiveImageview)
                makeBricksInvisible(gameFragmentBinding.brickSixImageview)
                makeBricksInvisible(gameFragmentBinding.brickSevenImageview)
                makeBricksInvisible(gameFragmentBinding.brickEightImageview)
                makeBricksInvisible(gameFragmentBinding.brickNineImageview)
                makeBricksInvisible(gameFragmentBinding.brickTenImageview)

                gameFragmentBinding.personImageview.setImageResource(R.drawable.bartfalling)
                gameFragmentBinding.personImageview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_bottom))
            })
        alertDialog.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, which -> })
        alertDialog.show()
    }

    private fun userWon() {
        disableButtons()

        gameFragmentBinding.combinationLinear.visibility = View.VISIBLE
        gameFragmentBinding.dispayHintsAndGameStatusTextview.text =
            resources.getText(R.string.you_won_text)
        gameFragmentBinding.feedbackTextview.text = resources.getText(R.string.correct)
        gameFragmentBinding.userGuessEdittext.setBackgroundColor(resources.getColor(R.color.userWonColor))
        gameFragmentBinding.userGuessEdittext.setText(combination)

        countDownTimer.cancel()

        val userWonTheGameDialog = UserWonTheGameDialog()
        userWonTheGameDialog.arguments = winningCombinationBundle
        userWonTheGameDialog.setTargetFragment(this, 1)
        activity?.let { fragmentManager?.let { it -> userWonTheGameDialog.show(it, "WinnerWinnerDialog") }
        }
        disableButtons()
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

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    val randomNumbersResponse = response.body!!.string()
                    Log.d("Random", randomNumbersResponse)

                    val separatedResponse = randomNumbersResponse.split("\\s+".toRegex())

                    val firstNumber = separatedResponse[0]
                    val secondNumber = separatedResponse[1]
                    val thirdNumber = separatedResponse[2]
                    val fourthNumber = separatedResponse[3]

                    combination = firstNumber + secondNumber + thirdNumber + fourthNumber
                    comboList.add(combination)

                    winningCombinationBundle.putString("Combination", combination)

                    val numbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7")
                    numbers.random()

                    val eightNumbersToDisplay = listOf(numbers[6], numbers[4], numbers[2], numbers[0], firstNumber, secondNumber, thirdNumber, fourthNumber)
                    eightNumbersToDisplay.random()

                    Handler(Looper.getMainLooper()).post {

                        startCountDown()
                        gameFragmentBinding.combinationTextview.text = combination

                        gameFragmentBinding.firstNumberTextview.text = eightNumbersToDisplay[5]
                        gameFragmentBinding.secondNumberTextview.text = eightNumbersToDisplay[1]
                        gameFragmentBinding.thirdNumberTextview.text = eightNumbersToDisplay[3]
                        gameFragmentBinding.fourthNumberTextview.text = eightNumbersToDisplay[0]
                        gameFragmentBinding.fifthNumberTextview.text = eightNumbersToDisplay[7]
                        gameFragmentBinding.sixthNumberTextview.text = eightNumbersToDisplay[2]
                        gameFragmentBinding.seventhNumberTextview.text = eightNumbersToDisplay[4]
                        gameFragmentBinding.eighthNumberTextview.text = eightNumbersToDisplay[6]

                        gameFragmentBinding.revealButton.isEnabled = true
                        gameFragmentBinding.guessButton.isEnabled = true
                        gameFragmentBinding.hintButton.isEnabled = true
                    }
                }
            }
        })
    }
    private fun makeBricksInvisible(brick: ImageView){
        brick.visibility = View.INVISIBLE
    }
    private fun userLostBecauseTimerRanOut() {
        gameFragmentBinding.combinationLinear.visibility = View.VISIBLE

        animateBrick(gameFragmentBinding.brickOneImageview)
        animateBrick(gameFragmentBinding.brickTwoImageview)
        animateBrick(gameFragmentBinding.brickThreeImageview)
        animateBrick(gameFragmentBinding.brickFourImageview)
        animateBrick(gameFragmentBinding.brickFiveImageview)
        animateBrick(gameFragmentBinding.brickSixImageview)
        animateBrick(gameFragmentBinding.brickSevenImageview)
        animateBrick(gameFragmentBinding.brickEightImageview)
        animateBrick(gameFragmentBinding.brickNineImageview)
        animateBrick(gameFragmentBinding.brickTenImageview)

        makeBricksInvisible(gameFragmentBinding.brickOneImageview)
        makeBricksInvisible(gameFragmentBinding.brickTwoImageview)
        makeBricksInvisible(gameFragmentBinding.brickThreeImageview)
        makeBricksInvisible(gameFragmentBinding.brickFourImageview)
        makeBricksInvisible(gameFragmentBinding.brickFiveImageview)
        makeBricksInvisible(gameFragmentBinding.brickSixImageview)
        makeBricksInvisible(gameFragmentBinding.brickSevenImageview)
        makeBricksInvisible(gameFragmentBinding.brickEightImageview)
        makeBricksInvisible(gameFragmentBinding.brickNineImageview)
        makeBricksInvisible(gameFragmentBinding.brickTenImageview)

        gameFragmentBinding.personImageview.setImageResource(R.drawable.bartfalling)
        gameFragmentBinding.personImageview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_bottom))
        gameFragmentBinding.feedbackTextview.text = resources.getText(R.string.ran_out_of_time)
        gameFragmentBinding.dispayHintsAndGameStatusTextview.text = resources.getText(R.string.you_lost_text)
        gameFragmentBinding.userGuessEdittext.setBackgroundColor(resources.getColor(R.color.low_guesses_color))
        gameFragmentBinding.userGuessEdittext.setText(combination)

        val timerRanOutDialog = TimerRanOutDialog()
        timerRanOutDialog.arguments = winningCombinationBundle
        timerRanOutDialog.setTargetFragment(this, 1)
        activity?.let { fragmentManager?.let { it -> timerRanOutDialog.show(it, "TimerRanOutDialog") }
        }
        disableButtons()
    }

    private fun checkInternetConnection() {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectedToInternet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
        if (connectedToInternet) {
            getRandomNumbers()

        } else {
            Toast.makeText(context, "No Internet Connection.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {

            MotionEvent.ACTION_DOWN -> v?.background =
                resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v?.background =
                resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 1) {
            resetGame()
        }
    }

    private fun startCountDown() {

        countDownTimer = object : CountDownTimer(COUNTDOWN_TIMER_IN_MILLIS.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished.toInt()
                updateCountDownText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateCountDownText()
                userLostBecauseTimerRanOut()
            }
        }.start()
    }
    private fun updateCountDownText() {
        val seconds = (timeLeftInMillis / 1000) % 60
        val formattedTime = String.format(
            Locale.getDefault(), "%02d:%02d",
            (timeLeftInMillis / 1000 / 60), seconds
        )
        gameFragmentBinding.countdownTimerTextview.text = formattedTime
        if (timeLeftInMillis < 30000) {
            gameFragmentBinding.countdownTimerTextview.setTextColor(resources.getColor(R.color.lose_and_timer_running_out_color))

        } else {
            gameFragmentBinding.countdownTimerTextview.setTextColor(gameFragmentBinding.countdownTimerTextview.textColors)
        }
        if (totalGuesses == 0) {
            countDownTimer.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(countDownTimer != null){
            countDownTimer.cancel()
        }
    }
}

