package com.ezyedu.student.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.SeperateInstitution;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class BottomSheet extends BottomSheetDialogFragment {
    private BottomSheetDialog.BottomSheetListner bottomSheetListner;
    SharedPreferences sharedPreferences;
    String vendor_id = null;
    RequestQueue requestQueue;
    Button button;

    TextView tittle,addres,rating;
    ImageView logo;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("marker_details", Context.MODE_PRIVATE);
        vendor_id = sharedPreferences.getString("marker_id","");
        Log.i("vendor_id_marker",vendor_id);



        if(!TextUtils.isEmpty(vendor_id))
        {
            getVenDetails(vendor_id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_bottom_sheet,container,false);

        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();

        requestQueue = Volley.newRequestQueue(getContext());

        tittle = view.findViewById(R.id.map_ven_ttl);
        addres = view.findViewById(R.id.map_ven_cat);
        rating = view.findViewById(R.id.new_rateing);
        logo = view.findViewById(R.id.map_ven_logo);

        button = view.findViewById(R.id.button_cntinue);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeperateInstitution.class);
                intent.putExtra("ven_id",vendor_id);
                startActivity(intent);
            }
        });
        return view;
    }


    private void getVenDetails(String vendor_id)
    {
        String url = base_app_url+"api/vendor/"+vendor_id+"/details";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseMapMark",response.toString());

                try {
                    String name = response.getString("vendor_name");
                    String address = response.getString("vendor_address");
                    String vendor_logo = response.getString("vendor_logo");
                    int vendor_rating = response.getInt("vendor_rating");

                    tittle.setText(name);
                    addres.setText(address);
                    if (vendor_rating !=0)
                    {
                        rating.setText(String.valueOf(vendor_rating));
                    }
                    Glide.with(getContext()).load(img_url_base+vendor_logo).into(logo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }




}
