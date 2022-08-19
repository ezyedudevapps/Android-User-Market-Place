package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.InstiGroupAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Institution_Groups extends AppCompatActivity{
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    InstiGroupAdapter instiGroupAdapter;
    private List<com.ezyedu.student.model.Institution_Groups> institution_groupsList = new ArrayList<>();

    ProgressDialog progressDialog;
    EditText editText;

    TextView filter;

    GridLayoutManager manager1;
    int pageArticles = 1;
    int totalItemCountArticles;
    int firstVisibleItemCountArticles;
    int visibleItemCountArticles;
    int previousTotalArticles;
    boolean loadArticles = true;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    String language = null;
    TextView allgrouptext,Institutiontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution__groups);

        //get domain url
        base_app_url = sharedData.getValue();
       // Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
     //   Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        allgrouptext = findViewById(R.id.ag_ttl);
        Institutiontext = findViewById(R.id.ins_ttl);

        filter = findViewById(R.id.fltr_btn);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Institution_Groups.this,Filter_Vendor_Groups_Activity.class);
                startActivity(intent1);
            }
        });

        ImageView imageView = findViewById(R.id.lft_arrrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Institution_Groups.this, Explore_Activity.class);
                startActivity(intent1);
            }
        });

        recyclerView = findViewById(R.id.group_recyc);
       // LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        instiGroupAdapter = new InstiGroupAdapter(Institution_Groups.this,institution_groupsList);
        recyclerView.setAdapter(instiGroupAdapter);
        manager1 = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager1);
     //   recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(Institution_Groups.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        getData();

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


        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            allgrouptext.setText("Semua Groups");
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

    private void getData()
    {
        String url = base_app_url+"api/vendor/group/with-detail?page="+pageArticles;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("JSONGroups",response.toString());
                try {
                    progressDialog.dismiss();
                    int current_page = response.getInt("current_page");
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0)
                    {
                        Log.i("jsonFiltData",jsonArray.toString());
                        for (int i = 0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String group_name = jsonObject.getString("group_name");
                            int id = jsonObject.getInt("id");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("vendor_group_detail");
                            String image = null;
                            if (!jsonObject1.isNull("vendor"))
                            {
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("vendor");
                                image = jsonObject2.getString("logo");
                            }
                            com.ezyedu.student.model.Institution_Groups post = new com.ezyedu.student.model.Institution_Groups(id,group_name,image);
                            institution_groupsList.add(post);
                            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        if (current_page == 1 && jsonArray.length() ==0)
                        {
                            Toast.makeText(Institution_Groups.this, "No data Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.i("errorGroups",e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("ErrorGroups",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
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
                    getData();
                    loadArticles = true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Institution_Groups.this,Explore_Activity.class);
        startActivity(intent1);
    }
}