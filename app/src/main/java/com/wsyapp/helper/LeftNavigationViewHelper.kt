package com.beautybirds.helper

import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import com.wsyapp.MainActivity
import com.wsyapp.R
import kotlinx.android.synthetic.main.activity_main.*

open class LeftNavigationViewHelper(activity: MainActivity) : View.OnClickListener {
    private var activity: MainActivity
    private var nav_view_left: NavigationView

    private lateinit var tv_selected: TextView
    private lateinit var tv_login: TextView
    private lateinit var ll_login: LinearLayout
    private lateinit var iv_selected: ImageView

    init {
        this.activity = activity
        this.nav_view_left = activity.nav_view_left
        val headerView = nav_view_left.getHeaderView(0)
        this.tv_login = headerView.findViewById(R.id.tv_login)
        this.ll_login = headerView.findViewById(R.id.ll_login)
    }

    fun getLeftNavView(): NavigationView {
        return nav_view_left
    }

    fun init() {

        nav_view_left.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_home -> {
                        openHomeFragment()
                    }
                    R.id.menu_profile -> openProfileFragment()
                    R.id.menu_my_order -> openMyOrderFragment()
                    R.id.menu_about -> {
                        openAboutFragment()
                    }
                    R.id.menu_faq -> {
                        openFaqFragment()
                    }
                    R.id.menu_term -> {
                        openTermFragment()
                    }
                    R.id.menu_complains -> {
                        openComplainFragment()
                    }
                    R.id.menu_add_garage -> {
                        openAddGarageFragment()
                    }
                    R.id.menu_settings -> {
                        openSettingsActivity()
                    }
                    R.id.tv_login -> onLogin()
                }
                activity.toggleLeft()
                return true
            }

        })
        tv_login.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        activity.toggleLeft()

        when (view!!.id) {
            R.id.tv_login -> onLogin()
        }
    }

    private fun openAddGarageFragment() {
        activity.openAddGarageFragment()
    }

    private fun openHomeFragment() {
        activity.openHomeFragment()
    }

    private fun openSettingsActivity() {
        activity.openSettingsActivity()
    }

    fun rateApp() {
        activity.rateApp()

    }

    private fun openComplainFragment() {
        activity.openComplainFragment()
    }

    private fun openConfirmDialog() {
        activity.openConfirmDialog(activity.getString(R.string.msg))
    }

    private fun openTermFragment() {
        activity.openTermFragment()

    }

    private fun openFaqFragment() {
        activity.openFaqFragment()

    }

    private fun openAboutFragment() {
        activity.openAboutFragment()
    }

    private fun openMyOrderFragment() {
        activity.openMyOrderFragment()

    }

    private fun openProfileFragment() {
        activity.openProfileFragment()
    }

    private fun onXYZ() {
        activity.showToast(activity.getString(R.string.Commig_Soon))
    }

    fun openOfferFragment() {
        activity.openOfferFragment()
    }

    private fun onLogin() {
        activity.openLoginFragment()
    }

    fun openPackageFragment() {
        activity.openPackageFragment()
    }

    fun setSelectedView(textView: TextView, imageView: ImageView) {
        if (iv_selected != null) {
            iv_selected.isSelected = false
        }
        if (tv_selected != null) {
            tv_selected.isSelected = false
        }
        iv_selected = imageView
        tv_selected = textView

        iv_selected.isSelected = true
        tv_selected.isSelected = true
    }

    open fun share(url: String) {
        activity.share(url)
    }

    fun userLogin() {
        ll_login.visibility = View.GONE
        // div_0.visibility = View.GONE
    }

    fun userLogout() {
        ll_login.visibility = View.VISIBLE
        //div_0.visibility = View.VISIBLE
    }
}