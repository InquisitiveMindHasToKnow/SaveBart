package org.ohmstheresistance.savebart.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.NoMoreGuessesBinding

class NoMoreGuessesDialog: DialogFragment(), View.OnClickListener {

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

        return noMoreGuessesDialogBinding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.no_more_guesses_confirm_button -> dialog!!.dismiss()
            R.id.no_more_guesses_play_again_button -> playAgain()
        }
    }

    private fun playAgain() {
        dialog!!.dismiss()
    }
}