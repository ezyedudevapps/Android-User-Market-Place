package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.adapter.VendorFacilityAdapter;
import com.ezyedu.student.adapter.VendorInfo1Adapter;
import com.ezyedu.student.model.VendorFacilities;
import com.ezyedu.student.model.VendorInfo1;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.VendorInfoAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.VendorInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class fragment_insti_1 extends Fragment implements OnMapReadyCallback
{
    SharedPreferences ven_id_shared;
   public String ven_id;
   RecyclerView recyclerView,recyclerView1,recyclerView2;
   VendorInfoAdapter vendorInfoAdapter;
   VendorInfo1Adapter vendorInfo1Adapter;
   VendorFacilityAdapter vendorFacilityAdapter;
   private RequestQueue requestQueue;
   private List<VendorInfo> vendorInfoList = new ArrayList<>();
   private List<VendorInfo1> vendorInfo1List = new ArrayList<>();
   private List<VendorFacilities> vendorFacilitiesList = new ArrayList<>();

   private String tag = "nullmain";
   TextView textView;

    private GoogleMap mMap;

    TextView  locationtext;
    String language = null;
    String ven_detail_url;


    ProgressDialog progressDialog;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String instagram,facebook,twitter,youtube,tiktok;

 Button relativeLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //new connection #vishnu
        ven_id_shared = getContext().getSharedPreferences("Vendor_id",Context.MODE_PRIVATE);
        ven_id = ven_id_shared.getString("ven","");
        Log.i("ven_get_id",ven_id.toString());
        String detail_base = base_app_url+"api/vendor/";
        String end = "/details";
        ven_detail_url = detail_base+ven_id+end;
        Log.i("ven_details_full_url",ven_detail_url);
     //   fetchData(ven_detail_url);


        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);
        if (language.equals("Indonesia"))
        {
            locationtext.setText("Lokasi Pinpoint");
            relativeLayout.setText("Lihat dipeta");
        }


    }

 /*   private void fetchData(String ven_detail_url)
    {

    }

  */


    @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

            // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_insti_1, container, false);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        locationtext = view.findViewById(R.id.loc_txt);
        relativeLayout = view.findViewById(R.id.opn_map);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        requestQueue= Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.frag_insti1_recyc);
        vendorInfoAdapter = new VendorInfoAdapter(getContext(),vendorInfoList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(vendorInfoAdapter);

        recyclerView1 = view.findViewById(R.id.ven_cat_desc_recyc);
        vendorInfo1Adapter = new VendorInfo1Adapter(getContext(),vendorInfo1List);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(vendorInfo1Adapter);

        textView = view.findViewById(R.id.text_facility);
        recyclerView2 = view.findViewById(R.id.ven_facility_recyc);
        vendorFacilityAdapter = new VendorFacilityAdapter(getContext(),vendorFacilitiesList);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(vendorFacilityAdapter);

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
    public void onMapReady(GoogleMap googleMap) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ven_detail_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                Log.i("venbasicdtl",response.toString());
                try {
                    Integer vendor_chat_id = response.getInt("vendor_id");
                    Integer is_chatting_allowed = response.getInt("is_chatting_allowed");
                    String vendor_hash_id = response.getString("vendor_hash_id");
                    String vendor_name =response.getString("vendor_name");
                    String vendor_address =response.getString("vendor_address");
                    String vendor_logo =response.getString("vendor_logo");
                    String vendor_email=response.getString("vendor_email");
                    String vendor_phone=response.getString("vendor_phone");
                    String website=response.getString("website");
                    String vendor_image=response.getString("vendor_image");
                    String vendor_rating=response.getString("vendor_rating");
                    JSONArray jsonArray = response.getJSONArray("operatings");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String opt = jsonObject.getString("open")+" - "+jsonObject.getString("close");

                    String latitude = response.getString("latitude");
                    String longitude = response.getString("longitude");


                    JSONArray jsonArray1 = response.getJSONArray("social_medias");

                    for (int i = 0;i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        int id = jsonObject1.getInt("id");
                        if (id == 1)
                        {
                            facebook = jsonObject1.getString("url");
                        }
                        else if (id == 4)
                        {
                            instagram = jsonObject1.getString("url");
                        }
                        else if (id == 6)
                        {
                            youtube = jsonObject1.getString("url");
                        }
                       else if (id == 7)
                        {
                            twitter = jsonObject1.getString("url");
                        }
                       else if (id == 10 || id == 9)
                        {
                            tiktok = jsonObject1.getString("url");
                        }

                    }

                    JSONArray jsonArray2 = response.getJSONArray("operatings");
                    JSONObject jsonObject9 = jsonArray2.getJSONObject(0);
                    int monday = jsonObject9.getInt("monday");
                    int tuesday = jsonObject9.getInt("tuesday");
                    int wednesday = jsonObject9.getInt("wednesday");
                    int thursday = jsonObject9.getInt("thursday");
                    int friday = jsonObject9.getInt("friday");
                    int saturday = jsonObject9.getInt("saturday");
                    int sunday = jsonObject9.getInt("sunday");

                   Double latii = Double.parseDouble(latitude);
                    Double longii = Double.parseDouble(longitude);

                    if (latii != null)
                    {
                        LatLng latLng = new LatLng(latii,longii);
                        mMap = googleMap;
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                        float zoomLevel = 11.0f;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
                    }

                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (latii!= null && longii != null)
                            {
                                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                                intent1.setData(Uri.parse("geo:"+latii+","+longii));
                                Intent chooser = Intent.createChooser(intent1,"Launch Maps");
                                startActivity(chooser);
                            }
                        }
                    });


                    VendorInfo post = new VendorInfo(vendor_chat_id,vendor_hash_id,vendor_name,vendor_address,vendor_logo,vendor_email,
                            vendor_phone,website,vendor_image,vendor_rating,is_chatting_allowed,opt,instagram,facebook,twitter,youtube,tiktok,
                    monday,tuesday,wednesday,thursday,friday,saturday,sunday);
                    vendorInfoList.add(post);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

                    JSONArray jsonArray3 = response.getJSONArray("vendor_category_descriptions");
                    for (int i = 0;i<jsonArray3.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray3.getJSONObject(i);
                        String titttle = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        VendorInfo1 post1 = new VendorInfo1(titttle,content);
                        vendorInfo1List.add(post1);
                        recyclerView1.getAdapter().notifyDataSetChanged();
                    }

                    JSONArray jsonArray4 = response.getJSONArray("vendor_facilities");
                    if (jsonArray4.length() >0)
                    {
                        textView.setText("Facilities");
                    }
                    for (int i = 0; i<jsonArray4.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray4.getJSONObject(i);
                        String tittle = jsonObject1.getString("title");
                        String image = jsonObject1.getString("image");
                        VendorFacilities post2 = new VendorFacilities(tittle,image);
                        vendorFacilitiesList.add(post2);
                        recyclerView2.getAdapter().notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}