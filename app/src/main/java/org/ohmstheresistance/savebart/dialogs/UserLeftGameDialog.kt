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
import org.ohmstheresistance.savebart.databinding.UserLeftGameBinding

class UserLeftGameDialog: DialogFragment() , View.OnClickListener, View.OnTouchListener {

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

        val userLeftGameBinding = DataBindingUtil.inflate<UserLeftGameBinding>(inflater, R.layout.user_left_game, container, false)
        userLeftGameBinding.userLeftGamePlayAgainButton.setOnClickListener(this)
        userLeftGameBinding.userLeftGamePlayAgainButton.setOnTouchListener(this)

        return userLeftGameBinding.root
    }

    private fun playAgain() {
        targetFragment?.onActivityResult(targetRequestCode, 1, activity?.intent)
        dialog!!.dismiss()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.user_left_game_play_again_button -> playAgain()
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