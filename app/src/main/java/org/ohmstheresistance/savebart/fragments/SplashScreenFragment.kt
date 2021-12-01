package org.ohmstheresistance.savebart.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.ohmstheresistance.savebart.R
import org.ohmstheresistance.savebart.databinding.SplashScreenFragmentBinding

class SplashScreenFragment : Fragment() {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val splashScreenFragmentBinding = DataBindingUtil.inflate<SplashScreenFragmentBinding>(
            inflater,
            R.layout.splash_screen_fragment,
            container,
            false
        )

        Handler().postDelayed({
            splashScreenFragmentBinding.splashSevenButton.background =
                resources.getDrawable(R.drawable.pressed_rounded_button)
            splashScreenFragmentBinding.splashEdittext.append("7")

            Handler().postDelayed({
                splashScreenFragmentBinding.splashSevenButton.background = resources.getDrawable(R.drawable.rounded_button_corners)
                splashScreenFragmentBinding.splashZeroButton.background = resources.getDrawable(R.drawable.pressed_rounded_button)
                splashScreenFragmentBinding.splashEdittext.append("0")

                Handler().postDelayed({
                    splashScreenFragmentBinding.splashZeroButton.background = resources.getDrawable(
                        R.drawable.rounded_button_corners
                    )
                    splashScreenFragmentBinding.splashFiveButton.background = resources.getDrawable(
                        R.drawable.pressed_rounded_button
                    )
                    splashScreenFragmentBinding.splashEdittext.append("5")

                    Handler().postDelayed({
                        splashScreenFragmentBinding.splashFiveButton.background =
                            resources.getDrawable(
                                R.drawable.rounded_button_corners
                            )
                        splashScreenFragmentBinding.splashThreeButton.background = resources.getDrawable(R.drawable.pressed_rounded_button)
                        splashScreenFragmentBinding.splashEdittext.append("3")

                        Handler().postDelayed({
                            splashScreenFragmentBinding.splashThreeButton.background =
                                resources.getDrawable(R.drawable.rounded_button_corners)
                            splashScreenFragmentBinding.splashEnterButton.background =
                                resources.getDrawable(R.drawable.pressed_rounded_button)

                            Handler().postDelayed({
                                splashScreenFragmentBinding.splashEnterButton.background = resources.getDrawable(R.drawable.rounded_button_corners)
                                splashScreenFragmentBinding.splashCheckmark.visibility =
                                    View.VISIBLE
                            }, 200)
                        }, 200)
                    }, 200)
                }, 200)
            }, 200)
        }, 200)


        Handler().postDelayed({

            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLandingPageFragment())
        },
            2000)

        return splashScreenFragmentBinding.root
    }
}
