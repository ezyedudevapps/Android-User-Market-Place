package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.ArticleSeperateAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.articesSeperate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article_details_activity extends AppCompatActivity
{


    RecyclerView recyclerView;
    ArticleSeperateAdapter articleSeperateAdapter;
    RequestQueue requestQueue;
    private List<articesSeperate> articesSeperateList = new ArrayList<>();
    private String Hashid;
    String session_id = null;
    ProgressDialog progressDialog;



    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details_activity);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);




        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_chat_activity",session_id);


        Hashid = getIntent().getStringExtra("id");



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.recycler_seperate_articles);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        articleSeperateAdapter = new ArticleSeperateAdapter(Article_details_activity.this,articesSeperateList);
        recyclerView.setAdapter(articleSeperateAdapter);

        if (!TextUtils.isEmpty(session_id))
        {
            Log.i("sepAAtritest","session");
            fetchSessionArticles(Hashid);

        }
        else
        {
            Log.i("sepAAtritest","empty");
            fetchArticles(Hashid);
        }

        progressDialog = new ProgressDialog(Article_details_activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    private void fetchSessionArticles(String hashid)
    {
        String base = base_app_url+"api/blog/";
        String url = base+hashid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("sepArtRes",response.toString());
                    progressDialog.dismiss();
                    String header = response.getString("title");
                    String image = response.getString("image");
                    String description = response.getString("content");
                    String date = response.getString("created_at");
                    JSONObject jsonObject = response.getJSONObject("image_url");
                    String bookmark = response.getString("bookmark");
                    //  String image = jsonObject.getString("original");
                    JSONObject jsonObject1 = response.getJSONObject("user");
                    String name = jsonObject1.getString("name");
                    String Hash_id = jsonObject1.getString("hash_id");
                    JSONObject jsonObject2 = response.getJSONObject("blog_category");
                    String label = jsonObject2.getString("label");

                    articesSeperate post = new articesSeperate(image,"type",header,name,date,description,hashid,label,bookmark);
                    articesSeperateList.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("ArtiSepErr",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("LoginErrorResult",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("LoginFail", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        //  Toast.makeText(Login_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        })
        {
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


    private void fetchArticles(String hashid)
    {
        String base = base_app_url+"api/blog/";
        String url = base+hashid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    progressDialog.dismiss();
                    String header = response.getString("title");
                    String image = response.getString("image");
                    String description = response.getString("content");
                    String date = response.getString("created_at");
                    JSONObject jsonObject = response.getJSONObject("image_url");
                    String bookmark = response.getString("bookmark");
                    //  String image = jsonObject.getString("original");
                    JSONObject jsonObject1 = response.getJSONObject("user");
                    String name = jsonObject1.getString("name");
                    String Hash_id = jsonObject1.getString("hash_id");
                    JSONObject jsonObject2 = response.getJSONObject("blog_category");
                    String label = jsonObject2.getString("label");

                    articesSeperate post = new articesSeperate(image,"type",header,name,date,description,hashid,label,"unautherized");
                    articesSeperateList.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();

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

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Article_details_activity.this,MainActivity.class);
        startActivity(intent1);
    }
}