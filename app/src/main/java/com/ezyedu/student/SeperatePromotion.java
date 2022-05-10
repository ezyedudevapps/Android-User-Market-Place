package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.SeperatePromotionAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Promo_one_new;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeperatePromotion extends AppCompatActivity {

    RecyclerView recyclerView;
    private String Hashid;
    private RequestQueue requestQueue;
    SeperatePromotionAdapter seperatePromotionAdapter;
    private List<Promo_one_new> plist = new ArrayList<>();
    ProgressDialog LoadingBar;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seperate_promotion);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        Hashid = getIntent().getStringExtra("id");

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();




        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Please Wait");
        LoadingBar.setMessage("Loading Promotion Details");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();


        recyclerView = findViewById(R.id.reperate_promotion_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(SeperatePromotion.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        seperatePromotionAdapter = new SeperatePromotionAdapter(SeperatePromotion.this,plist);
        recyclerView.setAdapter(seperatePromotionAdapter);
        fetchPromo();

    }

    private void fetchPromo()
    {
        String base = base_app_url+"api/event/";
        String url = base+Hashid;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    LoadingBar.dismiss();
                    JSONArray jsonArray = response.getJSONArray("event_images");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject = jsonObject1.getJSONObject("image_url");
                    String head_img = jsonObject.getString("original");
                    String tittle = response.getString("title");
                    Log.i("tittle_new",tittle);
                    String description = response.getString("description");
                    String start_at = response.getString("start_at");
                    String finish_at = response.getString("finish_at");
                    JSONObject jsonObject2 = response.getJSONObject("vendor");
                    int vendor_id = jsonObject2.getInt("id");
                    String name = jsonObject2.getString("name");
                    String address = jsonObject2.getString("address");
                    String logo = jsonObject2.getString("logo");
                    JSONObject jsonObject3 = jsonObject2.getJSONObject("logo_url");
                  //  String logo = jsonObject3.getString("small");

                    JSONArray jsonArray1 = response.getJSONArray("event_images");
                    JSONObject jsonObject4 = jsonArray1.getJSONObject(0);
                    String image = jsonObject4.getString("image");


                    Promo_one_new post = new Promo_one_new(
                            image,tittle,"00",logo,name,address,3.0,description,start_at,finish_at,vendor_id);
                    plist.add(post);
                    Log.i("Array_list_promo", String.valueOf(plist.size()));

                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LoadingBar.dismiss();
                Toast.makeText(SeperatePromotion.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}