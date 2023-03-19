package com.wsyapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.beautybirds.base.BaseActivity
import com.wsyapp.utils.LocalizationUtil

class MainActivity() : BaseActivity() {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 989

    override fun onCreate(savedInstanceState: Bundle?) {
        LocalizationUtil.applyLanguage(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAllUi(this)
//        val permission = ContextCompat.checkSelfPermission(this,
//            Manifest.permission.READ_PHONE_STATE)
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1)
//        }else{
//            getIMEI()
//        }
    }
    private fun getIMEI() {

        val tm =getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var deviceUuid:String
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            deviceUuid=tm.getImei(0)

        } else {
            deviceUuid = "" + tm.getDeviceId()
        }

        val deviceId: String = deviceUuid
        Log.d("TAG", "getIMEI: IMEI id :" + deviceId)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        /*val currentDestination = navController.currentDestination
        when (currentDestination!!.id) {
        }*/
    }

    fun openHome() {
        val checkPermission = checkPermission()

        if (checkPermission) {
            val statusGPSCheck = statusGPSCheck()
            if (statusGPSCheck) {
                navController.navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                Toast.makeText(this, getString(R.string.location_aleart_msg), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



    fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        openHome()
                    }
                } else {
                    Toast.makeText(
                        this, getString(R.string.permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }
}