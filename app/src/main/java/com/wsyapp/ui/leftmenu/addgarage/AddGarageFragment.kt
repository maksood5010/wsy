package com.wsyapp.ui.leftmenu.addgarage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.R
import com.wsyapp.data.model.request.AddGarageRequestModel
import com.wsyapp.data.model.response.GarageCategoryModel
import com.wsyapp.data.model.response.GarageCategoryResponseModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.SnackBarHelper
import com.wsyapp.utils.UserPref
import kotlinx.android.synthetic.main.fragment_add_garage.*
import java.io.ByteArrayOutputStream
import java.io.File

private const val TAG = "AddGarageFragment"
const val PICK_GARAGE_LOGO_IMAGE = 123
const val PICK_TRADE_LICENSE_IMAGE = 311

class AddGarageFragment : BaseFragment(), View.OnClickListener {


    private lateinit var viewModel: AddGarageViewModel
    private var list: MutableList<GarageCategoryModel>? = null
    private var snackBarHelper: SnackBarHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_garage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!getMainActivity().isUserLogin()) {
            findNavController().popBackStack()
            getMainActivity().openLoginFragment()
            return
        }

        ititToolBar()
        initSpinner()
        cv_add_photo_cover.setOnClickListener(this)
        cv_license.setOnClickListener(this)
        tv_add.setOnClickListener(this)
        viewModel = ViewModelProviders.of(this).get(AddGarageViewModel::class.java)
        viewModel.getCategoryLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                AddGarageViewModel.Action.SUCCESS -> updateUi(it.payload)
                AddGarageViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                AddGarageViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        viewModel.getaddGarageLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                AddGarageViewModel.Action.SUCCESS -> updateUiForAddGarage(it.payload)
                AddGarageViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                AddGarageViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        getGarageCategories()
    }

    fun updateUiForAddGarage(payload: GlobalResponseModel?) {
        if (payload == null) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            return
        }
        if (payload.success) {

            snackBarHelper = SnackBarHelper()
            snackBarHelper!!.addAppSnackBarListener(object : SnackBarHelper.AppSnackBarListener {
                override fun onDissmissListener(snackbar: Snackbar?) {
                    if (snackBarHelper == null) return
                    if (snackbar == null) return
                    snackbar.dismiss()
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

    private fun updateUi(payload: GarageCategoryResponseModel?) {
        if (payload == null) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            return
        }
        val categories = payload.categories
        val results = categories ?: return@updateUi
        if (results == null || results.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.not_found))
            return
        }
        list = mutableListOf()
        list?.addAll(results)
        var array_category = Array(list!!.size, { "" })

        for (position in list!!.indices) {
            val model = list!!.get(position)
            array_category[position] = model.getName(requireContext()) ?: ""
        }
        val categoryAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            array_category
        )
        sp_category.setAdapter(categoryAdapter)
        sp_category.setText(array_category[0])
        sp_category.setTag(list!!.get(0))

        sp_category.setOnItemClickListener { adapterView, view, position, l ->
            sp_category.setTag(list!!.get(position))
        }


    }

    private fun ititToolBar() {

        getMainActivity().showToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.add_garage))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.add_garage), View.GONE)

    }

    private fun initSpinner() {
        val emirates = resources.getStringArray(R.array.emirates_form)
        val emirates_id = resources.getStringArray(R.array.emirates_id_form)

        val cityAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            emirates
        )
        sp_emirates.setAdapter(cityAdapter)
        sp_emirates.setText(emirates[0])
        sp_emirates.setTag(emirates_id[0])
        sp_emirates.setOnItemClickListener { adapterView, view, position, l ->
            if (position == 0) {
                sp_emirates.setTag(null)
            } else
                sp_emirates.setTag(emirates_id[position])
        }

    }

    private fun getGarageCategories() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getGarageCategories(RepoConstant.API_GET_ALL_CATEGORY_GARAGE)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    fun uploadImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .start(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("OnActivityResult")
        if (resultCode == Activity.RESULT_OK) {

            val fileUri = data?.data
            val file: File? = ImagePicker.getFile(data)

            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(file!!.path, options)


            val path = file!!.path
            val filePath: String? = ImagePicker.getFilePath(data)
            Log.e(TAG, "filePath: $filePath")
            Log.e(TAG, "filePath2 :$path ")

            if (requestCode == PICK_GARAGE_LOGO_IMAGE) {
                val car = bitmaptoBase64(bitmap)
                iv_garage_logo.setTag(R.id.image_car, car)
                iv_garage_logo.setImageURI(fileUri)
                txt_add_photo_cover.setTextColor(resources.getColor(R.color.green))
            } else {
                val image = bitmaptoBase64(bitmap)
                iv_garage_license.setTag(R.id.image_car, image)
                iv_garage_license.setImageURI(fileUri)
                txt_license.setTextColor(resources.getColor(R.color.green))

            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.cv_add_photo_cover -> uploadImage(PICK_GARAGE_LOGO_IMAGE)
            R.id.cv_license -> uploadImage(PICK_TRADE_LICENSE_IMAGE)
            R.id.tv_add -> addWithValidate()
        }
    }

    private fun addWithValidate() {
        val name_eng = et_name_eng.getText().toString().trim()
        val name_ar = et_name_ar.getText().toString().trim()
        val email = et_email.getText().toString().trim()
        val address = et_address.getText().toString().trim()
        val number = et_number.getText().toString().trim()
        val hours = et_hours.getText().toString().trim()

        if (name_eng.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.garage_english_name))
            return
        }
        if (name_ar.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.Garage_arabic_name))
            return
        }
        if (email.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.Email))
            return
        }
        if (address.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.addresss))
            return
        }
        if (number.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.phone))
            return
        }
        if (hours.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.enter_working_hours))
            return
        }

        val garage_logo = iv_garage_logo.getTag(R.id.image_car)
        if (garage_logo == null) {
            showSnackBar(cl_parent, "", getString(R.string.add_garage_cover_photo))
            return
        }

        val garage_license = iv_garage_license.getTag(R.id.image_car)
        if (garage_license == null) {
            showSnackBar(cl_parent, "", getString(R.string.add_trade_license))
            return
        }

        val city_id = sp_emirates.getTag()
        if (city_id == null) {
            showSnackBar(cl_parent, "", getString(R.string.select_city))
            return
        }

        val catModel = sp_category.getTag()
        if (catModel == null) {
            showSnackBar(cl_parent, "", getString(R.string.select_category))
            return
        }
        val garageCategoryModel = catModel as GarageCategoryModel
        if (garageCategoryModel == null || garageCategoryModel.id == null || garageCategoryModel.id.isEmpty()) {
            showSnackBar(cl_parent, "", getString(R.string.select_category))
            return
        }
        val userModel = UserPref(requireContext()).getUserModel()
        if (userModel == null) {
            showSnackBar(cl_parent, "", getString(R.string.Login_first))
            return
        }
        val requestModel = AddGarageRequestModel(
            userModel.user_id,
            garageCategoryModel.id,
            city_id as String,
            name_ar,
            name_eng,
            address,
            email,
            number,
            hours, garage_logo as String, garage_license as String
        )
        addGarege(requestModel)
    }

    private fun addGarege(requestModel: AddGarageRequestModel) {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.addGarge(requestModel)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    fun bitmaptoBase64(bitmap: Bitmap): String {
        var byteArrayOutputStream = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byte = byteArrayOutputStream!!.toByteArray()
        return Base64.encodeToString(byte, Base64.DEFAULT)
    }
}