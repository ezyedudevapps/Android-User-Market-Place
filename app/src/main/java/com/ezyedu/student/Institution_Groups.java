package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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

public class Institution_Groups extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    InstiGroupAdapter instiGroupAdapter;
    private List<com.ezyedu.student.model.Institution_Groups> institution_groupsList = new ArrayList<>();

    ProgressDialog progressDialog;
    EditText editText;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution__groups);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.group_recyc);
       // LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        instiGroupAdapter = new InstiGroupAdapter(Institution_Groups.this,institution_groupsList);
        recyclerView.setAdapter(instiGroupAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
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
        String url = base_app_url+"api/vendor/group/all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("JSONGroups",response.toString());
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("vendor_groups");
                    for (int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int Group_id = jsonObject.getInt("id");
                        String group_name = jsonObject.getString("group_name");
                        String image = jsonObject.getString("vendor_image");
                        com.ezyedu.student.model.Institution_Groups post = new com.ezyedu.student.model.Institution_Groups(Group_id,group_name,image);
                        institution_groupsList.add(post);
                        recyclerView.getAdapter().notifyDataSetChanged();

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
                Log.i("ErrorGroups",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}