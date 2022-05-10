package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class Reset_Password extends AppCompatActivity {
    TextView forgetpass;
    EditText otp,password;
    Button submit;

    String OTP,PASSWORD;
    RequestQueue requestQueue;

    String MAIL;

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
        setContentView(R.layout.activity_reset__password);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        forgetpass = findViewById(R.id.forget_pass_btn);
        SpannableString content = new SpannableString("Forgot Password?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        forgetpass.setText(content);


        MAIL = getIntent().getStringExtra("mail");
        Log.i("MAIL",MAIL);

        otp = findViewById(R.id.get_otp);
        password = findViewById(R.id.get_pass);
        submit = findViewById(R.id.sbmit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OTP = otp.getText().toString();
                PASSWORD = password.getText().toString();
                if (TextUtils.isEmpty(OTP))
                {
                    Toast.makeText(Reset_Password.this, "Pleae Enter OTP", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(PASSWORD))
                {
                    Toast.makeText(Reset_Password.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (OTP.length()==4)
                    {
                        if (PASSWORD.length()>5)
                        {
                            progressDialog = new ProgressDialog(Reset_Password.this);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            ResetPassword();
                        }
                        else
                        {
                            Toast.makeText(Reset_Password.this, "Password Must contain Atleast 6 characters", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(Reset_Password.this, "OTP Should Only be 4 Numbers ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void ResetPassword()
    {
        String url = "https://dev-api.ezy-edu.com/api/auth/reset-password";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email",MAIL)
                .addFormDataPart("key",OTP)
                .addFormDataPart("new_password",PASSWORD)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(base_app_url+"api/auth/reset-password")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                Log.d("ResponseResetPass", response.body().string());
                ResponseBody a = response.body();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(Reset_Password.this, "Password Reset Success", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent1 = new Intent(Reset_Password.this,Login_Activity.class);
                            startActivity(intent1);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Reset_Password.this, "Invalid OTP/Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progressDialog.dismiss();
                Log.d("ResponseResetFail", e.toString());
            }
        });
    }
}