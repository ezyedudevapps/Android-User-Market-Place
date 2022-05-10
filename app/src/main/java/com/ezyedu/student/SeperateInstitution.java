package com.ezyedu
        .student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
import java.util.List;

public class SeperateInstitution extends AppCompatActivity {

    TabLayout tableLayout;
    fragmentInstiAdapter fragmentInstiAdapters;
    ViewPager2 viewPager;
    /*
    RecyclerView recyclerView;
    SeperateInstitutionAdapter seperateInstitutionAdapter;

    private final List<com.madmobiledevs.ezyedu.model.SeperateInstitution> Ilist = new ArrayList<>();

     */

    private RequestQueue requestQueue;

    private String vendor_id;
    SharedPreferences sp1;



    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    //slider image
    SliderView sliderView;
    List<VenSliderImages> sliderImagesList = new ArrayList<>();
    VenSliderAdp sliderAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seperate_institution);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //slider
        sliderView = findViewById(R.id.slider_view);
/*
        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Please Wait");
        LoadingBar.setMessage("Loading Institution Details");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();


     */


        vendor_id = getIntent().getStringExtra("ven_id");
        Log.i("vendor_specific_id",vendor_id);
        sp1 = getSharedPreferences("Vendor_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp1.edit();
        editor1.putString("ven",vendor_id);
        editor1.commit();

        String base = base_app_url+"api/vendor/";
        String end = "/details";
        String url = base+vendor_id+end;
        fetchdata(url);

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


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentInstiAdapters = new fragmentInstiAdapter(fragmentManager,getLifecycle());
        viewPager.setAdapter(fragmentInstiAdapters);

        tableLayout.addTab(tableLayout.newTab().setText("Profile"));
        tableLayout.addTab(tableLayout.newTab().setText("Products"));
        tableLayout.addTab(tableLayout.newTab().setText("Feed"));


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

    private void fetchdata(String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("ResponseImage",response.toString());


                    JSONArray jsonArray = response.getJSONArray("vendor_image");

                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    for (int i = 0; i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        String img = jsonObject.getString("url");
                        VenSliderImages post1 = new VenSliderImages(img);
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
}