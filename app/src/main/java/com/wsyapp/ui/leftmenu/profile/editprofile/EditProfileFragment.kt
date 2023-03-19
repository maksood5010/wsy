package com.wsyapp.ui.leftmenu.profile.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.beautybirds.helper.ToolBarHelper
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.R
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.SnackBarHelper
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BaseFragment(), View.OnClickListener {

    private lateinit var viewModel: EditProfileViewModel
    private var requestModel: VerifyRequestModel? = null
    private var snackBarHelper: SnackBarHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        et_number.isEnabled = false

        tv_update.setOnClickListener(this)
        setUpdata()

        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                EditProfileViewModel.Action.SUCCESS -> updateUi(it.payload)
                EditProfileViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                EditProfileViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
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
            if (requestModel != null) {
                val userPref = UserPref(requireContext())
                userPref.saveUserData(requestModel!!)
            }
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
                cl_parent,
                "",
                getString(R.string.Success), requireContext()
            )
        } else {
            showSnackBar(cl_parent, "", getString(R.string.Failed))
        }
    }

    private fun setUpdata() {
        val userModel = getMainActivity().getUserModel() ?: return
        et_name.setText(userModel.name)
        et_mail.setText(userModel.email)
        et_number.setText(userModel.country_code_phone_number)
        et_address.setText(userModel.address)
    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Edit_Profile))
        getMainActivity().lockDrawer()
        getMainActivity().showBackOnToolBar()
        getMainActivity().updateToolBar(getString(R.string.Edit_Profile), View.VISIBLE)

        getMainActivity().showRightAction(resources.getDrawable(R.drawable.ic_close))
        getMainActivity().setRightActionListener(object : ToolBarHelper.RightActionListener {
            override fun onRightAction() {
                getMainActivity().onBackPressed()
            }

        })

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_update -> onUpDate()
            R.id.et_number -> {
                findNavController().navigate(R.id.action_editProfileFragment_to_changeFragment)
            }
        }
    }

    private fun onUpDate() {
        val name = et_name.text.toString().trim()
        val phone = et_number.text.toString().trim()
        val mail = et_mail.text.toString().trim()
        val address = et_address.text.toString().trim()
        if (name.isEmpty() || phone.isEmpty() || mail.isEmpty() || address.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.fill_all_fields))
            return
        }
        val userPref = UserPref(requireContext())
        val userModel = userPref.getUserModel() ?: return
        requestModel = VerifyRequestModel()
        requestModel?.name = name
        requestModel?.country_code_phone_number = phone
        requestModel?.email = mail
        requestModel?.address = address
        requestModel?.version = userModel.version
        requestModel?.lang = userModel.lang
        requestModel?.firebase = userModel.firebase
        requestModel?.device_id = userModel.device_id
        requestModel?.date = userModel.date
        requestModel?.balance = userModel.balance
        requestModel?.user_id = userModel.user_id

        verifyUser(requestModel!!)

    }

    private fun verifyUser(verifyRequestModel: VerifyRequestModel) {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.verifyUser(
                verifyRequestModel
            )
        } else {
            showToast(getString(R.string.network_error))
        }
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

}