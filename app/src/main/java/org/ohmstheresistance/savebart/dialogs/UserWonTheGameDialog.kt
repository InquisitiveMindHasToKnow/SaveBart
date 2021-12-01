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
import com.bumptech.glide.Glide
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.WinnerWinnerBinding

class UserWonTheGameDialog : DialogFragment(), View.OnClickListener, View.OnTouchListener {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.WideDialog)
    }

    @SuppressLint("ClickableViewAccessibility", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val winnerWinnerBinding = DataBindingUtil.inflate<WinnerWinnerBinding>(inflater, R.layout.winner_winner, container, false)

        val getCombinationBundle = arguments
        val winningCombo: String? = getCombinationBundle?.getString("Combination")

        winnerWinnerBinding.winnerDialogCombinationTextview.text = ("Winning Combo: $winningCombo")

        winnerWinnerBinding.winnerConfirmButton.setOnClickListener(this)
        winnerWinnerBinding.winnerPlayAgainButton.setOnClickListener(this)
        winnerWinnerBinding.winnerConfirmButton.setOnTouchListener(this)
        winnerWinnerBinding.winnerPlayAgainButton.setOnTouchListener(this)

            context?.let {
                Glide.with(it)
                    .load(resources.getDrawable(R.drawable.bartdancing))
                    .into(winnerWinnerBinding.bartWinnerIcon)
            }

        return winnerWinnerBinding.root
    }

    private fun playAgain() {
        targetFragment?.onActivityResult(targetRequestCode, 1, activity?.intent)
        dialog!!.dismiss()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.winner_confirm_button -> dialog!!.dismiss()
            R.id.winner_play_again_button -> playAgain()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){

            MotionEvent.ACTION_DOWN -> v?.background = resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v?.background = resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }
}