package com.wsyapp.ui.leftmenu.profile.cars.addcar

import android.app.Activity.RESULT_OK
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
import com.beautybirds.base.BaseFragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.R
import com.wsyapp.data.model.request.AddCarRequestModel
import com.wsyapp.data.model.request.EditCarRequestModel
import com.wsyapp.data.model.response.CarModel
import com.wsyapp.data.model.response.CarTypeResponseModel
import com.wsyapp.data.model.response.CarsTypeModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.utils.MyConstants
import com.wsyapp.utils.SnackBarHelper
import kotlinx.android.synthetic.main.fragment_add_car.*
import java.io.ByteArrayOutputStream
import java.io.File


const val PICK_CAR_IMAGE = 123
const val PICK_REG_IMAGE = 311
private const val TAG = "AddCarFragment"

class AddCarFragment : BaseFragment(), View.OnClickListener {

    private var carTypeList: MutableList<CarsTypeModel>? = null
    private var carTypeArray: Array<String>? = null

    private var carTypeAdapter: ArrayAdapter<String>? = null
    lateinit var viewModel: AddCarViewModel
    private var model: CarModel? = null

    private var snackBarHelper: SnackBarHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        iv_back.setOnClickListener(this)
        iv_save.setOnClickListener(this)
        tv_add_car.setOnClickListener(this)
        cv_reg_car_photo.setOnClickListener(this)
        cv_car_photo.setOnClickListener(this)


