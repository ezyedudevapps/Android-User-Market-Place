package com.ezyedu.student.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.Explore_Activity;
import com.ezyedu.student.MapActivity;
import com.ezyedu.student.NearMe_Activity;
import com.ezyedu.student.Settings_Activity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Maps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class NearMeMarkerFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    RequestQueue requestQueue;


    //  String Latitude = "-5.900290513073116";
    //String Longitude = "107.6744619011879";

    String Latitude = null;
    String Longitude = null;

    Double latitude;
    Double longitude;

    Button search_vendor;

    double lat;
    double lon;

    FusedLocationProviderClient client;


    ProgressDialog progressDialog;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    String session_id = null;
    private List<Maps> mapsList = new ArrayList<>();
    ArrayList<LatLng> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2;

    SearchView searchView;
    ImageView clicon;

    int code = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val", "");
        Log.i("Session_Histry_activity", session_id);


        sharedPreferences1 = getContext().getSharedPreferences("Current_lat", Context.MODE_PRIVATE);
        Latitude = sharedPreferences1.getString("CurrentLatitude", "");
        Log.i("Session_Lat", Latitude);

        sharedPreferences2 = getContext().getSharedPreferences("Current_long", Context.MODE_PRIVATE);
        Longitude = sharedPreferences2.getString("CurrentLongitude", "");
        Log.i("Session_Long", Longitude);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            code = 1;
            getCurrentLocation();
          //  mMap.clear();
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_me_marker, container, false);


        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        //    Log.i("img_url_global",img_url_base);

        search_vendor = view.findViewById(R.id.search_ven_map);
        searchView = view.findViewById(R.id.sv_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addresses = null;
                if (!location.equals("")) {
                    mMap.clear();
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addresses = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size()>0)
                    {
                        Address address = addresses.get(0);
                        if (address != null)
                        {
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            lat = address.getLatitude();
                            lon = address.getLongitude();
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            nearMe(lat,lon);
                        }
                    }

                    else
                    {
                        Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        clicon = view.findViewById(R.id.my_loc_icon);
        clicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 100);
                }


            }
        });
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);






        requestQueue = Volley.newRequestQueue(getContext());


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Log.i("FastLatLon", String.valueOf(lat + " " + lon));
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                    //    progressDialog = new ProgressDialog(getContext());
                      //  progressDialog.show();
                       // progressDialog.setContentView(R.layout.progress_dialog);
                       // progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

                    //    nearMe(lat,lon);


                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                lat = location1.getLatitude();
                                lon = location1.getLongitude();
                                Log.i("FastLatLon", String.valueOf(lat + " " + lon));
                                LatLng latLng = new LatLng(location1.getLatitude(), location1.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(latLng).title("You are Here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                progressDialog = new ProgressDialog(getContext());
                                progressDialog.show();
                                progressDialog.setContentView(R.layout.progress_dialog);
                                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                if (code == 0)
                                {
                                    nearMe(lat,lon);
                                }

                            }
                        };
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });

        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void nearMe(double lat, double lon)
    {
        String url = base_app_url + "api/vendor/nearme/all";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat", lat);
            jsonObject.put("lon", lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("LatCheck", Latitude);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("ResponseNewMap", response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String Latitude = jsonObject1.getString("latitude");
                            String Longitude = jsonObject1.getString("longitude");
                            String name = jsonObject1.getString("vendor_name");

                            double lat = Double.parseDouble(Latitude);
                            double lon = Double.parseDouble(Longitude);
                            LatLng value = new LatLng(lat, lon);
                            arrayList.add(value);

                            Maps post = new Maps(name);
                            mapsList.add(post);
                            Log.i("MapListSize", String.valueOf(mapsList.size()));


                            for (int j = 0; j < arrayList.size(); j++) {
                                mMap.addMarker(new MarkerOptions().position(arrayList.get(j)).title(name));
                             /*   mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                float zoomLevel = 11.0f;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(j), zoomLevel));

                              */
                            }


                        }
                        Log.i("MapOutlist", String.valueOf(mapsList.size()));

                   /*     mMap = googleMap;
                        for (int j = 0; j<arrayList.size();j++) {
                            mMap.addMarker(new MarkerOptions().position(arrayList.get(j)).title("EzyEdu"));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(25.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(j)));
                        }
                    */

                    }
                    //    04343 265785
                    else {
                        Toast.makeText(getContext(), "No Data Found....", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("ErrorMapsLatLong", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = mMap.getCameraPosition().target;
                Log.i("ContentData", String.valueOf(center));
                latitude = center.latitude;
                longitude = center.longitude;

                Latitude = String.valueOf(latitude);
                Longitude = String.valueOf(longitude);
                search_vendor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.clear();
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        nearMe(latitude,longitude);
                    }
                });
            }
        });
    }
}