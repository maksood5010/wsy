package com.wsyapp.ui.home.carservice.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.beautybirds.base.BaseFragment
import com.wsyapp.R
import com.wsyapp.ui.home.carservice.search.adapter.SearchAdressAdapter
import kotlinx.android.synthetic.main.fragment_search_address.*

class SearchAddressFragment : BaseFragment(), View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initRv()

        iv_back.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        actv_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 == null) return
                val keyword = p0.toString().trim()
                if (keyword.length > 0) {
                    rv_view.visibility = View.VISIBLE
                } else {
                    hideKeyboard()
                    clearUi()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun initRv() {
        rv_view.visibility = View.GONE

        rv_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_view.adapter = SearchAdressAdapter(requireContext())
    }

    private fun initToolBar() {
        getMainActivity().hideToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().hideBackOnToolBar()
        getMainActivity().hideTitleOnToolBar()
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.home), View.GONE)


    }

    override fun onClick(p0: View?) {
        hideKeyboard()
        when (p0!!.id) {
            R.id.iv_back -> getMainActivity().onBackPressed()
            R.id.iv_close -> clearUi()

        }
    }

    private fun clearUi() {
        actv_search.setText("")
    }
}