        viewModel = ViewModelProviders.of(this).get(AddCarViewModel::class.java)
        viewModel.getAddCarLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                AddCarViewModel.Action.SUCCESS -> updateUi(it.payload)
                AddCarViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                AddCarViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        viewModel.getEditCarLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                AddCarViewModel.Action.SUCCESS -> updateUi(it.payload)
                AddCarViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                AddCarViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        viewModel.getCarTypeLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                AddCarViewModel.Action.SUCCESS -> updateSpinner(it.payload)
                AddCarViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                AddCarViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        initUi()
        getCarType()
    }

    private fun initUi() {
        val arguments = arguments
        if (arguments == null) return
        model = arguments.getParcelable<CarModel>(MyConstants.CAR_MODEL) ?: return
        if (model == null) return
        et_name.setText(model?.name)
        et_model.setText(model?.model)
        et_plate_num.setText(model?.plate)
        Glide.with(requireContext()).load(model?.car).into(iv_car_photo)
        Glide.with(requireContext()).load(model?.img).into(iv_reg_car_photo)
        tv_add_car.setText(getString(R.string.update))
        getMainActivity().updateToolBar(model?.name, View.VISIBLE)

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

    private fun updateUi(payload: GlobalResponseModel?) {
        if (payload == null) return
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
                cl_view,
                "",
                getString(R.string.Success), requireContext()
            )
        } else
            showSnackBar(
                cl_view,
                "",
                getString(R.string.Failed)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("OnActivityResult")
        if (resultCode == RESULT_OK) {

            val fileUri = data?.data
            val file: File? = ImagePicker.getFile(data)

            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(file!!.path, options)


            val path = file!!.path
            val filePath: String? = ImagePicker.getFilePath(data)
            Log.e(TAG, "filePath: $filePath")
            Log.e(TAG, "filePath2 :$path ")

            if (requestCode == PICK_CAR_IMAGE) {
                val car = bitmaptoBase64(bitmap)
                iv_car_photo.setTag(R.id.image_car, car)
                iv_car_photo.setImageURI(fileUri)
            } else {
                val image = bitmaptoBase64(bitmap)
                iv_reg_car_photo.setTag(R.id.image_car, image)
                iv_reg_car_photo.setImageURI(fileUri)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.add_car))
        getMainActivity().lockDrawer()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.add_car), View.VISIBLE)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            // R.id.iv_back, R.id.iv_save -> getMainActivity().onBackPressed()
            R.id.tv_add_car -> addcar()
            R.id.cv_car_photo -> uploadImage(PICK_CAR_IMAGE)
            R.id.cv_reg_car_photo -> uploadImage(PICK_REG_IMAGE)
        }
    }

    private fun addcar() {
        val name = et_name.getText().toString().trim()
        val model_num = et_model.getText().toString().trim()
        val plate_num = et_plate_num.getText().toString().trim()
        if (name.isEmpty()) {
            showToast(getString(R.string.enter_car_name))
            return
        }
        if (model_num.isEmpty()) {
            showToast(getString(R.string.enter_car_model))
            return
        }
        if (plate_num.isEmpty()) {
            showToast(getString(R.string.Enter_plate_number))
            return
        }
        val car_model = sp_emirates.getTag()
        if (car_model == null) {
            showToast(getString(R.string.Select_car_Type))
            return
        }
        var typeModel: CarsTypeModel
        if (car_model is CarsTypeModel) {
            typeModel = car_model
        } else {
            showToast(getString(R.string.Something_went_wrong))
            return
        }
        if (model == null) {
            val carPic = iv_car_photo.getTag(R.id.image_car)
            if (carPic == null) {
                showToast(getString(R.string.Add_car_image))
                return
            }
            val carRegPic = iv_reg_car_photo.getTag(R.id.image_car)
            if (carRegPic == null) {
                showToast(getString(R.string.Add_car_image))
                return
            }


            val car_pic = carPic as String
            val car_reg = carRegPic as String
            var requestModel = AddCarRequestModel(
                RepoConstant.API_ADD_CAR,
                name,
                "1",
                model_num,
                plate_num,
                car_pic,
                car_reg, typeModel.id
            )
            addCarToServer(requestModel)
        } else {
            val carPic = iv_car_photo.getTag(R.id.image_car)
            var car_path = ""
            if (carPic != null) {
                car_path = carPic as String

            }
            val carRegPic = iv_reg_car_photo.getTag(R.id.image_car)
            var car_reg = ""

            if (carRegPic != null) {
                car_reg = carRegPic as String
            }
            var requestModel = EditCarRequestModel(
                RepoConstant.API_UPDATE_USER_CAR,
                name,
                "1",
                model_num,
                plate_num,
                model!!.id!!, car_reg, car_path, typeModel.id
            )
            editCar(requestModel)
        }


    }

    private fun editCar(requestModel: EditCarRequestModel) {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.editCar(requestModel)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun addCarToServer(requestModel: AddCarRequestModel) {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.addCar(requestModel)
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

    private fun getCarType() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getCarType(RepoConstant.API_GET_CAR_TYPE)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun updateSpinner(payload: CarTypeResponseModel?) {

        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        if (!payload.success) return
        val list = payload.cars_type ?: return
        if (list == null || list.isEmpty()) {
            showToast(getString(R.string.not_found))
            return
        }

        carTypeList = mutableListOf()
        carTypeList!!.addAll(list)
        carTypeArray = Array(carTypeList!!.size, { "" })

        for (position in carTypeList!!.indices) {
            carTypeArray!![position] = carTypeList!!.get(position).getTypeName(requireContext())
        }

        carTypeAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            carTypeArray!!
        )
        sp_emirates.setAdapter(carTypeAdapter)

        if (model == null) {
            val model_carType = carTypeList!!.get(0)
            sp_emirates.setText(model_carType.getTypeName(requireContext()))
            sp_emirates.setTag(model_carType)
        } else {
            val typeId = model!!.type_id ?: return
            setSelectionOnSpinner(typeId)
        }
        sp_emirates.setOnItemClickListener { adapterView, view, position, l ->
            Log.e(TAG, "onItemSelected")
            val model = carTypeList!!.get(position)
            sp_emirates.setTag(model)
        }

    }

    private fun setSelectionOnSpinner(typeId: String?) {
        if (carTypeList == null) return
        for (item in carTypeList!!.indices) {
            val model = carTypeList!!.get(item) ?: null
            val id = model!!.id ?: return
            if (id == typeId) {
                sp_emirates.setText(model.getTypeName(requireContext()))
                sp_emirates.setTag(model)
            }

        }
    }
}