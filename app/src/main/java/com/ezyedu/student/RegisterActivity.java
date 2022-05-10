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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password,conform_password,referral_code;
    Button RegisterButton;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    TextView sign_in;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        username = findViewById(R.id.register_user_get);
        email = findViewById(R.id.register_mail_get);
        password = findViewById(R.id.register_pass_get);
        conform_password = findViewById(R.id.register_conf_pass_get);
        referral_code = findViewById(R.id.ref_code_get);


        sign_in = findViewById(R.id.sign_up_back);
        SpannableString content = new SpannableString(" Sign in ?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        sign_in.setText(content);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterActivity.this,Login_Activity.class);
                startActivity(intent1);
            }
        });
        RegisterButton = findViewById(R.id.register_btn);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();


        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser()
    {
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
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            RegosterAccount(user_name,e_mail,pass_word,role);
        }

    }

    private void RegosterAccount(String user_name, String e_mail, String pass_word, String role)
    {
        String url = base_app_url+"api/auth/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                Log.i("RegisterSuccess",response.toString());
                Toast.makeText(RegisterActivity.this, "Congratulations, Account Has Been Registered Successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this,RegisterActivateActivity.class);
                intent.putExtra("mail_id",e_mail);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(RegisterActivity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",e_mail);
                params.put("password",pass_word);
                params.put("username",user_name);
                params.put("role",role);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}