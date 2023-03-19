package com.beautybirds.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.wsyapp.MainActivity

open class BaseFragment : Fragment() {

    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getMainActivity(): MainActivity {
        if (isValidActivity())
            return activity as MainActivity
        return null!!
    }

    fun isValidActivity(): Boolean {
        if (activity == null || requireActivity().isFinishing) return false
        return true
    }

    open fun printLog(TAG: String, msg: String) {
        Log.e(TAG, msg)
    }

    @Synchronized
    fun hideKeyboard() {
        var view: View? = getMainActivity().currentFocus
        if (view == null) {
            view = View(requireContext())
        }
        hideKeyboard(view)
    }

    @Synchronized
    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val imm: InputMethodManager =
            getMainActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun displayProgressDialog(isCancellable: Boolean, msg: String) {
        getMainActivity().displayProgressDialog(isCancellable, msg)

    }

    fun dismissProgressDialog() {
        getMainActivity().dismissProgressDialog()
    }

    fun showToast(msg: String) {
        getMainActivity().showToast(msg)
    }

    fun showSnackBar(view: View, action: String, msg: String) {
        if (!isValidActivity()) return
        getMainActivity().showSnackBar(view, "", msg)
    }

    fun getPresentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
        layout: Int
    ): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater?.inflate(layout, container, false)
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.getParent() as? ViewGroup)?.removeView(rootView)

        }
        return rootView
    }
}