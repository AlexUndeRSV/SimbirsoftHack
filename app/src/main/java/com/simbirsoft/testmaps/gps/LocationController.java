package com.simbirsoft.testmaps.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


public class LocationController {
    private Context context;
    private OnLocationChangedListener listener;
    private LatLng lastLocation = null;
    private boolean isStarted = false;

    public LocationController(Context context) {
        this.context = context;
    }

    private void prepare() {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude());
                if (listener != null) {
                    listener.onLocationChanged(lastLocation);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
            isStarted = true;
        }
    }

    public void setLocationListener(OnLocationChangedListener listener) {
        this.listener = listener;
        if (!isStarted) {
            prepare();
        }
    }

    /**
     * Возвращает текущее местоположение
     * @return текущее местоположение
     */
    public LatLng getCurrentLocation() {
        return lastLocation;
    }


    public interface OnLocationChangedListener {
        void onLocationChanged(LatLng location);
    }
}