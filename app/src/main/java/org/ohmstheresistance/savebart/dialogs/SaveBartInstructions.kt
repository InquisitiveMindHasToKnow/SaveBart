package org.ohmstheresistance.savebart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.SaveBartInstructionsBinding
import org.ohmstheresistance.savebart.fragments.LandingPageFragmentDirections

class SaveBartInstructions: DialogFragment(), View.OnClickListener {

    lateinit var saveBartInstructionsBinding: SaveBartInstructionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        saveBartInstructionsBinding =   DataBindingUtil.inflate(inflater, R.layout.save_bart_instructions, container, false)

        saveBartInstructionsBinding.gotItButton.setOnClickListener(this)
        saveBartInstructionsBinding.playNowFromInstructionsButton.setOnClickListener(this)

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

            saveBartInstructionsBinding.playNowFromInstructionsButton.id -> {findNavController().navigate(LandingPageFragmentDirections.actionLandingPageFragmentToGameFragment())
            dialog?.dismiss()}

            saveBartInstructionsBinding.gotItButton.id -> dialog?.dismiss()
        }
    }
}