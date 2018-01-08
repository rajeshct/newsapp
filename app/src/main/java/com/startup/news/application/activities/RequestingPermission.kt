package com.startup.news.application.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.startup.news.application.BuildConfig
import com.startup.news.application.R
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.utils.CheckPermission
import kotlinx.android.synthetic.main.activity_permission_checker.*


/**
 * Created by admin on 1/2/2018.
 */
class RequestingPermission : AppCompatActivity() {

    private var permissionCheckCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_checker)
        permissionCheckCode = intent.getIntExtra(AppConstants.EXTRA_REQUEST_CODE, 0)
        checkPermissionHere()
        requestPermissionAgain.setOnClickListener {
            when (permissionCheckCode) {
                AppConstants.PERMISSION_LOCATION -> requestLocationPermission()
            }
        }
    }

    private fun checkPermissionHere() {
        when (permissionCheckCode) {
            AppConstants.PERMISSION_LOCATION -> {
                text_permission?.text = getString(R.string.error_permission_location)
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        if (!CheckPermission.isLocationPermissionGranted(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), AppConstants.PERMISSION_LOCATION)
        } else {
            finishActivityNormally()
        }
    }

    override fun onResume() {
        super.onResume()
        if (CheckPermission.isLocationPermissionGranted(this))
            finishActivityNormally()
    }

    private fun finishActivityNormally() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (requestCode == AppConstants.PERMISSION_LOCATION) {
            when {
            // If user interaction was interrupted, the permission request is cancelled and you
            // receive empty arrays.
                grantResults.isEmpty() -> finish()
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> finishActivityNormally()
                else -> {
                    Snackbar.make(layout, getString(R.string.error_permission_declined), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.try_again)) { goToSetting() }.show()
                }
            }
        }

    }


    private fun goToSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}