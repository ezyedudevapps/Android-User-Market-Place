package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.google.android.material.tabs.TabLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.VenSliderAdp;
import com.ezyedu.student.adapter.fragmentInstiAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.VenSliderImages;
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

public class SeperateInstitution extends AppCompatActivity {

    TabLayout tableLayout;
    fragmentInstiAdapter fragmentInstiAdapters;
    ViewPager2 viewPager;
    /*
    RecyclerView recyclerView;
    SeperateInstitutionAdapter seperateInstitutionAdapter;

    private final List<com.madmobiledevs.ezyedu.model.SeperateInstitution> Ilist = new ArrayList<>();

     */

    String session_id = null;
    String language = null;
    private RequestQueue requestQueue;

    private String vendor_id;
    SharedPreferences sp1;

    String bookmark_val;
    String vendor_hash_id;


    ImageView back,chat,cart,bookmark,share,bookmarked;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String share_vendor;

    //slider image
    SliderView sliderView;
    List<VenSliderImages> sliderImagesList = new ArrayList<>();
    VenSliderAdp sliderAdp;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seperate_institution);

        //session id for logged in users
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        vendor_id = getIntent().getStringExtra("ven_id");
        Log.i("vendor_specific_id",vendor_id);



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        sp1 = getSharedPreferences("Vendor_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putString("ven",vendor_id);
        editor1.commit();

        String base = base_app_url+"api/vendor/";
        String end = "/details";
        String url = base+vendor_id+end;

        if (!TextUtils.isEmpty(session_id))
        {
            fetchAuthData(url);
        }
        else
        {
            fetchdata(url);
        }


        share = findViewById(R.id.share_img);
          share_vendor = base_app_url+"api/vendor/"+vendor_id;
        share.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,share_vendor);
                intent1.setType("text/plain");

                if (intent1.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent1);
                }

            }
        });

        back = findViewById(R.id.bk_rel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SeperateInstitution.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        chat = findViewById(R.id.chat_rel);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SeperateInstitution.this,Chat_List_Activity.class);
                startActivity(intent1);
            }
        });

        cart = findViewById(R.id.cart_rel);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SeperateInstitution.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });

        bookmarked = findViewById(R.id.bookmarked_img);
        bookmarked.setColorFilter(ContextCompat.getColor(this, R.color.orange));

        bookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dig = new AlertDialog.Builder(SeperateInstitution.this).setTitle("Please Select").setMessage("Remove from Bookmarks ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (TextUtils.isEmpty(session_id))
                                {
                                    Toast.makeText(SeperateInstitution.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    try {
                                        bookmarked.setVisibility(View.GONE);
                                        bookmark.setVisibility(View.VISIBLE);
                                        progressDialog = new ProgressDialog(SeperateInstitution.this);
                                        progressDialog.show();
                                        progressDialog.setContentView(R.layout.progress_dialog);
                                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                        removeBookMark();
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

        bookmark = findViewById(R.id.bk1_rel);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dig = new AlertDialog.Builder(SeperateInstitution.this).setTitle("Mohon dipilih").setMessage("Masukan ke Bookmarks?").
                            setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (TextUtils.isEmpty(session_id))
                                    {
                                        Toast.makeText(SeperateInstitution.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        try {
                                            bookmark.setVisibility(View.GONE);
                                            bookmarked.setVisibility(View.VISIBLE);
                                            progressDialog = new ProgressDialog(SeperateInstitution.this);
                                            progressDialog.show();
                                            progressDialog.setContentView(R.layout.progress_dialog);
                                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                            addToMark();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dig.show();
                }
            });
        }
        else
        {
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog dig = new AlertDialog.Builder(SeperateInstitution.this).setTitle("Please Select").setMessage("Add to Bookmarks ?").
                            setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (TextUtils.isEmpty(session_id))
                                    {
                                        Toast.makeText(SeperateInstitution.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        try {
                                            bookmark.setVisibility(View.GONE);
                                            bookmarked.setVisibility(View.VISIBLE);
                                            progressDialog = new ProgressDialog(SeperateInstitution.this);
                                            progressDialog.show();
                                            progressDialog.setContentView(R.layout.progress_dialog);
                                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                            addToMark();
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
        }



        //slider
        sliderView = findViewById(R.id.slider_view);
/*
        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Please Wait");
        LoadingBar.setMessage("Loading Institution Details");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();


     */





 /*       recyclerView = findViewById(R.id.insti_recyc);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        seperateInstitutionAdapter = new SeperateInstitutionAdapter(SeperateInstitution.this,Ilist);
        recyclerView.setAdapter(seperateInstitutionAdapter);
    //    fetchData();

  */

        tableLayout = findViewById(R.id.tab1);
        viewPager = findViewById(R.id.vpager);


        if (language.equals("Indonesia"))
        {
            tableLayout.addTab(tableLayout.newTab().setText("Profil"));
            tableLayout.addTab(tableLayout.newTab().setText("Produk"));
            tableLayout.addTab(tableLayout.newTab().setText("Feed"));
        }
        else
        {
            tableLayout.addTab(tableLayout.newTab().setText("Profile"));
            tableLayout.addTab(tableLayout.newTab().setText("Products"));
            tableLayout.addTab(tableLayout.newTab().setText("Feed"));
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentInstiAdapters = new fragmentInstiAdapter(fragmentManager,getLifecycle());
        viewPager.setAdapter(fragmentInstiAdapters);





        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });


    }

    private void removeBookMark() throws JSONException {
        String url = base_app_url+"api/bookmark";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",1);
        jsonObject.put("item_id",vendor_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("BIresponse",response.toString());
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(SeperateInstitution.this, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
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

    private void addToMark() throws JSONException {
        String url = base_app_url+"api/bookmark";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",1);
        jsonObject.put("item_id",vendor_id);
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(SeperateInstitution.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    private void fetchAuthData(String url)
    {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("ResponseImage",response.toString());

                    vendor_hash_id = response.getString("vendor_hash_id");
                    bookmark_val = response.getString("bookmark");
                    if (bookmark_val.equals("true"))
                    {
                     bookmarked.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        bookmark.setVisibility(View.VISIBLE);
                    }

                    JSONArray jsonArray = response.getJSONArray("vendor_image");

                    String imge = response.getString("vendor_logo");
                    int vendor_rating = response.getInt("vendor_rating");
                    String tittle = response.getString("vendor_name");

                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    for (int i = 0; i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        String img = jsonObject.getString("url");
                        VenSliderImages post1 = new VenSliderImages(img,imge,vendor_rating,tittle);
                        sliderImagesList.add(post1);
                    }
                    sliderAdp = new VenSliderAdp(SeperateInstitution.this,sliderImagesList);
                    sliderView.setSliderAdapter(sliderAdp);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();

                  /*  com.madmobiledevs.ezyedu.model.SeperateInstitution post = new com.madmobiledevs.ezyedu.model.SeperateInstitution(image);
                    Ilist.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();
                   */
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("errorSlider",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

    private void fetchdata(String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("ResponseImage",response.toString());

                    JSONArray jsonArray = response.getJSONArray("vendor_image");

                    String imge = response.getString("vendor_logo");
                    int vendor_rating = response.getInt("vendor_rating");
                    String tittle = response.getString("vendor_name");

                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    for (int i = 0; i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        String img = jsonObject.getString("url");
                        VenSliderImages post1 = new VenSliderImages(img,imge,vendor_rating,tittle);
                        sliderImagesList.add(post1);
                    }
                    sliderAdp = new VenSliderAdp(SeperateInstitution.this,sliderImagesList);
                    sliderView.setSliderAdapter(sliderAdp);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();

                  /*  com.madmobiledevs.ezyedu.model.SeperateInstitution post = new com.madmobiledevs.ezyedu.model.SeperateInstitution(image);
                    Ilist.add(post);
                    recyclerView.getAdapter().notifyDataSetChanged();
                   */
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("errorSlider",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

 /*   @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(SeperateInstitution.this,MainActivity.class);
        startActivity(intent1);
    }

  */
}