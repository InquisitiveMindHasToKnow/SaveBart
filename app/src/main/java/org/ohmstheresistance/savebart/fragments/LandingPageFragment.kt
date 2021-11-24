package org.ohmstheresistance.savebart.fragments

import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.LandingPageFragmentBinding
import org.ohmstheresistance.savebart.dialogs.SaveBartInstructions

class LandingPageFragment : Fragment(), View.OnClickListener{

    lateinit var landingPageFragmentBinding: LandingPageFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       landingPageFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.landing_page_fragment, container, false)

        landingPageFragmentBinding.playNowButton.setOnClickListener(this)
        landingPageFragmentBinding.instructionsButton.setOnClickListener(this)


        return landingPageFragmentBinding.root
    }

    override fun onClick(view: View?) {

        when(view?.id){

            landingPageFragmentBinding.instructionsButton.id -> fragmentManager?.let {
                SaveBartInstructions().show(
                    it, "SaveBartInstructions")
            }
            landingPageFragmentBinding.playNowButton.id -> findNavController().navigate(LandingPageFragmentDirections.actionLandingPageFragmentToGameFragment())
        }
    }


}