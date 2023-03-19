package com.wsyapp.ui.leftmenu.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.wsyapp.R
import com.wsyapp.data.model.request.LoginRequestModel
import com.wsyapp.utils.LocalizationPref
import com.wsyapp.utils.MyConstants.Companion.LOGIN_REQUEST_MODEL
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


private const val TAG = "LoginFragment"

class LoginFragment : BaseFragment(), View.OnClickListener {

    private var firebaseAuth: FirebaseAuth? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.login))
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.log_in), View.GONE)

        tv_send.setOnClickListener(this)
        init()
        ccp.registerPhoneNumberTextView(et_number)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_send -> onLogin()
        }
    }

    fun onLogin() {
        var number = et_number.getText().toString().trim()
        var name = et_name.getText().toString().trim()
        var email = et_email.getText().toString().trim()
        if (name.isEmpty()) {
            showSnackBar(rl_view, "", getString(R.string.enter_full_name))

            return
        }
        if (email.isEmpty()) {
            email = ""
        }
        if (number.isEmpty()) {
            showSnackBar(rl_view, "", getString(R.string.enter_number))
            return
        }
        if (!ccp.isValid) {
            showSnackBar(rl_view, "", "invalid number")
            return
        }

        var country_code = ccp.fullNumberWithPlus
        Log.e("TAG", country_code)
        displayProgressDialog(false, "")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            country_code,
            40,
            TimeUnit.SECONDS,
            requireActivity(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    dismissProgressDialog()
                }

                override fun onVerificationFailed(error: FirebaseException) {
                    dismissProgressDialog()
                    if (error is FirebaseAuthInvalidCredentialsException) {
                        showSnackBar(rl_view, "", getString(R.string.Invalid_Mobile_number))
                    } else {
                        showSnackBar(rl_view, "", getString(R.string.SMS_quota_full))

                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    dismissProgressDialog()
                    // Save verification ID and resending token so we can use them later
                    openVerifyFragment(
                        LoginRequestModel(
                            name,
                            email,
                            country_code,
                            number,
                            verificationId
                        )
                    )

                }
            })
    }

    fun init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth!!.setLanguageCode(LocalizationPref(requireContext()).getCurrentLanguage())
    }

    fun openVerifyFragment(model: LoginRequestModel) {
        val bundle = Bundle()
        bundle.putParcelable(LOGIN_REQUEST_MODEL, model)
        findNavController().navigate(R.id.action_loginFragment_to_verifyFragment, bundle)
    }
}