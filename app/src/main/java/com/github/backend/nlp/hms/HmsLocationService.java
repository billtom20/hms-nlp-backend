package com.github.backend.nlp.hms;

import android.Manifest;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsStates;
import com.huawei.hms.location.SettingsClient;

import org.microg.nlp.api.HelperLocationBackendService;

public class HmsLocationService extends HelperLocationBackendService {
    private static final String TAG = HmsLocationService.class.getSimpleName();

    // Define a fusedLocationProviderClient object.
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    private LocationRequest mLocationRequest;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                // Process the location callback result.
                if (locationResult.getLocations().size() > 0) {
                    report(locationResult.getLocations().get(0));
                }
            }
        }
    };

    @Override
    protected synchronized Location update() {
        checkLocationSettings();

        return super.update();
    }

    @Override
    protected synchronized void onOpen() {
        super.onOpen();

        // Instantiate the fusedLocationProviderClient object.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void checkLocationSettings() {
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
                // Define the listener for success in calling the API for checking device location settings.
                .addOnSuccessListener(locationSettingsResponse -> {
                    LocationSettingsStates locationSettingsStates =
                            locationSettingsResponse.getLocationSettingsStates();
                    StringBuilder stringBuilder = new StringBuilder();
                    // Check whether the location function is enabled.
                    stringBuilder.append(",\nisLocationUsable=")
                            .append(locationSettingsStates.isLocationUsable());
                    // Check whether HMS Core (APK) is available.
                    stringBuilder.append(",\nisHMSLocationUsable=")
                            .append(locationSettingsStates.isHMSLocationUsable());
                    Log.i(TAG, "checkLocationSetting onComplete:" + stringBuilder);
                })
                // Define callback for failure in checking the device location settings.
                .addOnFailureListener(e -> Log.i(TAG, "checkLocationSetting onFailure:" + e.getMessage()));
    }

    private void requestLocationUpdates() {
        // Set the location type.
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HD_ACCURACY);
        // Set the number of location updates to 1.
        mLocationRequest.setNumUpdates(1);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(aVoid -> {
                    // TODO: Define callback for API call success.
                })
                .addOnFailureListener(e -> {
                    // TODO: Define callback for API call failure.
                });
    }

    private void removeLocationUpdates() {
        // Note: When you stop requesting location updates, mLocationCallback must be the same object as LocationCallback passed to the requestLocationUpdates method.
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                // Define callback for success in stopping requesting location updates.
                .addOnSuccessListener(aVoid -> {
                    // TODO: Define callback for success in stopping requesting location updates.
                })
                // Define callback for failure in stopping requesting location updates.
                .addOnFailureListener(e -> {
                    // TODO: Define callback for failure in stopping requesting location updates.
                });
    }

}