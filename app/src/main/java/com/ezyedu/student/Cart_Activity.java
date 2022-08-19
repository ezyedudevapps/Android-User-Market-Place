package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezyedu.student.adapter.CartAdapter;
import com.ezyedu.student.model.Cart_Details;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart_Activity extends AppCompatActivity {
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    private List<cart> cartList = new ArrayList<>();
    Button check_out_btn;
    RelativeLayout rhome,rchat,rexp,rcart,rothers;

    private List<Cart_Details> cart_detailsList ;
    ImageView imageView;

    TextView cart, t1,t2;
    Button btn;
    String session_id = null;
    int total_price = 0;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

  String language = null;
    TextView hom,ms,exp,crt,othrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);

        //get domain url
        base_app_url = sharedData.getValue();
      //  Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        //Log.i("img_url_global",img_url_base);


        cart =findViewById(R.id.main_crt);
        imageView = findViewById(R.id.cart_empty_img);
        t1 = findViewById(R.id.ec);
        t2 = findViewById(R.id.ec1);
        btn = findViewById(R.id.openCourses);

        hom = findViewById(R.id.home_txt);
        ms = findViewById(R.id.ms_livia_text);
        exp = findViewById(R.id.explore_txt);
        crt = findViewById(R.id.Cart_text);
        othrs = findViewById(R.id.Others_text);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            hom.setText("Beranda");
            ms.setText("Ms.Livia");
            exp.setText("Explore");
            crt.setText("Keranjang");
            othrs.setText("Lainya");
            cart.setText("Keranjang");
            t1.setText("Keranjang kamu kosong");
            t2.setText("Sepertinya kamu belum masukan kursus kamu ke keranjang");
            btn.setText("Cari Kursus");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Cart_Activity.this,Search_Course_Activity.class);
                startActivity(intent1);
            }
        });
check_out_btn = findViewById(R.id.prc_checkout);
check_out_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       /* Intent intent1 = new Intent(Cart_Activity.this,Checkout_Page.class);
        startActivity(intent1);
        */
        Intent intent1 = new Intent(Cart_Activity.this,PaymentMethodActivity.class);
        startActivity(intent1);

    }
});
        //rel layout bottom nav
        rhome = findViewById(R.id.rel_1);
        rchat = findViewById(R.id.rel_chat);
        rexp = findViewById(R.id.rel_cen);
        rcart = findViewById(R.id.rel_cart);
        rothers = findViewById(R.id.rel_oth);

        rhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        rchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Activity.this,ms_livia_activity.class);
                startActivity(intent);
            }
        });
        rexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Activity.this,Explore_Activity.class);
                startActivity(intent);
            }
        });
        rothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_Activity.this,Others_Activity.class);
                startActivity(intent);
            }
        });

     Log.i("TotalPriceCart", String.valueOf(total_price));

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cart_activity",session_id);

        if (TextUtils.isEmpty(session_id))
        {
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
        }

        recyclerView = findViewById(R.id.recycer_cart);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        cartAdapter = new CartAdapter(Cart_Activity.this,cartList);
        recyclerView.setAdapter(cartAdapter);
         fetchdata(session_id);
    }

    private void fetchdata(String session_id)
    {
        String url = base_app_url+"api/user/cart";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CartGetVal",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has("message"))
                    {
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        check_out_btn.setVisibility(View.VISIBLE);
                    }
                    Log.i("jsonCart",jsonObject.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String course_hash_id = jsonObject1.getString("course_hash_id");
                            String title = jsonObject1.getString("title");
                            Double price = jsonObject1.getDouble("price");
                            Double discount_price = jsonObject1.getDouble("discount_price");
                            String start_date = jsonObject1.getString("start_date");
                            String image = jsonObject1.getString("image");
                            String vendor_name = jsonObject1.getString("vendor_name");
                            cart post = new cart(course_hash_id, title, price, discount_price, start_date, image, vendor_name);
                            cartList.add(post);
                            Log.i("cartListSize", String.valueOf(cartList.size()));
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
                Log.i("CartGetErrorVal",error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                     //   Toast.makeText(Cart_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Cart_Activity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}