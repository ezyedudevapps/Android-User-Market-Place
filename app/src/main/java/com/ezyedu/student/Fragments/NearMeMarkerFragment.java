package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.NearMe_Activity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NearMeMarkerFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    RequestQueue requestQueue;


   //  String Latitude = "-5.900290513073116";
     //String Longitude = "107.6744619011879";

    String Latitude = null;
    String Longitude = null;




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
    SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);


        sharedPreferences1 = getContext().getSharedPreferences("Current_lat", Context.MODE_PRIVATE);
       Latitude = sharedPreferences1.getString("CurrentLatitude","");
        Log.i("Session_Lat",Latitude);

        sharedPreferences2 = getContext().getSharedPreferences("Current_long", Context.MODE_PRIVATE);
        Longitude = sharedPreferences2.getString("CurrentLongitude","");
        Log.i("Session_Long",Longitude);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_near_me_marker, container, false);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.google_map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        requestQueue= Volley.newRequestQueue(getContext());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        // Add a marker in Sydney and move the camera
        String url = base_app_url+"api/vendor/nearme/all";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lat",Latitude);
            jsonObject.put("lon",Longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("LatCheck",Latitude);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("ResponseNewMap",response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String Latitude = jsonObject1.getString("latitude");
                            String Longitude = jsonObject1.getString("longitude");
                            String name = jsonObject1.getString("vendor_name");

                            double lat = Double.parseDouble(Latitude);
                            double lon = Double.parseDouble(Longitude);
                            LatLng value = new LatLng(lat,lon);
                            arrayList.add(value);

                            Maps post = new Maps(lat,lon,name);
                            mapsList.add(post);
                            Log.i("MapListSize", String.valueOf(mapsList.size()));

                            mMap = googleMap;
                            for (int j = 0; j<arrayList.size();j++) {
                                mMap.addMarker(new MarkerOptions().position(arrayList.get(j)).title(name));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                                float zoomLevel = 11.0f;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(j),zoomLevel));
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
                    else
                    {
                        Toast.makeText(getContext(), "No Data Found....", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Log.i("ErrorMapsLatLong",error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}