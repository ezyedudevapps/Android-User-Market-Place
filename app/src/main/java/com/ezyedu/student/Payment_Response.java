package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ezyedu.student.adapter.SaveOrderAdapter;
import com.ezyedu.student.model.Cart_Details;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Save_Orders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Payment_Response extends AppCompatActivity {

    TextView tr,ref,amount,pay_msg,view_order_button,ordr_dtl_txt;
    ImageView imageView;
    SharedPreferences sp1;
    private List<Cart_Details> cart_detailsList;
    String json,ref_no,tr_id,session_id = null;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    private List<Save_Orders> save_ordersList = new ArrayList<>();
    SaveOrderAdapter saveOrderAdapter;
    ProgressDialog progressDialog;
    @SuppressLint("SetTextI18n")




    //retrive base url
            Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__response);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        ref = findViewById(R.id.ref_num);
        tr = findViewById(R.id.Transaction_id);
        amount = findViewById(R.id.amt);
        pay_msg = findViewById(R.id.Payment_Response);
        imageView = findViewById(R.id.payment_icon);
        view_order_button = findViewById(R.id.order_page_btn);
        view_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Payment_Response.this,History_Page.class);
                startActivity(intent1);
            }
        });
        ordr_dtl_txt = findViewById(R.id.order_dtl_txt);

        Bundle bundle = getIntent().getExtras();
        String resultCd = bundle.getString("resultcd");
        String resultMessage = bundle.getString("resultMessage");
        tr_id = bundle.getString("Transaction");
        ref_no = bundle.getString("ReferenceNumber");
        String amt = bundle.getString("amt");

        sp1 = getApplicationContext().getSharedPreferences("Hash_Count", Context.MODE_PRIVATE);


        loadCartDetails();

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cart_activity",session_id);


        tr.setText("tXiD : "+tr_id);
        ref.setText("Ref Number : "+ref_no);
        amount.setText("Amount : "+amt);


        recyclerView = findViewById(R.id.order_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        saveOrderAdapter = new SaveOrderAdapter(this,save_ordersList);
        recyclerView.setAdapter(saveOrderAdapter);


        if (resultCd.equals("0000"))
        {
            pay_msg.setText("Payment Success");
            pay_msg.setTextColor(getResources().getColor(R.color.green));
            Glide.with(this).load(R.drawable.payment_success_icon).into(imageView);
            try {
                int status = 1;
                int type = 1;
                SaveOrder(ref_no,tr_id,json,status,type);
                progressDialog = new ProgressDialog(Payment_Response.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            pay_msg.setText("Payment Failed");
            pay_msg.setTextColor(getResources().getColor(R.color.red));
            Glide.with(this).load(R.drawable.failed_payment_icon).into(imageView);
            int status = 0;
            int type = 1;
            try {
                SaveOrder(ref_no,tr_id,json,status,type);
                progressDialog = new ProgressDialog(Payment_Response.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveOrder(String ref_no, String tr_id, String json, int status, int type) throws JSONException {
        String url = base_app_url+"api/payment/save";
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("referenceNo",ref_no);
        jsonObject.put("txid",tr_id);
        jsonObject.put("status",status);
        jsonObject.put("type",type);
        jsonObject.put("courses",jsonArray);
        Log.i("JSONArrayToSave",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                Log.i("JsonOrderedDetails",response.toString());
                try {
                    JSONObject jsonObject1 = response.getJSONObject("payment");
                    String message = jsonObject1.getString("message");
                    String reference_no = jsonObject1.getString("reference_no");
                    String amount = jsonObject1.getString("amount");
                    JSONArray jsonArray1 = response.getJSONArray("order");
                    for (int i = 0; i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        String order_ref_id = jsonObject2.getString("order_ref_id");
                        String vendor_name = jsonObject2.getString("vendor_name");
                        Double final_amount = jsonObject2.getDouble("amount");
                        if (message.equals("Success"))
                        {
                            ordr_dtl_txt.setVisibility(View.VISIBLE);
                            view_order_button.setVisibility(View.VISIBLE);
                            Save_Orders post = new Save_Orders(order_ref_id,vendor_name,final_amount);
                            save_ordersList.add(post);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Log.i("JsonOrderedError",error.toString());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    Log.i("RegisterFailure", jsonError.toString());
                    try {
                        JSONObject jsonObject1= new JSONObject(jsonError);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("errors");
                        Log.i("message",jsonObject2.toString());
                        Toast.makeText(Payment_Response.this, jsonObject2.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        })
        {
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
    private void loadCartDetails()
    {
        Gson gson = new Gson();
        json = sp1.getString("HashCountValues",null);
        Type type = new TypeToken<ArrayList<Cart_Details>>() {}.getType();
        cart_detailsList = gson.fromJson(json,type);
        Log.i("ArraySizeCheck", String.valueOf(cart_detailsList.size()));
        Log.i("jsonCheck",json);
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Payment_Response.this,MainActivity.class);
        startActivity(intent1);
    }
}