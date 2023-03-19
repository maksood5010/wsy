package com.wsyapp.ui.right_menu.term

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
import com.wsyapp.ui.right_menu.term.adapter.PolicyAdapter
import com.wsyapp.utils.LocalizationPref
import kotlinx.android.synthetic.main.fragment_term.*

class TermFragment : BaseFragment() {

    lateinit var termViewModel: TermViewModel
    lateinit var adapter: PolicyAdapter
    lateinit var dataList: MutableList<FaqModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_term, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
        initRv()
        termViewModel = ViewModelProviders.of(this).get(TermViewModel::class.java)
        termViewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            dismissProgressDialog()
            when (it.action) {
                TermViewModel.Action.SUCCESS -> updateUi(it.payload)
                TermViewModel.Action.NETWORK_ERROR -> showToast(getString(R.string.network_error))
                TermViewModel.Action.NOT_FOUND -> showToast(getString(R.string.not_found))
                else -> showToast(getString(R.string.Something_went_wrong))
            }
        })
        getPolicyFromServer()

    }

    private fun initRv() {
        dataList = mutableListOf()
        adapter = PolicyAdapter(requireContext(), dataList)
        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter = adapter
    }

    private fun initToolBar() {

        getMainActivity().showToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().showLeftMenuOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Term))
        getMainActivity().unlockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.Term), View.GONE)


    }

    private fun getPolicyFromServer() {
        if (ConnectionDetector.isNetAvailable(requireContext())) {
            displayProgressDialog(false, getString(R.string.Loading))
            termViewModel.getPolicyFromServer(
                PolicyRequestModel(
                    RepoConstant.API_ST,
                    RepoConstant.TERM_CONDITION,
                    LocalizationPref(requireContext()).getCurrentLanguageForApi()
                )
            )
        } else {
            showToast(getString(R.string.network_error))
        }
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
        adapter.notifyDataSetChanged()
    }

}