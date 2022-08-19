package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.InstiGroupAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Institution_Groups;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Filtered_Institution_Groups extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    InstiGroupAdapter instiGroupAdapter;
    private List<Institution_Groups> institution_groupsList = new ArrayList<>();

    ProgressDialog progressDialog;
    EditText editText;

    TextView filter;
    TextView textView;


    int category_hash_id;
    int sub_category_id;
    String sort_val = null;
    int city_id;
    int province_id;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String url;

    GridLayoutManager manager1;
    int pageArticles = 1;
    int totalItemCountArticles;
    int firstVisibleItemCountArticles;
    int visibleItemCountArticles;
    int previousTotalArticles;
    boolean loadArticles = true;

    String language = null;
    TextView Institutiontext;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered__institution__groups);

        category_hash_id = getIntent().getIntExtra("category_hash_id",0);
        sub_category_id = getIntent().getIntExtra("sub_category_id",0);
        sort_val = getIntent().getStringExtra("sort_val");
        city_id = getIntent().getIntExtra("city_id",0);
        province_id = getIntent().getIntExtra("province_id",0);

        textView = findViewById(R.id.filter_text);
        if (getIntent().getStringExtra("categoryy") != null)
        {
            textView.setText("Filtered By "+getIntent().getStringExtra("categoryy"));
        }
        if (sort_val.equals("Sort by Group Name"))
        {
            sort_val = "ASC";
        }
        Log.i("FilteredValues", category_hash_id +" "+ sort_val +" "+String.valueOf(city_id)
                +" "+String.valueOf(province_id));

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.group_recyc);

        // LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        instiGroupAdapter = new InstiGroupAdapter(Filtered_Institution_Groups.this,institution_groupsList);
        recyclerView.setAdapter(instiGroupAdapter);
        manager1 = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager1);
        //   recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(Filtered_Institution_Groups.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        ImageView imageView = findViewById(R.id.lft_arrrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Filtered_Institution_Groups.this, Explore_Activity.class);
                startActivity(intent1);
            }
        });


        url = base_app_url+"api/vendor/group/with-detail?";

         if (!sort_val.equals("Default Sort") & category_hash_id !=0)
         {
             url = url+"sort[group_name]="+sort_val+"&"+"category_id="+category_hash_id;
             filteredData();
         }
         else if (!sort_val.equals("Default Sort"))
         {
             url = url+"sort[group_name]="+sort_val;
             filteredData();
         }
         else if (category_hash_id !=0)
         {
             url = url +"category_id="+category_hash_id;
             filteredData();
         }
         else
         {
             filteredData();
         }

         Log.i("fiteringUrl",url);



        filter = findViewById(R.id.fltr_btn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Filtered_Institution_Groups.this, com.ezyedu.student.Institution_Groups.class);
                startActivity(intent1);
            }
        });



        //search vendor groups


        //Search courses
        editText = findViewById(R.id.search_course_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter(s.toString());
            }
        });

        Institutiontext = findViewById(R.id.ins_ttl);
        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            filter.setText("Reset Filters");
            Institutiontext.setText("Institusi");

        }

    }

    //method for search
    private void filter(String text)
    {
        ArrayList<com.ezyedu.student.model.Institution_Groups> filteredlist = new ArrayList<>();

        for (com.ezyedu.student.model.Institution_Groups item : institution_groupsList)
        {
            if (item.getGroup_name().toLowerCase().contains(text.toLowerCase()))
            {
                filteredlist.add(item);
            }
        }
        instiGroupAdapter.filterList(filteredlist);

    }
    private void filteredData()
    {
        if (sub_category_id != 0)
        {
            url = url+"&subcategory_ids="+sub_category_id;
        }
       /* else
        {
            url = url+"&subcategory_ids=others";
        }

        */
        url = url +"&page="+pageArticles;
        Log.i("filterUrl",url);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    int current_page = response.getInt("current_page");
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        Log.i("filterOnly",jsonArray.toString());
                        for (int i = 0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String group_name = jsonObject.getString("group_name");
                            int id = jsonObject.getInt("id");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("vendor_group_detail");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("vendor");
                            String image = jsonObject2.getString("logo");
                            com.ezyedu.student.model.Institution_Groups post = new com.ezyedu.student.model.Institution_Groups(id,group_name,image);
                            institution_groupsList.add(post);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        if (current_page == 1 && jsonArray.length() ==0)
                        {
                            Toast.makeText(Filtered_Institution_Groups.this, "No data Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonArrayRequest);
        paginationArticles();
    }

    private void paginationArticles()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemCountArticles = manager1.findFirstVisibleItemPosition();
                totalItemCountArticles = manager1.getItemCount();
                visibleItemCountArticles = manager1.getChildCount();

                if (loadArticles)
                {
                    if (totalItemCountArticles > previousTotalArticles)
                    {
                        previousTotalArticles = totalItemCountArticles;
                        pageArticles++;
                        loadArticles = false;
                    }
                }
                if (!loadArticles && (firstVisibleItemCountArticles+visibleItemCountArticles) >= totalItemCountArticles)
                {
                    filteredData();
                    loadArticles = true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Filtered_Institution_Groups.this,Explore_Activity.class);
        startActivity(intent1);
    }
}