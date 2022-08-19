package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class NearMe_Activity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTINGS = 1001;

    SharedPreferences sp,sp1;
    String Latitude,Longitude;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        sp = getSharedPreferences("Current_lat", Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("Current_long", Context.MODE_PRIVATE);

        progressDialog = new ProgressDialog(NearMe_Activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


        if (ActivityCompat.checkSelfPermission(NearMe_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(NearMe_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }



    //turn on location...

    private void getLocation()
    {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 5, (LocationListener) NearMe_Activity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {

        Log.i("LatitudeUser", String.valueOf(location.getLatitude()));
        Log.i("LongieUser", String.valueOf(location.getLongitude()));


        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
        Log.i("LatNewLong",Latitude+"  "+Longitude);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CurrentLatitude",Latitude);
        editor.apply();


        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putString("CurrentLongitude",Longitude);
        editor1.apply();


        progressDialog.dismiss();
        Intent intent1 = new Intent(NearMe_Activity.this,MapActivity.class);
        startActivity(intent1);

        try {
            Geocoder geocoder = new Geocoder(NearMe_Activity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            Log.i("AddressMy", address);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider)
    {
        Toast.makeText(this, "Please Turn on the Location", Toast.LENGTH_SHORT).show();
        Log.i("Provid", provider);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(NearMe_Activity.this, "GPS is On...", Toast.LENGTH_SHORT).show();
                    getLocation();

                } catch (ApiException e) {
                    e.printStackTrace();
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(NearMe_Activity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;

                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //location part
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is Turned On...", Toast.LENGTH_SHORT).show();
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "Turn on location Manually ", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(NearMe_Activity.this,Explore_Activity.class);
                    startActivity(intent1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(NearMe_Activity.this,Explore_Activity.class);
        startActivity(intent1);
    }
}