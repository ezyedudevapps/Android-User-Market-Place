package com.ezyedu.student;

import androidx.annotation.NonNull;
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
import com.ezyedu.student.adapter.exp_course_all_adapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.exp_all_courses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    exp_course_all_adapter adap;
    private ProgressDialog progressDialog;
    private List<exp_all_courses> expAllCoursesList = new ArrayList<>();


    LinearLayoutManager manager;
    int page = 1;
    int totalItemCount;
    int firstVisibleItemCount;
    int visibleItemCount;
    int previousTotal;
    boolean load = true;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        progressDialog = new ProgressDialog(CourseActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        recyclerView = findViewById(R.id.rec_alll_course);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        adap = new exp_course_all_adapter(CourseActivity.this,expAllCoursesList);
        recyclerView.setAdapter(adap);
        recyclerView.setHasFixedSize(true);
        fetchData();

    }

    private void fetchData()
    {
        String url = base_app_url+"api/courses?page="+page;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    progressDialog.dismiss();
                    for (int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Double id;
                        if (jsonObject1.isNull("id"))
                        {
                            id = 0.0;
                        }
                        else
                        {
                            id = jsonObject1.getDouble("id");
                        }
                        String sub_id;
                        if (jsonObject1.isNull("course_sub_category_id"))
                        {
                            sub_id = "0";
                        }
                        else
                        {
                            sub_id = jsonObject1.getString("course_sub_category_id");
                        }
                        Log.i("sub_id_exp", sub_id);
                        String title = jsonObject1.getString("title");
                        Log.i("tittle", title);
                        String description;
                        if (jsonObject1.isNull("description"))
                        {
                            description = "2 weeks";
                        }
                        else {
                            description = jsonObject1.getString("description");
                        }
                        Double price = jsonObject1.getDouble("price");
                        String is_discount = jsonObject1.getString("is_discount");
                        Double discount_price = jsonObject1.getDouble("discount_price");
                        String is_installment_available = jsonObject1.getString("is_installment_available");
                        String duration = jsonObject1.getString("duration");
                        String status = jsonObject1.getString("status");
                        String hash_id = jsonObject1.getString("hash_id");
                        String created_at = jsonObject1.getString("created_at");

                        //course json Array
                        JSONArray courseArray = jsonObject1.getJSONArray("courses_image");

                        JSONObject course_obj = courseArray.getJSONObject(0);
                        Double course_id = course_obj.getDouble("id");
                        Double courses_id = course_obj.getDouble("courses_id");
                        String image = course_obj.getString("image");
                        String ccreated_at = course_obj.getString("created_at");
                        String chash_id = course_obj.getString("hash_id");

                        JSONObject img_object = course_obj.getJSONObject("image_url");
                        String small = img_object.getString("small");
                        String original = img_object.getString("original");

                        JSONObject vendorArray = jsonObject1.getJSONObject("vendor");
                        Double vid = vendorArray.getDouble("id");
                        String name = vendorArray.getString("name");
                        String address = vendorArray.getString("address");
                        Double is_active = vendorArray.getDouble("is_active");
                        String latitude = vendorArray.getString("latitude");
                        String longitude = vendorArray.getString("longitude");
                        String logo = vendorArray.getString("logo");
                        String email = vendorArray.getString("email");
                        String website = vendorArray.getString("website");
                        String phone = vendorArray.getString("phone");
                        Double price_range = vendorArray.getDouble("price_range");
                        String vcreated_at = vendorArray.getString("created_at");
                        String vhash_id = vendorArray.getString("hash_id");
                        Double is_chatting_allowed = vendorArray.getDouble("is_chatting_allowed");
                        String automated_message = vendorArray.getString("automated_message");
                        String video = vendorArray.getString("video");

                        JSONObject logo_obj = vendorArray.getJSONObject("logo_url");
                        String lsmall = logo_obj.getString("small");
                        String ooriginal = logo_obj.getString("original");

                        JSONObject vendor_review_object = vendorArray.getJSONObject("vendor_reviews");
                        Double total_review = vendor_review_object.getDouble("total_review");
                        String average_rate = vendor_review_object.getString("average_rate");

                        JSONObject geo_obj = vendorArray.getJSONObject("geograph");

                        JSONObject prov_obj = geo_obj.getJSONObject("province");
                        Double pid = prov_obj.getDouble("id");
                        String pname = prov_obj.getString("name");

                        JSONObject city_obj = prov_obj.getJSONObject("city");
                        Double cid = city_obj.getDouble("id");
                        String ccname = city_obj.getString("name");

                        JSONObject district_obj = city_obj.getJSONObject("district");
                        Double did = district_obj.getDouble("id");
                        String dname = district_obj.getString("name");

                        JSONObject course_category_obj = jsonObject1.getJSONObject("course_category");
                        Double coid = course_category_obj.getDouble("id");
                        String label = course_category_obj.getString("label");
                        String created_at_o = course_category_obj.getString("created_at");
                        String ochash_id = course_category_obj.getString("hash_id");

                        exp_all_courses post = new exp_all_courses(  sub_id,
                                title,
                                description,
                                is_discount,
                                is_installment_available,
                                "2 weeks",
                                status,
                                hash_id,
                                created_at,
                                image,
                                ccreated_at,
                                chash_id,
                                small,
                                original,
                                name,
                                address,
                                latitude,
                                longitude,
                                logo,
                                email,
                                website,
                                phone,
                                vcreated_at,
                                vhash_id,
                                automated_message,
                                video,
                                lsmall,
                                ooriginal,
                                average_rate,
                                pname,
                                ccname,
                                dname,
                                label,
                                created_at_o,
                                ochash_id,1.0,
                                id,
                                price,
                                discount_price,
                                course_id,
                                courses_id,
                                vid,
                                is_active,
                                price_range,
                                is_chatting_allowed,
                                total_review,
                                pid,
                                cid,
                                did,
                                coid);
                        expAllCoursesList.add(post);
                    }
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CourseActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
        pagination();
    }

    private void pagination()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemCount = manager.findFirstVisibleItemPosition();
                totalItemCount = manager.getItemCount();
                visibleItemCount = manager.getChildCount();

                if (load)
                {
                    if (totalItemCount > previousTotal)
                    {
                        previousTotal = totalItemCount;
                        page++;
                        load = false;
                    }
                }
                if (!load && (firstVisibleItemCount+visibleItemCount) >= totalItemCount)
                {
                    fetchData();
                    load = true;
                }
            }
        });
    }
}