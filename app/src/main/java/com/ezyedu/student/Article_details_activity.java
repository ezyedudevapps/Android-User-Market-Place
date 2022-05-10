package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

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
import java.util.List;

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




        Hashid = getIntent().getStringExtra("id");



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.recycler_seperate_articles);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        articleSeperateAdapter = new ArticleSeperateAdapter(Article_details_activity.this,articesSeperateList);
        recyclerView.setAdapter(articleSeperateAdapter);
        fetchArticles(Hashid);

        progressDialog = new ProgressDialog(Article_details_activity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
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
                  //  String image = jsonObject.getString("original");
                    JSONObject jsonObject1 = response.getJSONObject("user");
                    String name = jsonObject1.getString("name");
                    String Hash_id = jsonObject1.getString("hash_id");


                    articesSeperate post = new articesSeperate(image,"type",header,name,date,description,hashid);
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
}