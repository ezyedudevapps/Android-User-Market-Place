package com.ezyedu.student.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ezyedu.student.adapter.InstiFeedAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.InstitutionFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class fragment_insti_3 extends Fragment {

    SharedPreferences sharedPreferences;
    private String Hashid;
    String Vendor_id;
    private RequestQueue requestQueue;
    private String HTAG = "fragment";
    TextView textView;
    TextView t1;
    ImageView i1;

    RecyclerView recyclerView;
    InstiFeedAdapter instiFeedAdapter;
    private List<InstitutionFeed> institutionFeedList = new ArrayList<>();

    GridLayoutManager manager1;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String language = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("Vendor_id",Context.MODE_PRIVATE);
        Vendor_id = sharedPreferences.getString("ven","");
        String base = "https://dev-api.ezy-edu.com/api/vendor/";
        String end = "/blogs";
        String new_ven = "115";
        String url = base +Vendor_id+end;
        Log.i("url_frag_3",url);

        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            t1.setText("Tidak Ada Feed Ditemukan");
        }
        fetchData(url);

    }

    private void fetchData(String url)
    {
        String a = base_app_url+"api/ideas?vendor_id="+Vendor_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, a, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("resArray",jsonArray.toString());
                    if (jsonArray.length()==0)
                    {
                        i1.setVisibility(View.VISIBLE);
                        t1.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("title");
                        String blog_date = jsonObject1.getString("created_at");
                        String hash_id = jsonObject1.getString("hash_id");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("ideas_images");
                        JSONObject jsonObject = jsonArray1.getJSONObject(0);
                        String blog_image = jsonObject.getString("image");


                        InstitutionFeed post = new InstitutionFeed(blog_image,title,blog_date,hash_id);
                        institutionFeedList.add(post);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error_frag3",e.toString());
                    textView.setText("No Feed Found");
                    i1.setVisibility(View.VISIBLE);
                    t1.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insti_3, container, false);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        i1 = view.findViewById(R.id.i11);
        t1 = view.findViewById(R.id.t11);

        textView = view.findViewById(R.id.frag3_error);
        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView = view.findViewById(R.id.recyc_feed);
        instiFeedAdapter = new InstiFeedAdapter(getContext(),institutionFeedList);
     //   LinearLayoutManager manager = new LinearLayoutManager(getContext());

        manager1 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager1);
        recyclerView.setAdapter(instiFeedAdapter);
        return view;
    }
}