package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.RealPathUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Update_profile_activity extends AppCompatActivity {

    ImageView Change_pic;

    private ProgressDialog LoadingBar;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String Gender;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    String path = null;

    ProgressDialog progressDialog;

    RadioButton g1,g2,g3;
    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    EditText name_edit, dob_edit, gender_edit, phone_edit;
    ImageView imageView;
    TextView upload_server;
    String session_id = null,
            name_update = null, birth_update = null, gender_update = null, phone_update = null, image_update = null;
    RequestQueue requestQueue;

    TextView usrname,myprofile,datebirth,phonenumber,gendertext;
    String language = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_activity);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        radioGroup = findViewById(R.id.radio_price);
        g1 = findViewById(R.id.yes_price);
        g2 = findViewById(R.id.no_price);
        g3 = findViewById(R.id.nos_price);

        Change_pic = findViewById(R.id.change_photo_btn);

        name_edit = findViewById(R.id.name_to_update);
        dob_edit = findViewById(R.id.dob_to_update);
        phone_edit = findViewById(R.id.phone_to_update);
        imageView = findViewById(R.id.image_to_update);
        upload_server = findViewById(R.id.update_progile_btn);

        myprofile= findViewById(R.id.mprfle);
        usrname=  findViewById(R.id.usr_txt);
        datebirth= findViewById(R.id.dob_txt);
        phonenumber= findViewById(R.id.phn_txt);
        gendertext= findViewById(R.id.gndr_txt);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            myprofile.setText("Profile Saya");
            usrname.setText("Nama User");
            datebirth.setText("Tanggal Lahir");
            phonenumber.setText("Nomer HP");
            gendertext.setText("Jenis Kelamin");
            upload_server.setText("Perbaharui");
            g1.setText("Laki-Laki");
            g2.setText("Perempuan");
            g3.setText("Lainya");
        }


        dob_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val", "");
        Log.i("Session_Update_activity", session_id);


        fetchData();

        ActivityCompat.requestPermissions(Update_profile_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        Change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent1= new Intent();
                    intent1.setType("image/*");
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent1,10);
                }
                else
                {
                    ActivityCompat.requestPermissions(Update_profile_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });
        //upload image to server
        upload_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_update = name_edit.getText().toString();
                Log.i("sdxghcvhg", name_update);
                phone_update = phone_edit.getText().toString();
                birth_update = dob_edit.getText().toString();
                try {
                    if (path == null)
                    {
                        if (TextUtils.isEmpty(name_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Name is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(phone_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Phone is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(birth_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Birth date is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(Gender))
                        {
                            Toast.makeText(Update_profile_activity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog = new ProgressDialog(Update_profile_activity.this);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            uploadData(name_update, gender_update, phone_update, birth_update);
                        }
                    }
                    else
                    {
                        if (TextUtils.isEmpty(name_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Name is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(phone_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Phone is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(birth_update))
                        {
                            Toast.makeText(Update_profile_activity.this, "Birth date is Empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(Gender))
                        {
                            Toast.makeText(Update_profile_activity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog = new ProgressDialog(Update_profile_activity.this);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                            uploadImgage(path,name_update,phone_update,birth_update,Gender);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //pic img
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Context context = Update_profile_activity.this;
            path = RealPathUtil.getRealPath(context,uri);
            Log.i("imgpth",path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
         //   uploadImg(path);
        }

    }



    private void ShowDialog()
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                Log.i("SelectDOB",simpleDateFormat.format(calendar.getTime()));
                dob_edit.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new DatePickerDialog(Update_profile_activity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void checkButton(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        // Toast.makeText(this, ""+radioButton.getText(), Toast.LENGTH_SHORT).show();
         Gender = (String) radioButton.getText();

    }

    private void fetchData()
    {
        String url = base_app_url+"api/user/profile";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonUserInfo",response.toString());

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
                        username_get = response.getString("name");
                    }

                    String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
                    String image_get;
                    if (response.isNull("image"))
                    {
                        image_get = null;
                        Glide.with(Update_profile_activity.this).load(R.drawable.spf).into(imageView);
                    }
                    else {
                        image_get = response.getString("image");
                        Glide.with(Update_profile_activity.this).load(img_url_base + image_get).into(imageView);
                    }

                    String birth_date_get;
                    if(response.isNull("birth_date"))
                    {
                        birth_date_get = "Select Here";
                    }
                    else
                    {
                        birth_date_get = response.getString("birth_date");
                    }


                    if(!response.isNull("gender"))
                    {
                        Gender = response.getString("gender");
                        if (Gender.equals("Male") || Gender.equals("male"))
                        {
                            g1.setChecked(true);
                        }
                        else if (Gender.equals("Female") || Gender.equals("female"))
                        {
                            g2.setChecked(true);
                        }
                        else if (Gender.equals("Others"))
                        {
                            g3.setChecked(true);
                        }
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

                    name_edit.setText(username_get);
                    dob_edit.setText(birth_date_get);
                    phone_edit.setText(phone_get);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("jsonUserError",error.toString());
                Toast.makeText(Update_profile_activity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void uploadData(String name_update, String gender_update, String phone_update, String birth_update) throws JSONException {
        String url = base_app_url+"api/user/profile";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name_update);
        jsonObject.put("birth_date", birth_update);
        if (!TextUtils.isEmpty(Gender))
        {
            jsonObject.put("gender", Gender);
        }
        jsonObject.put("phone", phone_update);
        final String mrequestBodey = jsonObject.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.i("responseForUpdatPro", response.toString());
                Toast.makeText(Update_profile_activity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
           Intent intent1 = new Intent(Update_profile_activity.this,Others_Activity.class);
            startActivity(intent1);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("ErrorForUpdatePro", error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("profFail", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                          Toast.makeText(Update_profile_activity.this, jsonObject2.toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", session_id);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    private void uploadImgage(String path,String name_update,String phone_update,String birth_update,String Gender) {
        try {
            File file = new File(path);
            Log.e(MainActivity.class.getSimpleName(), "uploadImgage: " + file.getPath());
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
                        .addFormDataPart("name",name_update)
                        .addFormDataPart("phone",phone_update)
                        .addFormDataPart("birth_date",birth_update)
                        .addFormDataPart("gender",Gender)
                        .build();
                Request request = new Request.Builder()
                        .url(base_app_url+"api/user/profile")
                        .addHeader("Authorization", session_id)
                        .addHeader("Content-Type", "application/json")
                        .post(requestBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.d("Response", e.toString());
                        progressDialog.dismiss();
                        Toast.makeText(Update_profile_activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d("Response", response.body().string());
                        progressDialog.dismiss();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(Update_profile_activity.this,Others_Activity.class);
                                startActivity(intent1);
                                Toast toast = Toast.makeText(Update_profile_activity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                    }
                });
            }
        } catch (Exception e) {
            Log.d("Response", e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(Update_profile_activity.this,Others_Activity.class);
        startActivity(intent1);
    }
}


