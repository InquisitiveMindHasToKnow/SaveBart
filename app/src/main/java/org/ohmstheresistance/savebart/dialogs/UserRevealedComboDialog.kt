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
import org.ohmstheresistance.savebart.databinding.UserRevealedComboBinding

class UserRevealedComboDialog: DialogFragment(), View.OnClickListener, View.OnTouchListener {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.WideDialog)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val userRevealedComboBinding = DataBindingUtil.inflate<UserRevealedComboBinding>(inflater, R.layout.user_revealed_combo, container, false)

        val getCombinationBundle = arguments

        val winningCombo: String = getCombinationBundle?.getString("Combination").toString()
        userRevealedComboBinding.revealedCombinationWinningCombinationTextview.text = ("Winning Combo: $winningCombo")

        userRevealedComboBinding.revealedCombinationConfirmButton.setOnClickListener(this)
        userRevealedComboBinding.revealedCombinationPlayAgainButton.setOnClickListener(this)
        userRevealedComboBinding.revealedCombinationConfirmButton.setOnTouchListener(this)
        userRevealedComboBinding.revealedCombinationPlayAgainButton.setOnTouchListener(this)

        return userRevealedComboBinding.root
    }

    private fun playAgain() {
        dialog!!.dismiss()
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.revealed_combination_confirm_button -> dialog!!.dismiss()
            R.id.revealed_combination_play_again_button -> playAgain()
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