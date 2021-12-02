package org.ohmstheresistance.savebart.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.SaveBartInstructionsBinding

class SaveBartInstructions: DialogFragment(), View.OnClickListener, View.OnTouchListener {

    private lateinit var saveBartInstructionsBinding: SaveBartInstructionsBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        saveBartInstructionsBinding =   DataBindingUtil.inflate(inflater, R.layout.save_bart_instructions, container, false)

        saveBartInstructionsBinding.gotItButton.setOnClickListener(this)
        saveBartInstructionsBinding.gotItButton.setOnTouchListener(this)

        return saveBartInstructionsBinding.root
    }
    override fun onResume() {
        super.onResume()

        val params:ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onClick(view: View?) {
        when(view?.id){

            saveBartInstructionsBinding.gotItButton.id -> dialog?.dismiss()
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