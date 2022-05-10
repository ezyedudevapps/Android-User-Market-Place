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
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Forget_Password extends AppCompatActivity {

    TextView forgetpass;
    EditText mail;
    Button submit;
    String mail_id;
    RequestQueue requestQueue;

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
        setContentView(R.layout.activity_forget__password);

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

        mail = findViewById(R.id.get_mail);
        submit = findViewById(R.id.sbmit_btn);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail_id = mail.getText().toString();
                if (TextUtils.isEmpty(mail_id))
                {
                    Toast.makeText(Forget_Password.this, "email should not be null", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog = new ProgressDialog(Forget_Password.this);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    forgotpassword();
                }
            }
        });
    }

    private void forgotpassword()
    {
        String url = "https://dev-api.ezy-edu.com/api/auth/forgot-password";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email",mail_id)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(base_app_url+"api/auth/forgot-password")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("ResponseCourseFailure", e.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("ResponseAddCourse", response.body().string());
                ResponseBody a = response.body();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast toast = Toast.makeText(Forget_Password.this, "OTP Has been sent to Your Registered E-Mail", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent1 = new Intent(Forget_Password.this,Reset_Password.class);
                            intent1.putExtra("mail",mail_id);
                            startActivity(intent1);


                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Forget_Password.this, "Invalid User ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}