package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.FragmentPromotionAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.fragmentPromotion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_promos extends Fragment {

    RecyclerView recyclerView;
    FragmentPromotionAdapter fragmentPromotionAdapter;
    private RequestQueue requestQueue;
    private List<fragmentPromotion> fplist = new ArrayList<>();
    private Context context;//not initiallized

    ImageView imageView;
    TextView textView;

    ProgressDialog progressDialog;
    private String Tag = "abcd";


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestQueue= Volley.newRequestQueue(getContext());




        View view = inflater.inflate(R.layout.fragment_promos, container, false);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        textView = view.findViewById(R.id.t11);
        imageView = view.findViewById(R.id.i11);

        recyclerView = view.findViewById(R.id.promo_fragment);
        fragmentPromotionAdapter = new FragmentPromotionAdapter(getContext(),fplist);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(fragmentPromotionAdapter);
        fetchPromotion();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


        return view;



    }

    private void fetchPromotion()
    {
        String url = base_app_url+"api/event";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    progressDialog.dismiss();
                    if (jsonArray.length() == 0)
                    {
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                    for(int a = 0;a<jsonArray.length();a++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        String tittle = jsonObject.getString("title");
                        Log.i("pr_tt",tittle);
                        String hashid = jsonObject.getString("hash_id");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("event_images");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        String image = jsonObject1.getString("image");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("image_url");
                      //  String image = jsonObject2.getString("small");
                        //Log.i("pr_ig",image);

                        fragmentPromotion post = new fragmentPromotion(tittle,image,hashid);
                        fplist.add(post);

                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(Tag,e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}