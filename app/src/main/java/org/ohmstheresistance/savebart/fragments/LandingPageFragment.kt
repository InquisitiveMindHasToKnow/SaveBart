package org.ohmstheresistance.savebart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.LandingPageFragmentBinding
import org.ohmstheresistance.savebart.dialogs.SaveBartInstructions

class LandingPageFragment : Fragment(), View.OnClickListener, View.OnTouchListener{

    lateinit var landingPageFragmentBinding: LandingPageFragmentBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       landingPageFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.landing_page_fragment, container, false)

        landingPageFragmentBinding.playNowButton.setOnClickListener(this)
        landingPageFragmentBinding.instructionsButton.setOnClickListener(this)
        landingPageFragmentBinding.playNowButton.setOnTouchListener(this)
        landingPageFragmentBinding.instructionsButton.setOnTouchListener(this)


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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){

            MotionEvent.ACTION_DOWN -> v?.background = resources.getDrawable(R.drawable.pressed_rounded_button)
            MotionEvent.ACTION_UP -> v?.background = resources.getDrawable(R.drawable.rounded_button_corners)
        }
        return false
    }
}