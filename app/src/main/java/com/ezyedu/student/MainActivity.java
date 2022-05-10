package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ezyedu.student.adapter.ArticlesAdapter;
import com.ezyedu.student.adapter.CourseAdapter;
import com.ezyedu.student.adapter.First_Adapter;
import com.ezyedu.student.adapter.InstituteAdapter;
import com.ezyedu.student.adapter.Promotion_Adapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.First_model;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.articles;
import com.ezyedu.student.model.courses;
import com.ezyedu.student.model.institutions;
import com.ezyedu.student.model.promotion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  String tag="MainActivity";
    private String tag1 = "ABC";
    String Tag2 = "DEF";
    String session_id = null;
    public ImageView chatbtn,homebtn,exp_btnt,cart_btnt,others_btnt;
    TextView txt1,wlcm;
    ImageButton chatListBtn;



    //pagination response...
    //courses
    LinearLayoutManager manager;
    int page = 1;
    int totalItemCount;
    int firstVisibleItemCount;
    int visibleItemCount;
    int previousTotal;
    boolean load = true;

    //articles
    LinearLayoutManager manager1;
    int pageArticles = 1;
    int totalItemCountArticles;
    int firstVisibleItemCountArticles;
    int visibleItemCountArticles;
    int previousTotalArticles;
    boolean loadArticles = true;



    //institutions
    LinearLayoutManager manager3;
    int pageinsti = 1;
    int totalItemCountinsti;
    int firstVisibleItemCountinsti;
    int visibleItemCountinsti;
    int previousTotalinsti;
    boolean loadinsti = true;


    //bottom nav....
    RelativeLayout rhome,rchat,rexp,rcart,rothers;


    RecyclerView recyclerView,recyclerView_1,recyc_articles,recycler_promotion,recycle_institutions;

    First_Adapter first_adapter;
    CourseAdapter courseAdapter;
    ArticlesAdapter articlesAdapter;
    Promotion_Adapter promotion_adapter;
    InstituteAdapter instituteAdapter;

    private RequestQueue requestQueue;

    ArrayList<First_model> first_model;
    private List<courses> mlist=new ArrayList<>();
    private List<articles> alist = new ArrayList<>();
    private List<promotion> plist = new ArrayList<>();
    private List<institutions> ilist = new ArrayList<>();

    //vendor id storing
    SharedPreferences sp;

    ShimmerFrameLayout s,s1,s2,s3;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        //session id for logged in users
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);


        chatListBtn = findViewById(R.id.chat_list);

        chatListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(MainActivity.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, Chat_List_Activity.class);
                    startActivity(intent);
                }

            }
        });


        s = findViewById(R.id.shimmer_frame_layout);
        s.startShimmerAnimation();

        s1 = findViewById(R.id.shimmer_frame_layout1);
        s1.startShimmerAnimation();

        s2 = findViewById(R.id.shimmer_frame_layout2);
        s2.startShimmerAnimation();

        s3 = findViewById(R.id.shimmer_frame_layout3);
        s3.startShimmerAnimation();


        //rel layout bottom nav
        rhome = findViewById(R.id.rel_1);
        rchat = findViewById(R.id.rel_chat);
        rexp = findViewById(R.id.rel_cen);
        rcart = findViewById(R.id.rel_cart);
        rothers = findViewById(R.id.rel_oth);


        chatbtn = findViewById(R.id.chat_btn);
        exp_btnt = findViewById(R.id.exp_btn);
        cart_btnt = findViewById(R.id.cart_btn);
        others_btnt = findViewById(R.id.others_btn);
        wlcm = findViewById(R.id.welcome_text);

        txt1 = findViewById(R.id.search_txt);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView_1 = findViewById(R.id.home_recyc_2);
        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView_1.setLayoutManager(manager);
        recyclerView_1.setHasFixedSize(true);
        courseAdapter = new CourseAdapter(MainActivity.this,mlist);
        recyclerView_1.setAdapter(courseAdapter);
        fetchCourses();

        recyc_articles = findViewById(R.id.home_page_recyc_5);
        manager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyc_articles.setLayoutManager(manager1);
        recyc_articles.setHasFixedSize(true);
        articlesAdapter = new ArticlesAdapter(MainActivity.this,alist);
        recyc_articles.setAdapter(articlesAdapter);
        fetchArticles();

        recycler_promotion = findViewById(R.id.recyc_promotion);
        LinearLayoutManager manager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycler_promotion.setLayoutManager(manager2);
        recycler_promotion.setHasFixedSize(true);
        promotion_adapter = new Promotion_Adapter(MainActivity.this,plist);
        recycler_promotion.setAdapter(promotion_adapter);
        fetchPromotion();


        recycle_institutions = findViewById(R.id.recycle_institution);
        manager3 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recycle_institutions.setLayoutManager(manager3);
        recycle_institutions.setHasFixedSize(true);
        instituteAdapter = new InstituteAdapter(MainActivity.this,ilist);
        recycle_institutions.setAdapter(instituteAdapter);
        fetchInstitutions();





        //      recyclerView_1.setHasFixedSize(true);
    //    recyclerView_1.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));

      //  mlist = new ArrayList<>();

        //fetch courses function



        //live chat intent
        rchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ms_livia_activity.class);
                startActivity(intent);
            }
        });
        //cart activity intent
        rcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Cart_Activity.class);
                startActivity(intent);
            }
        });
        //others Activity
        rothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Others_Activity.class);
             //   intent.putExtra("session_main_activity",session_id);
               // Log.i("adcgh",session_id);
                startActivity(intent);
            }
        });
        //explore Activity
        rexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Explore_Activity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.recycler1);
        //image file
        Integer[] icons = {R.drawable.ic_personality_test,0};
        //sstring text array
        String[] head_text = {"Find Out about who you are","Lorem Ipsum"};
        //Bode text array
        String[] body_text = {"take one of the tests","Lorem ipsum is placeholder text"};
        String[] body_text2 = {"Personality","graphic, print,"};
        String[] body_text3 = {"Multi intelligence Or IQ test","publishing industries"};

        //initialize the arraylist
        first_model = new ArrayList<>();
        for (int i = 0; i<icons.length;i++)
        {
            First_model model = new First_model(icons[i],head_text[i],body_text[i],body_text2[i],body_text3[i]);
            first_model.add(model);
        }
        //Design Horizontal layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

      //  CourseAdapter adapter = new CourseAdapter(MainActivity.this,mlist);
        //recyclerView_1.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        //initialize main adapter
        first_adapter = new First_Adapter(MainActivity.this,first_model);
        recyclerView.setAdapter(first_adapter);
        
    }

    private void fetchInstitutions()
    {
        String insti_url = base_app_url+"api/vendor?page="+pageinsti;
        JsonObjectRequest jsonInstituteRequest = new JsonObjectRequest(Request.Method.GET,insti_url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {

                    s2.stopShimmerAnimation();
                    s2.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() >0) {
                        TextView textView = findViewById(R.id.inst_ttl);
                        textView.setVisibility(View.VISIBLE);
                        TextView textView1 = findViewById(R.id.inst_desc);
                      //  textView1.setVisibility(View.VISIBLE);

                        for (int b = 0; b < jsonArray.length(); b++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(b);
                            Integer id = jsonObject.getInt("id");
                            String tittle = jsonObject.getString("name");
                            String logo = jsonObject.getString("logo");
                            String address = jsonObject.getString("address");
                            String hashid = jsonObject.getString("hash_id");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("vendor_group");
                            String learn_type = jsonObject1.getString("group_name");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("vendor_category");
                            String image = jsonObject2.getString("image");
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("image_url");
                            //   String image = jsonObject3.getString("original");
                            JSONObject jsonObject4 = jsonObject.getJSONObject("vendor_reviews");

                            double average_rate = 0;
                            if (!jsonObject4.isNull("average_rate")) {
                                average_rate = jsonObject4.getDouble("average_rate");
                            }

                            int review = jsonObject4.getInt("total_review");


                            institutions post_institutions = new institutions(
                                    learn_type,
                                    tittle,
                                    address, average_rate,
                                    logo, hashid, id, review
                            );
                            ilist.add(post_institutions);
                        }
                    }
                    recycle_institutions.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i("ErrorInsti",e.toString());
                    s2.stopShimmerAnimation();
                    s2.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.i(Tag2,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                s2.stopShimmerAnimation();
                s2.setVisibility(View.GONE);
            }
        });
        requestQueue.add(jsonInstituteRequest);

        paginationInstitutions();
    }

    private void paginationInstitutions()
    {
        recycle_institutions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemCountinsti = manager3.findFirstVisibleItemPosition();
                totalItemCountinsti = manager3.getItemCount();
                visibleItemCountinsti = manager3.getChildCount();

                if (loadinsti)
                {
                    if (totalItemCountinsti > previousTotalinsti)
                    {
                        previousTotalinsti = totalItemCountinsti;
                        pageinsti++;
                        loadinsti = false;
                    }
                }
                if (!loadinsti && (firstVisibleItemCountinsti+visibleItemCountinsti) >= totalItemCountinsti)
                {
                    fetchInstitutions();
                    loadinsti = true;
                }
            }
        });
    }

    /* private void savePromotion()
     {
         SharedPreferences sharedPreferences = getSharedPreferences("promotions",MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();
         Gson gson = new Gson();
         String json = gson.toJson(plist);
         editor.putString("task",json);
         editor.apply();
     }*/
    private void fetchPromotion()
    {
        String promo_url = base_app_url+"api/event";
        JsonObjectRequest jsonObjPromoRequest = new JsonObjectRequest(Request.Method.GET, promo_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    s1.stopShimmerAnimation();
                    s1.setVisibility(View.GONE);
                    Log.i("PromoRes",response.toString());
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        TextView textView = findViewById(R.id.text_promo);
                        textView.setVisibility(View.VISIBLE);
                        TextView textView1 = findViewById(R.id.desc_promo);
                 //       textView1.setVisibility(View.VISIBLE);
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(a);
                            String tittle = jsonObject.getString("title");
                            Log.i("pr_tit", tittle);
                            String hashid = jsonObject.getString("hash_id");
                            JSONArray jsonArray1 = jsonObject.getJSONArray("event_images");
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            String image = jsonObject1.getString("image");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("image_url");
                            //    String image = jsonObject2.getString("small");
                            Log.i("pr_img", image);

                            promotion post_promotion = new promotion(
                                    tittle,
                                    image, hashid
                            );
                            plist.add(post_promotion);
                            Log.i("ArraySize_promotions", String.valueOf(plist.size()));
                        }
                    }
                    recycler_promotion.getAdapter().notifyDataSetChanged();
       //             savePromotion();
                } catch (JSONException e) {
                    e.printStackTrace();
                    s1.stopShimmerAnimation();
                    s1.setVisibility(View.GONE);
                    Log.d(tag1,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                s1.stopShimmerAnimation();
                s1.setVisibility(View.GONE);
            }
        });

