package com.startup.news.application.userlocation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.*
import android.support.annotation.NonNull
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.startup.news.ApplicationInitializer
import com.startup.news.application.activities.RequestingPermission
import com.startup.news.application.constants.AppConstants
import com.startup.news.application.service.FetchUserAddress
import com.startup.news.application.utils.CheckPermission
import com.startup.news.application.utils.Utils
import java.text.DateFormat
import java.util.*

/**
 * @param locationCallback send callback to parent class
 * @param didFetchAddress  user Address based on Lat,Log but may be null if didFetchAddress=false
 */
open class FetchUserLocation(val context: Activity?, var locationCallback: LocationCallbackToClass?, var didFetchAddress: Boolean) {
    private var mResultReceiver: AddressResultReceiver? = null

    private val TAG = FetchUserLocation::class.java.simpleName
    /**
     * Constant used in the location settings dialog.
     */
    private val REQUEST_CHECK_SETTINGS = 0x1
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    // Keys for storing activity state in the Bundle.
    private val KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates"
    private val KEY_LOCATION = "location"
    private val KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string"
    /**
     * Provides access to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    /**
     * Provides access to the Location Settings API.
     */
    private var mSettingsClient: SettingsClient? = null
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private var mLocationRequest: LocationRequest? = null
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    /**
     * Callback for Location events.
     */
    private var mLocationCallback: LocationCallback? = null
    /**
     * Represents a geographical location.
     */
    private var mCurrentLocation: Location? = null
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private var mRequestingLocationUpdates: Boolean = false
    /**
     * Time when the location was updated represented as a String.
     */
    private var mLastUpdateTime: String? = null


    fun beginFetchUserLocation(savedInstanceState: Bundle?) {
        mRequestingLocationUpdates = false
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState)
        getUserLocation()
    }

    fun removeCallback() {
        locationCallback = null
    }


    private fun getUserLocation() {
        mLastUpdateTime = ""
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ApplicationInitializer.instance.applicationContext)
        mSettingsClient = LocationServices.getSettingsClient(ApplicationInitializer.instance.applicationContext)
        // Acquire a reference to the system Location Manager
        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
        startLocationUpdates()
    }

    /**
     * Stores activity data in the Bundle.
     */
    fun onSaveInstanceState(@NonNull savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates)
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation)
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime)
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES)
            }
            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable<Parcelable>(KEY_LOCATION) as Location
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING)
            }
        }
    }


    /**
     * Sets up the location request. Android has two location request settings:
     * `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     *
     *
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     *
     *
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest?.numUpdates = 1
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    /**
     * Creates a callback for receiving location events.
     */
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                mCurrentLocation?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                    stopLocationUpdates()
                    if (didFetchAddress) {
                        startIntentService(latLng)
                    } else {
                        if (locationCallback != null) {
                            setResultForClient(mCurrentLocation, null)
                        }
                    }
                }
            }
        }
    }


    protected fun startIntentService(location: LatLng) {
        if (mResultReceiver == null) {
            mResultReceiver = AddressResultReceiver(Handler())
        }
        val intent = Intent(context, FetchUserAddress::class.java)
        intent.putExtra(AppConstants.RECEIVER, mResultReceiver)
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, location)
        context?.startService(intent)
    }

    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            // Display the address string
            // or an error message sent from the intent service.
            val address = resultData.getString(AppConstants.ADDRESS)
            if (locationCallback != null && mCurrentLocation != null) {
                setResultForClient(mCurrentLocation, address)
            }
        }
    }

    private fun setResultForClient(mCurrentLocation: Location?, address: String?) {
        locationCallback?.userLocation(mCurrentLocation, address)
        locationCallback = null
    }

    /**
     * Uses a [com.google.android.gms.location.LocationSettingsRequest.Builder] to build
     * a [com.google.android.gms.location.LocationSettingsRequest] that is used for checking
     * if a device has the needed location settings.
     */
    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest?.let {
            builder.addLocationRequest(it)
        }
        mLocationSettingsRequest = builder.build()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
        // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Log.i(TAG, "User agreed to make required location settings changes.")
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, "User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
            }
        // Nothing to do. startLocationupdates() gets called in onResume again.
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            context?.let {
                if (CheckPermission.isLocationPermissionGranted(context)) { // Begin by checking if the device has the necessary location settings.
                    mSettingsClient?.checkLocationSettings(mLocationSettingsRequest)?.addOnSuccessListener(context, {
                        mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback,
                                Looper.myLooper())
                    })?.addOnFailureListener(context, { e ->
                        val statusCode = (e as ApiException).statusCode
                        when (statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                setErrorForClient()
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " + "location settings ")
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(context as Activity?, REQUEST_CHECK_SETTINGS)
                                } catch (sie: IntentSender.SendIntentException) {
                                    Log.i(TAG, "PendingIntent unable to execute request.")
                                }

                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                setErrorForClient()
                                val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Utils.showToast(context, errorMessage)
                                mRequestingLocationUpdates = false
                            }
                        }

                    })
                } else {
                    val intent = Intent(context, RequestingPermission::class.java)
                    intent.putExtra(AppConstants.EXTRA_REQUEST_CODE, AppConstants.PERMISSION_LOCATION)
                    context.startActivityForResult(intent, AppConstants.PERMISSION_LOCATION)
                }
            }
        }
    }

    private fun setErrorForClient() {
        locationCallback?.error()
        locationCallback = null
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.")
            return
        }
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        context?.let {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
                    ?.addOnCompleteListener(it, { mRequestingLocationUpdates = false })
        }
    }


    interface LocationCallbackToClass {
        fun userLocation(mCurrentLocation: Location?, address: String?)
        fun error()
    }

    fun onResume() {
        if (mRequestingLocationUpdates) {
            startLocationUpdates()
        }
    }

}