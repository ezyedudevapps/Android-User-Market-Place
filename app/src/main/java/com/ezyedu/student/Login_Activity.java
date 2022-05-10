package com.ezyedu.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ezyedu.student.Fragments.Gmail_Login_Fragment;
import com.ezyedu.student.model.CourseVolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.util.Util;


public class Login_Activity extends AppCompatActivity implements FacebookCallback<LoginResult> {


    TextView forgetpass,signup;
    EditText email,password;
    Button submit;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;


    TextView privacyPolicy,TermsConditions;


    SharedPreferences sp,sp1;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    CheckBox checkBox;


    //fb
    CallbackManager callbackManager;
    LoginButton fb_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        //to get fb key hashes.....
        Util.KeyHashes(this);

        sp = getSharedPreferences("Session_id", Context.MODE_PRIVATE);

        //underlining the text
        forgetpass = findViewById(R.id.forget_pass_btn);
        SpannableString content = new SpannableString("Forgot Password?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        forgetpass.setText(content);

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_Activity.this,Forget_Password.class);
                startActivity(intent1);
            }
        });

        signup = findViewById(R.id.sign_up_back);
        SpannableString content1 = new SpannableString("Sign Up");
        content1.setSpan(new UnderlineSpan(),0,content1.length(),0);
        signup.setText(content1);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_Activity.this,RegisterActivity.class);
                startActivity(intent1);
            }
        });



        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        email = findViewById(R.id.Login_email_get);
        password = findViewById(R.id.Login_pass_get);


        privacyPolicy = findViewById(R.id.privacy);
        SpannableString c2 = new SpannableString("Privacy Policy");
        c2.setSpan(new UnderlineSpan(),0,content1.length(),0);
        privacyPolicy.setText(c2);

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_Activity.this,Privacy_policy_activity.class);
                startActivity(intent1);
            }
        });
        TermsConditions = findViewById(R.id.terms);
        SpannableString content2 = new SpannableString("Terms & Conditions");
        content2.setSpan(new UnderlineSpan(),0,content2.length(),0);
        TermsConditions.setText(content2);
        TermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_Activity.this,Terms_Conditions_Activity.class);
                startActivity(intent1);
            }
        });

        checkBox = findViewById(R.id.text_check);

        String mail_to_encode = email.getText().toString();
        Log.i("mail_new",mail_to_encode);
        String pass_to_encode = password.getText().toString();

        submit = findViewById(R.id.LoginBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (checkBox.isChecked()) {
                        loginUser();
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "Please Accept Our Privacy Policy to Continue", Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


        //fb
        fb_btn = findViewById(R.id.sign_in_fb_btn);
        callbackManager = CallbackManager.Factory.create();
        fb_btn.registerCallback(callbackManager,Login_Activity.this);

        //gmail fragment
        replaceFragment(new Gmail_Login_Fragment());

    }
    public void loginUser() throws UnsupportedEncodingException {
        String mail_to_encode = email.getText().toString();
        Log.i("mail_new",mail_to_encode);
        String pass_to_encode = password.getText().toString();

        if (TextUtils.isEmpty(mail_to_encode))
        {
            Toast.makeText(this, "mail should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass_to_encode))
        {
            Toast.makeText(this, "password should not be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
           encodeData(mail_to_encode,pass_to_encode);
            progressDialog = new ProgressDialog(Login_Activity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        }


    }

    private void replaceFragment(Gmail_Login_Fragment gmail_login_fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,gmail_login_fragment);
        fragmentTransaction.commit();
    }


    private void encodeData(String mail_to_encode, String pass_to_encode) throws UnsupportedEncodingException {
        String mailpass = mail_to_encode+":"+pass_to_encode;

        //encoding
        byte[] encoding_mail_pass = mailpass.getBytes("UTF-8");
        String encoded_mail_pass= Base64.encodeToString(encoding_mail_pass,Base64.DEFAULT);
        Log.i("encoded_mail_pass",encoded_mail_pass);

        ValidateUser(encoded_mail_pass);

     /*   decoding
        byte [] decoding_mail_pass = Base64.decode(encoded_mail_pass,Base64.DEFAULT);
        String decoded_mail_pass = new String(decoding_mail_pass,"UTF-8");
        Log.i("decoded_mail_pass",decoded_mail_pass);
        */


    }

    private void ValidateUser(String encoded_mail_pass)
    {
        String url = base_app_url+"api/auth/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.i("loginresult",response.toString());
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String session = jsonObject.getString("session");
                    Log.i("sessionLogged",session);
                    //storing session id
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("session_val",session);
                    editor.commit();

                    Intent intent = new Intent(Login_Activity.this,MainActivity.class);
                    startActivity(intent);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                    Double id = jsonObject1.getDouble("id");
                    Log.i("logged_id",id.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Log.i("LoginErrorResult",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("LoginFail", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                      //  Toast.makeText(Login_Activity.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(Login_Activity.this,"Authentication Failed, E-mail or Password is Wrong", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                params.put("Authorization",encoded_mail_pass);
                int role = 3;
                params.put("role",String.valueOf(role));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onSuccess(LoginResult loginResult) {
        Log.i("LoginResultFb", String.valueOf(loginResult));
      //  Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        if(loginResult != null)
        {
            GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response)
                {
                    if (object != null)
                    {
                        try {
                            Log.i("Fbdata",object.toString());
                            String profileid = object.getString("id");
                            String user_name = object.getString("name");
                            Log.i("name",user_name);

                            if (profileid != null)
                            {
                                fbLogin(profileid,user_name);
                                //logout....
                                LoginManager.getInstance().logOut();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("fields","id,name,email,gender,birthday");
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }
    }



    @Override
    public void onCancel() {

    }

    private void fbLogin(String profileid, String user_name) throws JSONException {
        String url = base_app_url+"api/auth/login-social";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provider","facebook");
        jsonObject.put("provider_id",profileid);
        Log.i("JSONBJ",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseFB",response.toString());

                try {
                    String session = response.getString("session");
                    Log.i("sessionLogged",session);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("session_val",session);
                    editor.commit();
                    if (response.has("session"))
                    {
                        Intent intent1 = new Intent(Login_Activity.this,MainActivity.class);
                        startActivity(intent1);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("error",error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());


                    JSONObject jsonObject1= null;
                    try {
                        jsonObject1 = new JSONObject(jsonError);
                        Log.i("fbError",jsonObject1.toString());
                        String mess = jsonObject1.getString("message");
                        Toast.makeText(Login_Activity.this, mess, Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(Login_Activity.this,facebook_Register.class);
                        intent1.putExtra("provider_id",profileid);
                        intent1.putExtra("username",user_name);
                        startActivity(intent1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("fbError",e.toString());
                    }



                }
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onError(FacebookException error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}