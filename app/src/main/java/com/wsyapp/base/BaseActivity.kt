package com.beautybirds.base

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.beautybirds.helper.LeftNavigationViewHelper
import com.beautybirds.helper.ToolBarHelper
import com.google.android.material.snackbar.Snackbar
import com.wsyapp.MainActivity
import com.wsyapp.R
import com.wsyapp.activity.SettingsActivity
import com.wsyapp.data.model.request.VerifyRequestModel
import com.wsyapp.data.model.response.HomeSliderResponseModel
import com.wsyapp.ui.dialog.appupdate.AppUpdateDialog
import com.wsyapp.ui.dialog.confirm_dialog.ConfirmDialog
import com.wsyapp.ui.home.HomeFragment
import com.wsyapp.ui.home.cart.CartFragment
import com.wsyapp.ui.leftmenu.addgarage.AddGarageFragment
import com.wsyapp.ui.leftmenu.login.LoginFragment
import com.wsyapp.ui.leftmenu.myorder.MyOrderFragment
import com.wsyapp.ui.leftmenu.profile.ProfileFragment
import com.wsyapp.ui.right_menu.about.AboutFragment
import com.wsyapp.ui.right_menu.complains.ComplainFragment
import com.wsyapp.ui.right_menu.faq.FaqFragment
import com.wsyapp.ui.right_menu.term.TermFragment
import com.wsyapp.utils.UserPref


open class BaseActivity() : AppCompatActivity() {
    private lateinit var toolBarHelper: ToolBarHelper
    private lateinit var leftNavHelper: LeftNavigationViewHelper
    lateinit var navController: NavController
    private lateinit var progressDialog: Dialog
    private lateinit var tv_loader_msg: TextView

    fun initAllUi(activity: MainActivity) {
        toolBarHelper = ToolBarHelper(activity)

        leftNavHelper = LeftNavigationViewHelper(activity)
        toolBarHelper.init()
        leftNavHelper.init()
        navController = activity.findNavController(R.id.fragment_nav_host)

        onitProgressDialog()

    }

    private fun onitProgressDialog() {
        progressDialog = Dialog(this, R.style.CustomAlertDialogStyle)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.progress_dialog)
        tv_loader_msg = progressDialog.findViewById<TextView>(R.id.tv_loader_msg)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    open fun printLog(TAG: String, msg: String) {
        Log.e(TAG, msg)
    }

    @Synchronized
    fun hideKeyboard() {
        var view: View? = currentFocus
        if (view == null) {
            view = View(this)
        }
        hideKeyboard(view)
    }

    @Synchronized
    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun hideToolBar() {
        toolBarHelper.hideToolBar()
    }

    fun showToolBar() {
        toolBarHelper.showToolBar()

    }

    fun toggleLeft() {
        toolBarHelper.toggleLeft()

    }

