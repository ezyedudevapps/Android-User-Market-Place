package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Others_Activity extends AppCompatActivity {
    Button button;
    RequestQueue requestQueue;
    String session_main = null;
    ImageView imageView;
    TextView textView;
    private ProgressDialog progressDialog;

    CardView card_languag,card_bookmarks,card_bankacccount,card_History,card_referrals,card_settings,card_Logout,card_account;

    //bottom nav....
    RelativeLayout rhome,rchat,rexp,rcart,rothers;


    TextView lg,ma,bk,hs,st,lgo;
    String language = null;
    TextView hom,ms,exp,crt,othrs;
    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        button = findViewById(R.id.login_sign_up);
        imageView = findViewById(R.id.img_other);
        textView = findViewById(R.id.welcm_name);

        card_bookmarks = findViewById(R.id.card_bookmarks);
        card_bankacccount = findViewById(R.id.card_bankacccount);
        card_History = findViewById(R.id.card_History);
        card_referrals = findViewById(R.id.card_referrals);
        card_settings = findViewById(R.id.card_settings);
        card_Logout = findViewById(R.id.card_Logout);
        card_account = findViewById(R.id.card_myaccount);
        card_languag = findViewById(R.id.card_language);

        lg= findViewById(R.id.lng);
        ma=findViewById(R.id.myc);
        bk=findViewById(R.id.bmk);
        hs=findViewById(R.id.hstry);
        st=findViewById(R.id.stngs);
        lgo=findViewById(R.id.lgout);

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


            lg.setText("Bahasa");
            ma.setText("Akun Saya");
            bk.setText("Bookmarks");
            hs.setText("Sejarah");
            st.setText("Pengaturan");
            lgo.setText("Keluar akun");
        }


        card_languag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Others_Activity.this,Language_activity.class);
                startActivity(intent1);
            }
        });



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        //rel layout bottom nav
        rhome = findViewById(R.id.rel_1);
        rchat = findViewById(R.id.rel_chat);
        rexp = findViewById(R.id.rel_cen);
        rcart = findViewById(R.id.rel_cart);
        rothers = findViewById(R.id.rel_oth);


        rhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Others_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        rchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Others_Activity.this,ms_livia_activity.class);
                startActivity(intent);
            }
        });
        rexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Others_Activity.this,Explore_Activity.class);
                startActivity(intent);
            }
        });
        rcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Others_Activity.this,Cart_Activity.class);
                startActivity(intent);
            }
        });







        SharedPreferences sp = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_main = sp.getString("session_val","");
        Log.i("Session_other_activity",session_main);

        //get profile image and Name....
        if (!TextUtils.isEmpty(session_main))
        {
            progressDialog = new ProgressDialog(Others_Activity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            getData(session_main);
        }

        if (TextUtils.isEmpty(session_main))
        {
            button.setVisibility(View.VISIBLE);
        }
        else
        {
            card_Logout.setVisibility(View.VISIBLE);
           // card_bankacccount.setVisibility(View.VISIBLE);
            card_bookmarks.setVisibility(View.VISIBLE);
            card_History.setVisibility(View.VISIBLE);
         //   card_referrals.setVisibility(View.VISIBLE);
            card_settings.setVisibility(View.VISIBLE);
            card_account.setVisibility(View.VISIBLE);
        }

card_account.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Others_Activity.this,Update_profile_activity.class);
        startActivity(intent);
    }
});
        int a = 92;

        card_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Others_Activity.this,History_Page.class);
             //   intent1.putExtra("id",a);
                startActivity(intent1);
            }
        });

        card_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = new ProgressDialog(Others_Activity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                logOut();


            }
        });

        card_bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Others_Activity.this,Bookmarks_Activity.class);
                startActivity(intent1);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Others_Activity.this,Login_signup_page.class);
                startActivity(intent);
            }
        });

        card_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Others_Activity.this,Settings_Activity.class);
                startActivity(intent1);
            }
        });
    }

    private void logOut()
    {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(base_app_url+"api/auth/logout")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", session_main)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("ResponseCourseFailure", e.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                Log.d("ResponseAddCourse", response.body().string());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Others_Activity.this, "Logged Out...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Others_Activity.this,MainActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences("Session_id",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("session_val");
                            editor.apply();
                            finish();
                        }
                    }
                });
            }
        });

    }

    private void getData(String session_main)
    {
        String url = base_app_url+"api/user/profile";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("responseUser",response.toString());
                progressDialog.dismiss();
                if (!response.isNull("name"))
                {
                    try {
                        textView.setText(response.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    textView.setText("Welcome Guest");
                }

                String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                String image_get;
                if (response.isNull("image"))
                {
                    Glide.with(Others_Activity.this).load(R.drawable.spf).into(imageView);
                }
                else {
                    try {
                        image_get = response.getString("image");
                        Glide.with(Others_Activity.this).load(img_url_base + image_get).into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(Others_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",session_main);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Others_Activity.this,MainActivity.class);
        startActivity(intent1);
    }
}