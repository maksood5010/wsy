package com.wsyapp.ui.leftmenu.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.ProfileResponseModel
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.MyConstants
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!getMainActivity().isUserLogin()) {
            findNavController().popBackStack()
            getMainActivity().openLoginFragment()
            return
        }

        initToolBar()
        et_name.isEnabled = false
        et_number.isEnabled = false
        et_mail.isEnabled = false
        et_address.isEnabled = false


        cl_manage_car.setOnClickListener(this)
        cl_wallet.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                ProfileViewModel.Action.SUCCESS -> updateUi(it.payload)
                ProfileViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                ProfileViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        getUserProfile()
    }

    private fun getUserProfile() {
        val userModel = getMainActivity().getUserModel() ?: return
        Log.d("TAG", "getUserProfile: userModel.user_id ${userModel.user_id}")

        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getUserProfile(
                userModel.user_id
            )
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun updateUi(payload: ProfileResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }

        val userProfile = payload.user_profile ?: return
        if (userProfile.isEmpty()) return
        val model = userProfile.get(0)
        val userModel = VerifyRequestModel()
        userModel.country_code_phone_number = model.phone
        userModel.email = model.email
        userModel.name = model.name
        userModel.address = model.address
        userModel.balance = model.balance
        userModel.date = model.date
        userModel.device_id = model.device
        userModel.firebase = model.firebase
        userModel.lang = model.lang
        userModel.user_id = model.uid
        userModel.version = model.version
        UserPref(requireContext()).saveUserData(userModel)

        et_name.setText(userModel.name)
        et_mail.setText(userModel.email)
        et_address.setText(userModel.address)
        et_number.setText(userModel.country_code_phone_number)
        tv_wallet.setText(userModel.balance + " " + getString(R.string.aed))
    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.profile))
        getMainActivity().unlockDrawer()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showRightAction(resources.getDrawable(R.drawable.ic_edit))
        getMainActivity().updateToolBar(getString(R.string.profile), View.GONE)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cl_wallet -> openWalletFragment()
            R.id.cl_manage_car -> openEditCarFragment()
        }
    }

    private fun openWalletFragment() {
        findNavController().navigate(R.id.action_profileFragment_to_walletFragment)
    }

    private fun openEditCarFragment() {
        findNavController().navigate(R.id.action_profileFragment_to_carsFragment)
    }

    private fun openEditProfileFragment() {
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> openEditProfileFragment()
        }
        return true
    }

}