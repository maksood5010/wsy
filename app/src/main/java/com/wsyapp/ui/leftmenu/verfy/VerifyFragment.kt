package com.wsyapp.ui.leftmenu.verfy

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.wsyapp.R
import com.wsyapp.data.model.request.LoginRequestModel
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.LocalizationPref
import com.wsyapp.utils.MyConstants
import com.wsyapp.utils.SnackBarHelper
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_verify.*

private const val TAG = "VerifyFragment"

class VerifyFragment : BaseFragment(), View.OnClickListener {

    private var firebaseAuth: FirebaseAuth? = null

    private var loginRequestModel: LoginRequestModel? = null
    private var userModle: VerifyRequestModel? = null
    private lateinit var viewModel: VerifyViewModel
    private var snackBarHelper: SnackBarHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Verification_code))
        getMainActivity().updateToolBar(getString(R.string.Verification_code), View.VISIBLE)

        getMainActivity().hideRightAction()

        initData()
        init()
        tv_confirm.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(VerifyViewModel::class.java)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                VerifyViewModel.Action.SUCCESS -> updateUi(it.payload)
                VerifyViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                VerifyViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
    }

    private fun updateUi(payload: GlobalResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        if (payload.success) {
            saveUserdataInPref()
        } else {
            showSnackBar(cp_parent, "", getString(R.string.Failed))
        }
    }

    private fun saveUserdataInPref() {
        if (userModle == null) {
            showSnackBar(cp_parent, "", getString(R.string.not_found))
            return
        }

        UserPref(requireContext()).saveUserData(userModle!!)
        getMainActivity().userLogin()

        snackBarHelper = SnackBarHelper()
        snackBarHelper!!.addAppSnackBarListener(object : SnackBarHelper.AppSnackBarListener {
            override fun onDissmissListener(snackbar: Snackbar?) {
                if (snackBarHelper == null) return
                if (snackbar == null) return
                snackbar.dismiss()
                findNavController().popBackStack(R.id.loginFragment, false)
                getMainActivity().onBackPressed()
            }

        })
        snackBarHelper!!.showSnackBarWithListenner(
            cp_parent,
            "",
            getString(R.string.Success), requireContext()
        )
    }
    override fun onDestroyView() {
        if (snackBarHelper == null) {
            super.onDestroyView()
            return
        }
        snackBarHelper?.removeAppSnackBarListener()
        val snackbar = snackBarHelper?.getSnackbar() ?: return
        if (snackbar != null) {
            snackbar.dismiss()
        }
        super.onDestroyView()
    }
    fun init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth!!.setLanguageCode(LocalizationPref(requireContext()).getCurrentLanguage())
    }

    private fun initData() {
        val arguments = arguments ?: return
        loginRequestModel =
            arguments.getParcelable<LoginRequestModel>(MyConstants.LOGIN_REQUEST_MODEL) ?: return
        tv_number.setText(loginRequestModel!!.phone)
        setGitImg()
    }

    private fun setGitImg() {


        Glide.with(requireContext()).load(resources.getDrawable(R.drawable.verify_me))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            }).into(iv_marker)


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_confirm -> onOtpVerification()
        }
    }

    private fun onOtpVerification() {
        val code = et_code.getText().toString().trim()
        if (code.isEmpty()) {
            showSnackBar(cp_parent, "", getString(R.string.Please_enter_code))
            return
        }
        if (loginRequestModel == null) {
            showSnackBar(cp_parent, "", getString(R.string.Something_went_wrong))
            return
        }
        val credential =
            PhoneAuthProvider.getCredential(loginRequestModel!!.verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        if (loginRequestModel == null) {
            showSnackBar(cp_parent, "", getString(R.string.Something_went_wrong))
            return
        }
        val verifyRequestModel = VerifyRequestModel()
        verifyRequestModel.email = loginRequestModel?.email ?: ""
        verifyRequestModel.name = loginRequestModel?.name ?: ""
        verifyRequestModel.country_code_phone_number =
            loginRequestModel?.phone ?: ""
        //verifyRequestModel.phone_number = loginRequestModel?.phone_number ?: ""
        displayProgressDialog(false, "")
        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener {
            dismissProgressDialog()
            if (it.isSuccessful) {
                val result = it.result
                if (result != null) {
                    val user = result.user
                    if (user == null) {
                        showSnackBar(cp_parent, "", getString(R.string.Incorrect_OTP_entered))
                        return@addOnCompleteListener
                    }
                    verifyRequestModel.user_id = user.uid
                    Log.e(TAG, "user_id: " + user.uid)

                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (it.result == null) {
                                showSnackBar(
                                    cp_parent,
                                    "",
                                    getString(R.string.Something_went_wrong)
                                )
                                return@addOnCompleteListener
                            }
                            verifyRequestModel.firebase = it.result?.token ?: ""
                            Log.e(TAG, "token: " + it.result?.token)
                            getHomeSliderFromServer(verifyRequestModel)

                        }
                    }

                }


            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    showSnackBar(cp_parent, "", getString(R.string.Incorrect_OTP_entered))

                } else {
                    showSnackBar(cp_parent, "", getString(R.string.Something_went_wrong))

                }
            }
        }
    }

    private fun getHomeSliderFromServer(verifyRequestModel: VerifyRequestModel) {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            userModle = verifyRequestModel
            viewModel.verifyUser(
                verifyRequestModel
            )
        } else {
            showToast(getString(R.string.network_error))
        }
    }
}