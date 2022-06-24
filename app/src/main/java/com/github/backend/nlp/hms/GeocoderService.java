package com.github.backend.nlp.hms;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.microg.nlp.api.GeocoderBackendService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocoderService extends GeocoderBackendService {
    private static final String TAG = GeocoderService.class.getSimpleName();

    @Override
    protected List<Address> getFromLocation(double latitude, double longitude, int maxResults, String locale) {
        List<Address> addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this, getLocale(locale));
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, maxResults);
        } catch (IOException e) {
            Log.i(TAG, e.getMessage(), e);
        }
        return addressList;
    }

    @Override
    protected List<Address> getFromLocationName(String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, String locale) {
        List<Address> addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this, getLocale(locale));
        try {
            addressList = geocoder.getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);
        } catch (IOException e) {
            Log.i(TAG, e.getMessage(), e);
        }
        return addressList;
    }

    private Locale getLocale(String locale) {
        String[] temp = locale.split("_");
        if (temp.length == 2) {
            return new Locale(temp[0], temp[1]);
        }
        return Locale.getDefault();
    }
}
