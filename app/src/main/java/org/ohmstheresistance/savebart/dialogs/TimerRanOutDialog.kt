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
import org.ohmstheresistance.savebart.databinding.TimerRanOutDialogBinding

class TimerRanOutDialog : DialogFragment(), View.OnClickListener, View.OnTouchListener {

    private lateinit var timerRanOutDialogBinding: TimerRanOutDialogBinding

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireActivity(), R.style.WideDialog)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, savedInstanceState: Bundle?): View {

        timerRanOutDialogBinding = DataBindingUtil.inflate(inflater, R.layout.timer_ran_out_dialog, container, false)

        val getCombinationBundle = arguments
        val winningCombination = getCombinationBundle!!.getString("Combination")
        timerRanOutDialogBinding.timerRanOutWinningCombinationTextview.text = "Winning Combination: $winningCombination"

        timerRanOutDialogBinding.timerRanOutConfirmButton.setOnTouchListener(this)
        timerRanOutDialogBinding.timerRanOutPlayAgainButton.setOnClickListener(this)
        timerRanOutDialogBinding.timerRanOutConfirmButton.setOnClickListener(this)
        timerRanOutDialogBinding.timerRanOutPlayAgainButton.setOnTouchListener(this)

        return timerRanOutDialogBinding.root
    }

    private fun playAgain(){
        targetFragment?.onActivityResult(targetRequestCode, 1, activity?.intent)
        dialog!!.dismiss()

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.timer_ran_out_confirm_button -> dialog!!.dismiss()
            R.id.timer_ran_out_play_again_button -> playAgain()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> v.background = resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v.background = resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }
}