    fun showToast(msg: String) {
        if (isFinishing) return
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun openVerifyFragment() {
        navController.navigate(R.id.verifyFragment)
    }

    fun openLoginFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is LoginFragment) return
        navController.navigate(R.id.loginFragment)
    }

    fun openAboutFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is AboutFragment) return
        navController.navigate(R.id.aboutFragment)
    }

    fun openOfferFragment() {
        navController.navigate(R.id.offerFragment)
    }

    fun openFaqFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is FaqFragment) return
        navController.navigate(R.id.faqFragment)

    }

    fun openComplainFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is ComplainFragment) return
        navController.navigate(R.id.complainFragment)
    }

    fun openAddGarageFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is AddGarageFragment) return
        navController.navigate(R.id.addGarageFragment)
    }

    fun openHomeFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is HomeFragment) return

        navController.popBackStack(R.id.homeFragment, true)
        navController.navigate(R.id.homeFragment)
    }

    fun openPackageFragment() {
        navController.navigate(R.id.packageFragment)

    }

    fun openTermFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is TermFragment) return
        navController.navigate(R.id.termFragment)

    }

    fun openMyOrderFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is MyOrderFragment) return
        navController.navigate(R.id.myOrderFragment)

    }


    fun openProfileFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is ProfileFragment) return
        navController.navigate(R.id.profileFragment)
    }

    fun hideLeftMenuOnToolBar() {
        toolBarHelper.hideLeftMenuOnToolBar()
    }

    fun openConfirmDialog(msg: String) {
        val confirmDialog = ConfirmDialog(this)
        confirmDialog.setMessage(msg)
        confirmDialog.setOnClickListener(object : ConfirmDialog.OnClickListener {
            override fun onClick() {
                confirmDialog.dismiss()
                openDialer()
            }
        })
        confirmDialog.show()
    }

    open fun share(url: String) {
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "WSY")
            var sAux =
                resources.getString(R.string.Let_me_recommend_you_this)
            sAux = sAux + url
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            startActivity(
                Intent.createChooser(
                    i,
                    resources.getString(R.string.Choose_one)
                )
            )
        } catch (e: Exception) {
            //  Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }


    fun rateApp() {
        val uri =
            Uri.parse("market://details?id=" + "com.facebook.lite")
        //    Uri.parse("market://details?id=" + activity.getPackageName())
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, resources.getString(R.string.unable_to_find_market_app),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openDialer() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+971 545454545")
        startActivity(intent)
    }

    fun showLeftMenuOnToolBar() {
        toolBarHelper.showLeftMenuOnToolBar()
    }

    fun hideTitleOnToolBar() {
        toolBarHelper.hideTitleOnToolBar()
    }

    fun showTitleOnToolBar(title: String) {
        toolBarHelper.showTitleOnToolBar(title)
    }

    fun hideBackOnToolBar() {
        toolBarHelper.hideBackOnToolBar()
    }

    fun updateToolBar(
        title: String?,
        back_visibility: Int
    ) {
        toolBarHelper.updateToolBar(title, back_visibility)
    }

    fun showBackOnToolBar() {
        toolBarHelper.showBackOnToolBar()
    }

    open fun unlockDrawer() {
        toolBarHelper.unlockDrawer()

    }

    open fun lockDrawer() {
        toolBarHelper.lockDrawer()

    }

    fun showRightAction(res: Drawable) {
        toolBarHelper.showRightAction(res)

    }

    fun hideRightAction() {
        toolBarHelper.hideRightAction()

    }

    fun setRightActionListener(listener: ToolBarHelper.RightActionListener) {
        toolBarHelper.setRightActionListener(listener)

    }

    open fun statusGPSCheck(): Boolean {

        val manager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true

    }

    open fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.gps_aleart_msg))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes),
                DialogInterface.OnClickListener
                { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            )
            .setNegativeButton(getString(R.string.no),
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    open fun getCurrentVisibleFragment(): Fragment {
        val findFragmentById = supportFragmentManager.findFragmentById(R.id.fragment_nav_host)
            ?: return null!!
        val navHostFragment = findFragmentById as NavHostFragment
        val childFragmentManager = navHostFragment.childFragmentManager ?: return null!!
        val fragments = childFragmentManager.fragments ?: return null!!
        if (fragments.size == 0) return null!!

        return fragments.get(0)

    }


    fun displayProgressDialog(isCancellable: Boolean, msg: String) {
        if (isFinishing) return
        dismissProgressDialog()
        progressDialog.setCancelable(isCancellable)
        if (msg == null || msg.trim().isEmpty()) {
            tv_loader_msg.visibility = View.GONE
        } else {
            tv_loader_msg.visibility = View.VISIBLE
            tv_loader_msg.setText(msg)
        }
        progressDialog.show()

    }

    fun dismissProgressDialog() {
        if (isFinishing || progressDialog == null) return
        progressDialog.dismiss()
    }

    fun openAppUpdateDialog(model: HomeSliderResponseModel) {
        val dialog = AppUpdateDialog(this)
        dialog.setModel(model)
        dialog.setOnClickListener(object : AppUpdateDialog.OnClickListener {
            override fun onClick() {
                dialog.dismiss()
                rateApp()
            }

        })
        dialog.show()
    }

    fun showSnackBar(view: View, action: String?, msg: String) {

        if (isFinishing) return
        val snackbar = Snackbar
            .make(view, msg, Snackbar.LENGTH_LONG)
        var isEmpty = action?.isEmpty() ?: false
        if (action != null || isEmpty)
            snackbar.setAction(action) {
            }

        snackbar.setActionTextColor(Color.WHITE)
        snackbar.setBackgroundTint(resources.getColor(R.color.green))
        val sbView = snackbar.view
        val textView =
            sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

    fun userLogin() {
        leftNavHelper.userLogin()
    }

    fun userLogout() {
        leftNavHelper.userLogout()
    }

    fun isUserLogin(): Boolean {
        val userModel = UserPref(this).getUserModel()
        return userModel != null
    }

    fun getUserModel(): VerifyRequestModel? {
        val userModel = UserPref(this).getUserModel()
        return userModel
    }

    fun openCartFragment() {
        val currentVisibleFragment = getCurrentVisibleFragment()
        if (currentVisibleFragment is CartFragment) return
        navController.navigate(R.id.cartFragment)
    }
}