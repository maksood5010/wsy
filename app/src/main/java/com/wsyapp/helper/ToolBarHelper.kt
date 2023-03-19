package com.beautybirds.helper

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.wsyapp.MainActivity
import com.wsyapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

open class ToolBarHelper(val activity: MainActivity) : View.OnClickListener {
    private var drawer_layout: DrawerLayout = activity.drawer_layout

    private var app_bar: AppBarLayout = activity.app_bar
    private var toolbar: Toolbar = activity.toolbar
    private var tv_title: TextView = activity.tv_title

    private var toggleButton: ActionBarDrawerToggle? = null
    private var listener: RightActionListener? = null
    fun init() {
        activity.setSupportActionBar(toolbar)
        setUpdrawer()
        changeToolBarMenuColors()
    }

    private fun setUpdrawer() {
        toggleButton = ActionBarDrawerToggle(
            activity,
            drawer_layout,
            toolbar,
            R.string.app_name,
            R.string.app_name
        )
        drawer_layout.addDrawerListener(toggleButton!!)
        toggleButton?.syncState()
    }

    fun changeToolBarMenuColors() {
        toolbar?.navigationIcon?.setColorFilter(
            ContextCompat.getColor(
                activity,
                android.R.color.white
            ), PorterDuff.Mode.SRC_ATOP
        )
    }

    override fun onClick(view: View?) {


        when (view!!.id) {
            R.id.iv_back -> activity.onBackPressed()
            R.id.iv_left_menu -> toggleLeft()
        }
    }

    public fun toggleLeft() {
        if (drawer_layout.isDrawerVisible(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    fun hideToolBar() {
        // cl_tool_bar.visibility = View.GONE
        app_bar.visibility = View.GONE
    }

    fun showToolBar() {
        //   cl_tool_bar.visibility = View.VISIBLE
        app_bar.visibility = View.VISIBLE
    }

    fun hideLeftMenuOnToolBar() {
    }

    fun showLeftMenuOnToolBar() {
    }

    fun hideTitleOnToolBar() {
        tv_title.visibility = View.GONE
    }

    fun showTitleOnToolBar(title: String) {
        tv_title.setText(title)
        tv_title.visibility = View.VISIBLE
    }

    fun hideBackOnToolBar() {
    }

    fun showBackOnToolBar() {
    }

    open fun unlockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    open fun lockDrawer() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun showRightAction(res: Drawable) {
    }

    fun hideRightAction() {
    }

    fun setRightActionListener(listener: RightActionListener) {
        this.listener = listener
    }

    interface RightActionListener {
        fun onRightAction()
    }


    var mToolBarNavigationListenerIsRegistered = false
    fun updateToolBar(
        title: String?,
        back_visibility: Int
    ) {
        updateToolBar(
            title,
            activity.supportActionBar!!,
            back_visibility
        )
    }


    private fun updateToolBar(
        title: String?,
        actionBar: ActionBar,
        back_visibility: Int
    ) {


        if (back_visibility == View.VISIBLE) {
            //You may not want to open the drawer on swipe from the left in this case
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            // Remove hamburger
            toggleButton!!.isDrawerIndicatorEnabled = false
            // Show back button
            actionBar.setDisplayHomeAsUpEnabled(true)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggleButton!!.toolbarNavigationClickListener =
                    View.OnClickListener { // Doesn't have to be onBackPressed
                        activity.navController.navigateUp()
                    }
                mToolBarNavigationListenerIsRegistered = true
            }
        } else {
            //You must regain the power of swipe for the drawer.
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // Remove back button
            actionBar.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            toggleButton!!.isDrawerIndicatorEnabled = true
            // Remove the/any drawer toggle listener
            toggleButton!!.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
        }
        actionBar.setDisplayShowTitleEnabled(false)
        tv_title.setText(title)
        //    toolbar.setTitle(title)
    }

}