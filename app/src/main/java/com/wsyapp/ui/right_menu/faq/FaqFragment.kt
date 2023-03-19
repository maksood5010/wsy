package com.wsyapp.ui.right_menu.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.data.model.request.PolicyRequestModel
import com.wsyapp.data.model.response.FaqModel
import com.wsyapp.data.model.response.PolicyResponseModel
import com.wsyapp.data.repo.RepoConstant
import com.wsyapp.data.repo.repo_base.ConnectionDetector
import com.wsyapp.ui.right_menu.faq.adapter.FaqAdapter
import com.wsyapp.utils.LocalizationPref
import kotlinx.android.synthetic.main.fragment_faq.*

private const val TAG = "FaqFragment"

class FaqFragment : BaseFragment() {

    lateinit var viewModel: FaqViewModel
    lateinit var faqAdapter: FaqAdapter
    lateinit var dataList: MutableList<FaqModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ititToolBar()
        initRv()

        viewModel = ViewModelProviders.of(this).get(FaqViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                FaqViewModel.Action.SUCCESS -> updateUi(it.payload)
                FaqViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                FaqViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })

        getFaqFromServer()
    }

    private fun ititToolBar() {

        getMainActivity().showToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.FAQ))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()

        getMainActivity().updateToolBar(getString(R.string.FAQ), View.GONE)


    }

    private fun initRv() {
        dataList = mutableListOf()
        faqAdapter = FaqAdapter(requireContext(), dataList)
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter = faqAdapter
    }

    private fun updateUi(payload: PolicyResponseModel?) {
        if (payload == null) {
            showToast(getString(R.string.not_found))
            return
        }
        val data = payload.data
        if (data == null || data.isEmpty()) {
            showToast(getString(R.string.not_found))
            return
        }
        dataList.clear()
        dataList.addAll(data)
        faqAdapter.notifyDataSetChanged()

    }

    private fun getFaqFromServer() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            viewModel.getFaqFromServer(
                PolicyRequestModel(
                    RepoConstant.API_ST,
                    RepoConstant.FAQ,
                    LocalizationPref(requireContext()).getCurrentLanguageForApi()
                )
            )
        } else {
            showToast(getString(R.string.network_error))
        }

    }

}