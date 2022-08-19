package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.MainActivity;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.bookmarkArticlesAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.bookmark_articles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fragment_bookmark_articles extends Fragment {
    RecyclerView recyclerView;
    private RequestQueue requestQueue;
    String session_id = null;
    bookmarkArticlesAdapter adapter;
private List<bookmark_articles> bookmarkArticlesList = new ArrayList<>();
    ImageView imageView;
    TextView t1,t2;
    Button b1;
    GridLayoutManager manager;

    ProgressDialog progressDialog;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_articles, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

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

        recyclerView = view.findViewById(R.id.bm_course_recyc);
        manager = new GridLayoutManager(getContext(),2);
        adapter = new bookmarkArticlesAdapter(getContext(),bookmarkArticlesList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        t1 = view.findViewById(R.id.t11);
        t2 = view.findViewById(R.id.t12);
        imageView = view.findViewById(R.id.i11);
        b1 = view.findViewById(R.id.b11);
b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(getContext(), MainActivity.class);
        startActivity(intent1);
    }
});


        getData();
        return view;
    }

    private void getData()
    {
        String url = base_app_url+"api/bookmark";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseBkArti",response.toString());
                try {
                    progressDialog.dismiss();
                    String message = response.getString("message");
                    if (!message.equals("success"))
                    {
                        recyclerView.setVisibility(View.GONE);
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    Log.i("responseArtiBook",response.toString());
                    JSONArray jsonArray = response.getJSONArray("blogs");
                    for (int i = 0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String hash_id = jsonObject.getString("blog_id");
                        String tittle = jsonObject.getString("title");
                        String image = jsonObject.getString("image");
                        bookmark_articles post = new bookmark_articles(hash_id,tittle,image);
                        bookmarkArticlesList.add(post);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                    if (bookmarkArticlesList.size()==0)
                    {
                        recyclerView.setVisibility(View.GONE);
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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