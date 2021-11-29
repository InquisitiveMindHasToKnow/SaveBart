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
import org.ohmstheresistance.savebart.databinding.WinnerWinnerBinding

class UserWonTheGameDialog: DialogFragment(), View.OnClickListener {

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.WideDialog)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val winnerWinnerBinding  = DataBindingUtil.inflate<WinnerWinnerBinding>(inflater, R.layout.winner_winner, container, false)

       val getCombinationBundle = arguments
        val winningCombo: String? = getCombinationBundle?.getString("Combination")

        winnerWinnerBinding.winnerDialogCombinationTextview.text = ("Winning Combo: $winningCombo")

        winnerWinnerBinding.winnerConfirmButton.setOnClickListener(this)
        winnerWinnerBinding.winnerPlayAgainButton.setOnClickListener(this)

        return winnerWinnerBinding.root
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.winner_confirm_button -> dialog!!.dismiss()
            R.id.winner_play_again_button -> playAgain()
        }
    }

    private fun playAgain() {
        dialog!!.dismiss()
    }

}