requestQueue.add(jsonObjPromoRequest);
    }

    private void fetchArticles()
    {
        String arti_url = base_app_url+"api/blog?page="+pageArticles;
        JsonObjectRequest jsonObjectRequest_articles = new JsonObjectRequest(Request.Method.GET, arti_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    s3.stopShimmerAnimation();
                    s3.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (jsonArray.length() >0) {
                        TextView textView = findViewById(R.id.art_ttl);
                        textView.setVisibility(View.VISIBLE);
                        TextView textView1 = findViewById(R.id.art_desc);
                    //    textView1.setVisibility(View.VISIBLE);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("title");
                            String image = jsonObject.getString("image");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("image_url");
                            //  String image = jsonObject1.getString("original");
                            String hashid = jsonObject.getString("hash_id");

                            articles post_new = new articles(
                                    image, name, hashid
                            );
                            alist.add(post_new);
                            Log.i("ArraySize_articles", String.valueOf(alist.size()));
                        }
                    }
                    recyc_articles.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    s3.stopShimmerAnimation();
                    s3.setVisibility(View.GONE);
                    Log.d(tag,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                s3.stopShimmerAnimation();
                s3.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest_articles);
        paginationArticles();
    }

    private void paginationArticles()
    {
        recyc_articles.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    fetchArticles();
                    loadArticles = true;
                }
            }
        });
    }

    //this is the function...
    private void fetchCourses()
    {

        String url = base_app_url+"api/courses?page="+page;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("responseCourse",response.toString());
                try
                {
                    s.stopShimmerAnimation();
                    s.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("courseSize", String.valueOf(jsonArray.length()));

                    TextView textView = findViewById(R.id.sttr);
                    TextView textView1 = findViewById(R.id.crs_des);

                    if (jsonArray.length()>0) {
                        textView.setVisibility(View.VISIBLE);
                   //     textView1.setVisibility(View.VISIBLE);
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

                            courses post = new courses(
                                    sub_id,
                                    title,
                                    description,
                                    is_discount,
                                    is_installment_available,
                                    duration,
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
                                    ochash_id, 1.0,
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
                            mlist.add(post);
                            Log.i("ArraySize", String.valueOf(mlist.size()));
                        }
                    }
                    //courseAdapter = new CourseAdapter(MainActivity.this,mlist);
                    //recyclerView_1.setAdapter(courseAdapter);
                   // courseAdapter.notifyDataSetChanged();
                    recyclerView_1.getAdapter().notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    s.stopShimmerAnimation();
                    s.setVisibility(View.GONE);
                    Log.d("MainActivty",e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                s.stopShimmerAnimation();
                s.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
        pagination();
    }

    private void pagination()
    {
        recyclerView_1.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
