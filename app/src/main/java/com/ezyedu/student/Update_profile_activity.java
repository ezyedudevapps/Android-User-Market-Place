package com.ezyedu.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView Change_pic;

    private ProgressDialog LoadingBar;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String Gender;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;


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

    public static String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            } else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else if ("pdf".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

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

        Change_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Update_profile_activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Update_profile_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Update_profile_activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openFileChooser();
                } else {
                    ActivityCompat.requestPermissions(Update_profile_activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
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
                    LoadingBar = new ProgressDialog(Update_profile_activity.this);
                    LoadingBar.setTitle("Please Wait");
                    LoadingBar.setMessage("Updating Profile");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();

                    uploadData(name_update, gender_update, phone_update, birth_update);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                        username_get = response.getString("username");
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
                Log.i("responseForUpdatPro", response.toString());
                LoadingBar.dismiss();
                Toast.makeText(Update_profile_activity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(Update_profile_activity.this,Others_Activity.class);
            startActivity(intent1);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LoadingBar.dismiss();
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


    private void uploadImgage(String path) {
        try {
            LoadingBar = new ProgressDialog(this);
            LoadingBar.setTitle("Please Wait");
            LoadingBar.setMessage("Changing Profile Picture");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();

            File file = new File(path);
            Log.e(MainActivity.class.getSimpleName(), "uploadImgage: " + file.getPath());
            Glide.with(this).load(path).into(((ImageView) findViewById(R.id.image_to_update)));
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .build();
            if (file != null) {
                MultipartBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("multipart/form-data")))
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
                        LoadingBar.dismiss();
                        Toast.makeText(Update_profile_activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.d("Response", response.body().string());
                        LoadingBar.dismiss();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(Update_profile_activity.this, "Profile Picture Uploaded Successfully", Toast.LENGTH_SHORT);
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

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // And to convert the image URI to the direct file system path of the image file
    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadImgage(getFilePath(this, data.getData()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "all permission's granted", Toast.LENGTH_LONG).show();
                openFileChooser();
            } else {
                ActivityCompat.requestPermissions(Update_profile_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(Update_profile_activity.this,Others_Activity.class);
        startActivity(intent1);
    }
}


