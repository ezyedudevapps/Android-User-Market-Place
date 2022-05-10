package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.SeperateCourseAdapter;
import com.ezyedu.student.adapter.SeperateCoursePriceAdapter;
import com.ezyedu.student.adapter.SliderAdp;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SliderImages;
import com.ezyedu.student.model.course_seperate;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course_one_new extends AppCompatActivity {

    private  String tag="MainActivity";
    RecyclerView recyclerView,recyclerView1;
    private TextView textView;
    private String Hashid;
    private String TAG = "CourseOneActivity";
    SeperateCourseAdapter seperateCourseAdapter;
    SeperateCoursePriceAdapter seperateCoursePriceAdapter;
    private RequestQueue requestQueue;
    private List<course_seperate> mList = new ArrayList<>();


    int vendor_rating = 0;

    ProgressDialog progressDialog;

    //top icon
    ImageView bookmark,share,chat,cart,back;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    //reviews specific user
    String username,imag,review;
    int ratin;

    //total avg rating and total users rated
    int course_rating = 0;
    int total_course_rating = 0;

    //slider image
    SliderView sliderView;
    List<SliderImages> sliderImagesList = new ArrayList<>();
    SliderAdp sliderAdp;


    //cart and buy now..
    Button add_cart,buy_now;


    String session_id = null;

    String share_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_one_new);

        Hashid = getIntent().getStringExtra("id");
        Log.i("course_hash",Hashid);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.recyc_sep_cours);
        LinearLayoutManager manager = new LinearLayoutManager(Course_one_new.this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        seperateCourseAdapter = new SeperateCourseAdapter(Course_one_new.this,mList);
        recyclerView.setAdapter(seperateCourseAdapter);
        fetchArticles();

        //top icons

        progressDialog = new ProgressDialog(Course_one_new.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        bookmark = findViewById(R.id.bookmark_img);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dig = new AlertDialog.Builder(Course_one_new.this).setTitle("Please Select").setMessage("Add to Bookmarks ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (TextUtils.isEmpty(session_id))
                                {
                                    Toast.makeText(Course_one_new.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    try {
                                        progressDialog = new ProgressDialog(Course_one_new.this);
                                        progressDialog.show();
                                        progressDialog.setContentView(R.layout.progress_dialog);
                                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                        AddToBookMark();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dig.show();
            }
        });

        chat = findViewById(R.id.chat_sep);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Course_one_new.this,Chat_List_Activity.class);
                startActivity(intent1);
            }
        });

        share_course = base_app_url+"api/courses/"+Hashid;
        share = findViewById(R.id.share_img);
        share.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,share_course);
                intent1.setType("text/plain");

                if (intent1.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent1);
                }

            }
        });

        cart = findViewById(R.id.cart_img);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Course_one_new.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });

        back = findViewById(R.id.sc_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Course_one_new.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        add_cart = findViewById(R.id.add_to_cart_btn);
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(Course_one_new.this, "Please Login to Continue..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        progressDialog = new ProgressDialog(Course_one_new.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);


                        AddToCart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        buy_now = findViewById(R.id.buy_now_btn);
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(Course_one_new.this, "Please Login to Continue..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        progressDialog = new ProgressDialog(Course_one_new.this);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        AddToCart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        //slider
        sliderView = findViewById(R.id.slider_view);
    }

    private void AddToCart() throws JSONException {

        String url = base_app_url+"api/user/cart";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(Hashid);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course_ids",jsonArray);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("addedToCart",response.toString());
                Toast.makeText(Course_one_new.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                Intent intent1= new Intent(Course_one_new.this, Cart_Activity.class);
                startActivity(intent1);
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

    private void AddToBookMark() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",0);
        jsonObject.put("item_id",Hashid);
        String url = base_app_url+"api/bookmark";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    progressDialog.dismiss();
                    String message = response.getString("message");
                    Log.i("BookMarkMessage",message);
                    Toast.makeText(Course_one_new.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void fetchArticles()
    {
        String url = base_app_url+"api/courses/"+Hashid;
        Log.i("url_new_sep",url);

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response)
          {
              progressDialog.dismiss();
              Log.i("ResponseSepCourse",response.toString());
              try {
                  JSONObject jsonObject1 = response.getJSONObject("data");

                  int vendor_id = jsonObject1.getInt("vendor_id");
                  String category_label = jsonObject1.getString("category_label");
                  String course_title = jsonObject1.getString("course_title");
                  String course_description = jsonObject1.getString("course_description");

                  String course_duration = jsonObject1.getString("course_duration");
                  int initial_price = jsonObject1.getInt("initial_price");
                  int discount_price = jsonObject1.getInt("discount_price");
                  String start_date = jsonObject1.getString("start_date");
                  String course_hash_id = jsonObject1.getString("course_hash_id");



                  if(!jsonObject1.isNull("course_review"))
                  {
                      JSONObject j2 = jsonObject1.getJSONObject("course_review");
                      course_rating = j2.getInt("total_rate");
                      total_course_rating = j2.getInt("count");
                      JSONArray jsonArray = j2.getJSONArray("user");

                      for (int i = 0;i<jsonArray.length();i++)
                      {
                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                          username = jsonObject.getString("name");
                          imag = jsonObject.getString("image");
                          ratin = jsonObject.getInt("rate");
                          review = jsonObject.getString("description");
                      }
                  }



                  String vendor_logo = jsonObject1.getString("vendor_logo");
                  String vendor_name = jsonObject1.getString("vendor_name");
                  String vendor_address = jsonObject1.getString("vendor_address");


                 if (jsonObject1.isNull("vendor_rating"))
                 {
                     vendor_rating = 0;
                 }
                 else
                  {
                      vendor_rating = jsonObject1.getInt("vendor_rating");
                  }



                  course_seperate post = new course_seperate(category_label,course_title,course_description,course_duration,initial_price,discount_price,start_date,course_hash_id
                          ,vendor_logo,vendor_name,vendor_rating,vendor_address,vendor_id,course_rating,total_course_rating);

                  mList.add(post);
                  recyclerView.getAdapter().notifyDataSetChanged();



                  JSONArray j1 = jsonObject1.getJSONArray("courses_image");
                  for (int i = 0; i<j1.length();i++)
                  {
                      JSONObject jsonObject = j1.getJSONObject(i);
                      String image = jsonObject.getString("image");
                      SliderImages post1 = new SliderImages(image);
                      sliderImagesList.add(post1);
                  }
                  sliderAdp = new SliderAdp(Course_one_new.this,sliderImagesList);
                  sliderView.setSliderAdapter(sliderAdp);
                  sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                  sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                  sliderView.startAutoCycle();

              } catch (JSONException e) {
                  progressDialog.dismiss();
                  e.printStackTrace();
                  Log.i("errorMissing",e.toString());
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