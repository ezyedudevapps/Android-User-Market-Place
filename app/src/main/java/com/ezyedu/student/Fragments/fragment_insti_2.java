package com.ezyedu.student.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.VendorProductsAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.VendorProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fragment_insti_2 extends Fragment {


    TextView textView;
    SharedPreferences vendor_id;
    public String ven_id;
    RecyclerView recyclerView;
    VendorProductsAdapter vendorProductsAdapter;
    private List<VendorProducts> vendorProductsList = new ArrayList<>();
    RequestQueue requestQueue;
    private String TAG = "Fragment2";

    TextView t1;
    ImageView i1;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;
    String language = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vendor_id = getContext().getSharedPreferences("Vendor_id", Context.MODE_PRIVATE);
        ven_id = vendor_id.getString("ven","");
        String base = base_app_url+"api/vendor/";
        String end = "/courses";
        //test  String ven_new = "115";
        String url = base+ven_id+end;
        Log.i("url_frag2",url);


        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            t1.setText("Tidak Ada Produk Ditemukan");
        }


       fetchData(url);
            }

    private void fetchData(String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("jsonfrag2",jsonArray.toString());
                    if (jsonArray.length()==0)
                    {
                        i1.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.VISIBLE);
                    }
                    for(int i =0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        Log.i("id_frag2", Integer.toString(id));
                        String course_title = jsonObject.getString("course_title");
                        String course_hash_id = jsonObject.getString("course_hash_id");
                        String course_duration;
                        if (jsonObject.isNull("course_duration"))
                        {
                            course_duration = "null";
                        }
                        else
                        {
                            course_duration =  jsonObject.getString("course_duration");
                        }
                        String start_date = jsonObject.getString("start_date");
                        String institution = jsonObject.getString("institution");
                        String courses_image = jsonObject.getString("courses_image");
                        int category_id = jsonObject.getInt("category_id");
                        String category_label = jsonObject.getString("category_label");

                        VendorProducts post = new VendorProducts(id,course_title,course_hash_id,course_duration
                                ,start_date,institution,courses_image,category_id,category_label);
                        vendorProductsList.add(post);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG,e.toString());
                    Log.i("valfrag2",e.toString());
                    i1.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("errfrag2",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_insti_2, container, false);

        requestQueue= Volley.newRequestQueue(getContext());

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        i1 = view.findViewById(R.id.i11);
        t1 = view.findViewById(R.id.t11);

        textView = view.findViewById(R.id.frag2_error);
        recyclerView = view.findViewById(R.id.frag_2_recyc);
        vendorProductsAdapter = new VendorProductsAdapter(getContext(),vendorProductsList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(vendorProductsAdapter);
        return view;
    }
}