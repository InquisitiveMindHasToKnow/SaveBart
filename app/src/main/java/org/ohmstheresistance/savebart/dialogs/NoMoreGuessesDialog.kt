package org.ohmstheresistance.savebart.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.NoMoreGuessesBinding

class NoMoreGuessesDialog: DialogFragment(), View.OnClickListener, View.OnTouchListener {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.WideDialog)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val noMoreGuessesDialogBinding = DataBindingUtil.inflate<NoMoreGuessesBinding>(inflater, R.layout.no_more_guesses, container, false)

        val getCombinationBundle = arguments

        val winningCombo: String = getCombinationBundle?.getString("Combination").toString()

        noMoreGuessesDialogBinding.noMoreGuessesWinningCombinationTextview.text = ("Winning combination: $winningCombo")
        noMoreGuessesDialogBinding.noMoreGuessesConfirmButton.setOnClickListener(this)
        noMoreGuessesDialogBinding.noMoreGuessesPlayAgainButton.setOnClickListener(this)
        noMoreGuessesDialogBinding.noMoreGuessesPlayAgainButton.setOnTouchListener(this)
        noMoreGuessesDialogBinding.noMoreGuessesPlayAgainButton.setOnTouchListener(this)

        return noMoreGuessesDialogBinding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.no_more_guesses_confirm_button -> dialog!!.dismiss()
            R.id.no_more_guesses_play_again_button -> playAgain()
        }
    }

    private fun playAgain() {
        targetFragment?.onActivityResult(targetRequestCode, 1, activity?.intent)
        dialog!!.dismiss()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){

            MotionEvent.ACTION_DOWN -> v?.background = resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v?.background = resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }
}