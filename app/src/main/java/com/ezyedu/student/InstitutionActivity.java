package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.Insti_Exp_adapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.instituExpAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InstitutionActivity extends AppCompatActivity {

    Insti_Exp_adapter insti_exp_adapter;
    RecyclerView recyclerView;
    private List<instituExpAll> instituExpAllList = new ArrayList<>();
    RequestQueue requestQueue;
    int vendor_id;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        vendor_id = getIntent().getIntExtra("ven_id",0);
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.insti_all_recyc);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        insti_exp_adapter = new Insti_Exp_adapter(InstitutionActivity.this,instituExpAllList);
        recyclerView.setAdapter(insti_exp_adapter);
        fetchinstitutions();

    }

    private void fetchinstitutions()
    {
        String insti_url = base_app_url+"api/vendor/group/"+vendor_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, insti_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("vendor_details");

                    for (int b = 0; b<jsonArray.length();b++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(b);
                        //new
                        String group_name = jsonObject.getString("group_name");
                        int vendor_id = jsonObject.getInt("id");
                        String venor_name = jsonObject.getString("name");
                        String vendor_logo = jsonObject.getString("logo");
                        String address = jsonObject.getString("address");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("vendor_reviews");
                        int total_review = jsonObject1.getInt("total_review");
                        instituExpAll post = new instituExpAll( group_name,
                                vendor_id,
                                venor_name,vendor_logo,
                                address,total_review);
                        instituExpAllList.add(post);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
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