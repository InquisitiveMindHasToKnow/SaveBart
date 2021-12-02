package org.ohmstheresistance.savebart.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.LandingPageFragmentBinding
import org.ohmstheresistance.savebart.dialogs.SaveBartInstructions

class LandingPageFragment : Fragment(), View.OnTouchListener{

    private lateinit var landingPageFragmentBinding: LandingPageFragmentBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       landingPageFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.landing_page_fragment, container, false)

        landingPageFragmentBinding.playNowButton.setOnTouchListener(this)
        landingPageFragmentBinding.instructionsButton.setOnTouchListener(this)


        landingPageFragmentBinding.instructionsButton.setOnClickListener {
            val saveBartInstructions = SaveBartInstructions()
            activity?.let { fragmentManager?.let { it -> saveBartInstructions.show(it, "SaveBartInstructions") } }
        }

        landingPageFragmentBinding.playNowButton.setOnClickListener {

            if (connectedToInternet()) {
                findNavController().navigate(LandingPageFragmentDirections.actionLandingPageFragmentToGameFragment())
            }else{
                Toast.makeText(context, "No internet connection. Please connect and try again.", Toast.LENGTH_SHORT).show()
            }
        }

        return landingPageFragmentBinding.root
    }


    private fun connectedToInternet(): Boolean{
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
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