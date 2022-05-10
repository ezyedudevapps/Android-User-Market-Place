package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

public class facebook_Register extends AppCompatActivity {


    String provider_id,user_name;
    EditText username,email,password,conform_password;
    Button RegisterButton;
    RequestQueue requestQueue;
    TextView sign_in_bck;
    ProgressDialog progressDialog;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook__register);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        provider_id = getIntent().getStringExtra("provider_id");
        user_name = getIntent().getStringExtra("username");

        Log.i("NewFBData",provider_id+" "+user_name);


        sign_in_bck = findViewById(R.id.sign_up_back);
        SpannableString content1 = new SpannableString("Sign In");
        content1.setSpan(new UnderlineSpan(),0,content1.length(),0);
        sign_in_bck.setText(content1);

        sign_in_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(facebook_Register.this,Login_Activity.class);
                startActivity(intent1);
            }
        });


        username = findViewById(R.id.register_user_get);
        username.setText(user_name);
        email = findViewById(R.id.register_mail_get);
        password = findViewById(R.id.register_pass_get);
        conform_password = findViewById(R.id.register_conf_pass_get);

        RegisterButton = findViewById(R.id.register_btn);




        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RegisterUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void RegisterUser() throws JSONException {
        String user_name = username.getText().toString();
        String e_mail = email.getText().toString();
        String pass_word = password.getText().toString();
        String conf_password = conform_password.getText().toString();
        String role = "zxc123";

        if (TextUtils.isEmpty(user_name))
        {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(e_mail))
        {
            Toast.makeText(this, "Please Enter Email id", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass_word))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (!(pass_word.equals(conf_password)))
        {
            Toast.makeText(this, "Password Does Not match", Toast.LENGTH_SHORT).show();
        }
        else
        {

            RegosterAccount(user_name,e_mail,pass_word,role);
            progressDialog = new ProgressDialog(facebook_Register.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
    }

    private void RegosterAccount(String user_name, String e_mail, String pass_word, String role) throws JSONException {

        String url = base_app_url+"api/auth/register";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", e_mail);
        jsonObject.put("password", pass_word);
        jsonObject.put("username", user_name);
        jsonObject.put("role", role);
        jsonObject.put("provider","facebook");
        jsonObject.put("provider_id",provider_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("RegisterSuccess", response.toString());
                Toast.makeText(facebook_Register.this,  "Otp has been Sent to Your Registered E-mail...", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(facebook_Register.this,RegisterActivateActivity.class);
                intent1.putExtra("mail_id",e_mail);
                startActivity(intent1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("RegisterFailure", error.toString());
                //  Toast.makeText(facebook_Register.this, error.toString(), Toast.LENGTH_SHORT).show();

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {

                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(facebook_Register.this, jsonObject2.toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}