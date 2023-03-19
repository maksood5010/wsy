package com.wsyapp.ui.leftmenu.profile.cars

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.model.request.CarDetailsRequestModel
import com.wsyapp.data.model.request.DeleteCarRequestModel
import com.wsyapp.data.model.response.CarDetailsResponseModel
import com.wsyapp.data.model.response.CarModel
import com.wsyapp.data.model.response.GlobalResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.leftmenu.profile.cars.adapter.CarAdapter
import com.wsyapp.utils.MyConstants.Companion.CAR_MODEL
import kotlinx.android.synthetic.main.fragment_cars.*

class CarsFragment : BaseFragment(), View.OnClickListener {

    lateinit var carViewModel: CarViewModel

    private lateinit var carList: MutableList<CarModel>
    private lateinit var carAdapter: CarAdapter
    private var cur_delte_car: CarModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_cars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        iv_add_car.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        iv_add_car.setOnClickListener(this)
        nodataFoundView(View.GONE)
        initRv()

        carViewModel = ViewModelProviders.of(this).get(CarViewModel::class.java)
        carViewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CarViewModel.Action.SUCCESS -> updateUi(it.payload)
                CarViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CarViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        carViewModel.getDeleteCarLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                CarViewModel.Action.SUCCESS -> carDeleteUi(it.payload)
                CarViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                CarViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        getCarDetails()
    }

    private fun nodataFoundView(visibility: Int) {
        tv_no_data_found.visibility = visibility

    }

    private fun carDeleteUi(payload: GlobalResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        if (!payload!!.success) {
            showToast(getString(R.string.Failed))
            return
        }
        if (cur_delte_car != null) {
            if (cur_delte_car is CarModel) {
                carList.remove(cur_delte_car as CarModel)
                carAdapter.notifyDataSetChanged()
                showToast(getString(R.string.Success))
                if (carList.size == 0)
                    nodataFoundView(View.VISIBLE)
                else
                    nodataFoundView(View.GONE)
            }
        }

    }

    private fun getCarDetails() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            val requestModel =
                CarDetailsRequestModel(RepoConstant.API_ALL_CAR_DETAILS, "1")
            carViewModel.getCarDetails(requestModel)
        } else {
            showToast(getString(R.string.network_error))
        }
    }

    private fun updateUi(payload: CarDetailsResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        if (!payload!!.success) {
            showToast(getString(R.string.Something_went_wrong))
            return
        }
        val cars = payload.cars
        if (cars == null || cars.size == 0) {
            nodataFoundView(View.VISIBLE)
            return
        }
        nodataFoundView(View.GONE)
        carList.clear()
        carList.addAll(cars)
        carAdapter.notifyDataSetChanged()
    }

    fun initToolBar() {
        getMainActivity().showTitleOnToolBar(getString(R.string.manage_your_cars))
        getMainActivity().showToolBar()

        getMainActivity().updateToolBar(getString(R.string.manage_your_cars), View.VISIBLE)
        getMainActivity().hideLeftMenuOnToolBar()
       // getMainActivity().hideTitleOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideRightAction()

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_back -> getMainActivity().onBackPressed()
            R.id.iv_add_car -> openAddCarFragment(null)
        }
    }

    private fun initRv() {
        carList = mutableListOf()
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        carAdapter = CarAdapter(requireContext(), carList)
        carAdapter.setAppClickListener(object : CarAdapter.AppClickListener {
            override fun onClickListener(view: View, position: Int) {
                val model = carList[position]
                if (model == null) return
                when (view!!.id) {
                    R.id.iv_edit_car -> {
                        openAddCarFragment(model)
                    }
                    R.id.iv_delete -> {
                        val requestModel = DeleteCarRequestModel(
                            RepoConstant.API_DELETE_USER_CAR,
                            "1",
                            model.id ?: "0"
                        )
                        cur_delte_car = model
                        carViewModel.deleteCar(requestModel)
                    }
                }
            }

        })
        rv_view.adapter = carAdapter
    }

    private fun openAddCarFragment(model: CarModel?) {
        val bundle = Bundle()
        if (model != null) {
            bundle.putParcelable(CAR_MODEL, model)
        }
        findNavController().navigate(R.id.action_carsFragment_to_addCarFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_car_mgmt, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_car -> openAddCarFragment(null)
        }
        return true
    }
}