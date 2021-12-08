package org.ohmstheresistance.savebart.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.GameDifficultyDialogLayoutBinding
import org.ohmstheresistance.savebart.enums.GameDifficulty

class GameDifficultyDialog: DialogFragment(), View.OnClickListener{

    lateinit var  gameDifficultyDialogLayoutBinding: GameDifficultyDialogLayoutBinding
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
        gameDifficultyDialogLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.game_difficulty_dialog_layout, container, false)

        gameDifficultyDialogLayoutBinding.gameDifficultySelectButton.setOnClickListener(this)


        return gameDifficultyDialogLayoutBinding.root
    }

    override fun onClick(view: View?) {

        val preference = activity?.getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = preference?.edit()

        when(view?.id){

            gameDifficultyDialogLayoutBinding.easyRadioButton.id -> {
                editor?.putString(GameDifficulty.EASY.name, "Easy")
                editor?.commit()
            }

            gameDifficultyDialogLayoutBinding.mediumRadioButton.id -> {
                editor?.putString(GameDifficulty.MEDIUM.name, "Medium")
                editor?.commit()
            }

            gameDifficultyDialogLayoutBinding.hardRadioButton.id -> {
                editor?.putString(GameDifficulty.HARD.name, "Hard")
                editor?.commit()
            }

            gameDifficultyDialogLayoutBinding.gameDifficultySelectButton.id -> {
                dialog!!.dismiss()}
        }
    }
}