package com.wsyapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.beautybirds.base.BaseFragment
import com.wsyapp.MainActivity
import com.wsyapp.R
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "SplashFragment"

class SplashFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mainActivity: MainActivity = getMainActivity() ?: return
        mainActivity.hideToolBar()
        mainActivity.lockDrawer()
        val userModel = UserPref(requireContext()).getUserModel()
        if (userModel == null) {
            mainActivity.userLogout()
        } else {
            mainActivity.userLogin()
        }


    }

    private fun setAnim() {
        val loadAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        loadAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                goToHome()
            }

            override fun onAnimationStart(p0: Animation?) {
            }

        })

        iv_img.startAnimation(loadAnimation)

    }

    override fun onResume() {
        super.onResume()
        setAnim()
    }

    fun goToHome() {
        var mainActivity = getMainActivity()
        val statusGPSCheck = mainActivity.statusGPSCheck()
        if (statusGPSCheck) {
            GlobalScope.launch {
                delay(1000)
                mainActivity.openHome()
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.location_aleart_msg),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

}