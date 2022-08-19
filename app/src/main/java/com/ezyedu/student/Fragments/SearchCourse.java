package com.ezyedu.student.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.R;
import com.ezyedu.student.adapter.SearchCoursesAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchCourse extends Fragment
{

    EditText editText;
    RecyclerView recyclerView;
    SearchCoursesAdapter searchCoursesAdapter;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private List<com.ezyedu.student.model.SearchCourse> searchCourseList = new ArrayList<>();


    GridLayoutManager manager;
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

    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        requestQueue= Volley.newRequestQueue(getContext());

        View view = inflater.inflate(R.layout.fragment_search_course, container, false);

        linearLayout = view.findViewById(R.id.ln_crse);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        //Search courses
        editText = view.findViewById(R.id.search_course_edit);
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



        recyclerView = view.findViewById(R.id.search_course_recycler);
        searchCoursesAdapter = new SearchCoursesAdapter(getContext(),searchCourseList);
       manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);

      //  LinearLayoutManager manager = new GridLayoutManager(getContext(),2);
        //recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(searchCoursesAdapter);
        fetchCourses();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        return view;
    }

    //method for search
    private void filter(String text)
    {
        ArrayList<com.ezyedu.student.model.SearchCourse> filteredlist = new ArrayList<>();

        for (com.ezyedu.student.model.SearchCourse item : searchCourseList)
        {
            if (item.getTittle().toLowerCase().contains(text.toLowerCase()))
            {
                filteredlist.add(item);
            }
        }
        searchCoursesAdapter.filterList(filteredlist);

    }





    private void fetchCourses()
    {

        String url = base_app_url+"api/courses?page="+page;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    progressDialog.dismiss();
                    int current_page = response.getInt("current_page");
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Double id = jsonObject1.getDouble("id");
                            String sub_id = jsonObject1.getString("course_sub_category_id");
                            Log.i("sub_id", sub_id);
                            String title = jsonObject1.getString("title");
                            Log.i("tittle", title);
                            String description = jsonObject1.getString("description");
                            Double price = jsonObject1.getDouble("price");
                            String is_discount = jsonObject1.getString("is_discount");
                            Double discount_price = jsonObject1.getDouble("discount_price");
                            String is_installment_available = jsonObject1.getString("is_installment_available");
                            String duration;
                            if (jsonObject1.isNull("duration")) {
                                duration = "2 weeks";
                            } else {
                                duration = jsonObject1.getString("duration");
                            }
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

                            com.ezyedu.student.model.SearchCourse post = new com.ezyedu.student.model.SearchCourse(hash_id, duration, created_at_o, name, title, image);
                            searchCourseList.add(post);
                        }
                    }
                    else if (current_page == 1 & jsonArray.length() == 0)
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                linearLayout.setVisibility(View.VISIBLE);
              //  Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    fetchCourses();
                    load = true;
                }
            }
        });
    }
}