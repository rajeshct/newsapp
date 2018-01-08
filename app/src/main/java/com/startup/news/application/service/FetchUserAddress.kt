package com.startup.news.application.service

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.ResultReceiver
import com.google.android.gms.maps.model.LatLng
import com.startup.news.application.constants.AppConstants
import org.jetbrains.annotations.Nullable
import java.io.IOException
import java.util.*

/**
 * Created by admin on 1/2/2018.
 */
open class FetchUserAddress : IntentService(FetchUserAddress::class.java.simpleName) {
    private var mReceiver: ResultReceiver? = null

    override fun onHandleIntent(@Nullable intent: Intent) {
        val geoCoder = Geocoder(this, Locale.getDefault());
        // Get the location passed to this service through an extra.
        // Get the location passed to this service through an extra.
        val location: LatLng = intent.getParcelableExtra(AppConstants.LOCATION_DATA_EXTRA)
        mReceiver = intent.getParcelableExtra(AppConstants.RECEIVER)

        var addresses: List<Address>? = null
        try {
            addresses = geoCoder.getFromLocation(location.latitude, location.longitude,
                    // In this sample, get just a single address.
                    1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
        } catch (exp: IllegalArgumentException) {

        }
        // Handle case where no address was found.
        if (addresses != null && !addresses.isEmpty()) {
            for (temp in addresses) {
                if (temp.countryCode != null && !temp.countryCode.isBlank()) {
                    if (temp.countryCode != null && !temp.countryCode.isBlank()) {
                        deliverResultToReceiver(temp.countryCode)
                        break
                    }
                }
            }
        }
    }

    private fun deliverResultToReceiver(message: String) {
        val bundle = Bundle()
        bundle.putString(AppConstants.ADDRESS, message)
        mReceiver?.send(AppConstants.SUCCESS_RESULT, bundle)
    }

}