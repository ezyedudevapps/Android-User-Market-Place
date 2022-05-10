package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class My_profile_Activity extends AppCompatActivity {
    RequestQueue requestQueue;
    String session_id = null;
    private ProgressDialog LoadingBar;
TextView name,username,email,birthdate,gender,phone,editProfile;
ImageView imageView,chat,cart,back;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        imageView = findViewById(R.id.image_head);
        name = findViewById(R.id.name);
        username = findViewById(R.id.User_name);
        email = findViewById(R.id.User_mail);
        birthdate = findViewById(R.id.user_b_date);
        gender = findViewById(R.id.user_gender);
        phone = findViewById(R.id.user_phone);
        editProfile = findViewById(R.id.edit_profile);

        chat = findViewById(R.id.mp_chat);
        cart = findViewById(R.id.cart_btt);
        back = findViewById(R.id.left_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(My_profile_Activity.this,Others_Activity.class);
                startActivity(intent1);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(My_profile_Activity.this,Chat_List_Activity.class);
                startActivity(intent1);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(My_profile_Activity.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_account_activity",session_id);

        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Please Wait");
        LoadingBar.setMessage("Fetching Data");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        if (!TextUtils.isEmpty(session_id))
        {
            fetchData(session_id);
        }



    }

    private void fetchData(String session_id)
    {
        String url = base_app_url+"api/user/profile";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonUserInfo",response.toString());
                LoadingBar.dismiss();

                try {
                    Integer Id = response.getInt("id");
                    String email_get;
                    if( response.isNull("email"))
                    {
                        email_get = "email";
                    }
                    else
                    {
                        email_get = response.getString("email");
                    }
                    String name_get;
                    if( response.isNull("name"))
                    {
                        name_get = "name";
                    }
                    else
                    {
                        name_get = response.getString("name");
                    }

                    String username_get;
                    if( response.isNull("username"))
                    {
                        username_get = "User Name";
                    }
                    else
                    {
                        username_get = response.getString("username");
                    }

                    String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                    String image_get;
                    if (response.isNull("image"))
                    {
                        image_get = null;
                        Glide.with(My_profile_Activity.this).load(R.drawable.ic_launcher_background).into(imageView);
                    }
                    else {
                        image_get = response.getString("image");
                        Glide.with(My_profile_Activity.this).load(img_url_base + image_get).into(imageView);
                    }

                    String birth_date_get;
                    if( response.isNull("birth_date"))
                    {
                        birth_date_get = "birth_date";
                    }
                    else
                    {
                        birth_date_get = response.getString("birth_date");
                    }

                    String gender_get;
                    if( response.isNull("gender"))
                    {
                        gender_get = "gender";
                    }
                    else
                    {
                        gender_get = response.getString("gender");
                    }
                    String phone_get;

                    if( response.isNull("phone"))
                    {
                        phone_get = "phone";
                    }
                    else
                    {
                        phone_get = response.getString("phone");
                    }
                        phone.setText(" Phone :"+phone_get);
                        gender.setText(" Gender :"+gender_get);
                        birthdate.setText("Birth Data :"+birth_date_get);
                        email.setText(" email : "+email_get);
                    name.setText(" Name :"+name_get);
                    username.setText(" User Name :"+username_get);
                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(My_profile_Activity.this,Update_profile_activity.class);
                            Bundle extras = new Bundle();
                            extras.putString("name",name_get);
                            extras.putString("birthDate",birth_date_get);
                            extras.putString("gender",gender_get);
                            extras.putString("phone",phone_get);
                            extras.putString("image",image_get);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                LoadingBar.dismiss();
                Log.i("jsonUserError",error.toString());
                Toast.makeText(My_profile_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(My_profile_Activity.this,Others_Activity.class);
        startActivity(intent1);
    }
}