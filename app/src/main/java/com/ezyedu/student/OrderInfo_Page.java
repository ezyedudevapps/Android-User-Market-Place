package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ezyedu.student.adapter.OrderInfoAdapter;
import com.ezyedu.student.model.BottomSheetDialog;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.OrderInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderInfo_Page extends AppCompatActivity implements BottomSheetDialog.BottomSheetListner {
    RequestQueue requestQueue;
    String session_id = null;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    TextView pend_status,order_id,ven_name,date,actual_price,discount,final_price,total_items;
    ImageView imageButton;
    OrderInfoAdapter orderInfoAdapter;
    RecyclerView recyclerView;
    Button button;
    int id,order_status_code;
    RelativeLayout relativeLayout;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    //storing order_status code.. for bottom sheet....
    SharedPreferences sp;
    //proceed to confirm from bottom sheet
    TextView textView;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info__page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_order_activity",session_id);


        //storing status code
        sp = getSharedPreferences("order_status", Context.MODE_PRIVATE);

        id = getIntent().getIntExtra("id",0);

        pend_status = findViewById(R.id.pending_status);
        order_id = findViewById(R.id.ord_ide);
        ven_name = findViewById(R.id.od_ven);
        date = findViewById(R.id.od_dt);
        actual_price = findViewById(R.id.od_price);
        discount = findViewById(R.id.od_discount);
        final_price = findViewById(R.id.od_final);
        total_items = findViewById(R.id.itms_count);
        imageButton = findViewById(R.id.others_btn);

        relativeLayout = findViewById(R.id.relative_status);
        textView = findViewById(R.id.ct_now);

        button = findViewById(R.id.chat_ven);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(),"exampleBottomSheet");
            }
        });

        recyclerView = findViewById(R.id.od_recyc);
        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        orderInfoAdapter = new OrderInfoAdapter(OrderInfo_Page.this,orderInfoList);
        recyclerView.setAdapter(orderInfoAdapter);
        getOrderDetails();

    }

    private void getOrderDetails()
    {
        String url = base_app_url+"api/order/"+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseOrderInfo",response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("order_details");
                    int ordr_id = jsonObject.getInt("id");
                    String order_ref_id = jsonObject.getString("order_ref_id");
                    Double discounts = jsonObject.getDouble("discount");
                    Double amount = jsonObject.getDouble("amount");
                    Double final_amount = jsonObject.getDouble("final_amount");
                    int order_status = jsonObject.getInt("order_status");


                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("status_code", String.valueOf(order_status));
                    editor.commit();


                    order_status_code = order_status;
                    String created_at = jsonObject.getString("created_at");
                    int vendor_id = jsonObject.getInt("vendor_id");
                    String vendor_name = jsonObject.getString("vendor_name");
                    JSONArray jsonArray = response.getJSONArray("course_details");

                    if (order_status == 1)
                    {
                        relativeLayout.setBackgroundColor(R.color.orange_500);
                        pend_status.setText("Waiting for Vendor Approval");
                    }
                    else if (order_status == 2)
                    {
                        pend_status.setText("Please Proceed to Continue");
                        relativeLayout.setBackgroundColor(R.color.ezy);
                    }
                    else if (order_status == 3)
                    {
                        pend_status.setText("Waiting For Vendor's Confirmation");
                        relativeLayout.setBackgroundColor(R.color.ezy);
                    }
                   else if (order_status == 4)
                    {
                        pend_status.setText("Completed");
                        relativeLayout.setBackgroundColor(R.color.green);
                        imageButton.setVisibility(View.GONE);


                    }
                   else if (order_status == 5)
                    {
                        pend_status.setText("Vendor Cancelled Your Order");
                        relativeLayout.setBackgroundColor(Color.RED);
                        imageButton.setVisibility(View.GONE);

                    }
                    else if (order_status == 6)
                    {
                        pend_status.setText("Cancelled");
                        imageButton.setVisibility(View.GONE);

                        relativeLayout.setBackgroundColor(Color.RED);
                    }
                    else if (order_status == 7)
                    {
                        pend_status.setText("Requested to Cancel");
                        imageButton.setVisibility(View.GONE);
                        relativeLayout.setBackgroundColor(Color.RED);
                    }

                    order_id.setText(order_ref_id);
                    String [] dt = created_at.split(" ");
                    date.setText(dt[0]);

                    actual_price.setText(amount.toString());
                    discount.setText(discounts.toString());
                    final_price.setText(final_amount.toString());
                    ven_name.setText(vendor_name);


                    for (int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject a = jsonArray.getJSONObject(i);
                        String course_id = a.getString("course_id");
                        String course_name = a.getString("course_name");
                        String course_image = a.getString("course_image");
                        Double course_amount = a.getDouble("course_amount");
                        int course_qty = a.getInt("course_qty");
                        OrderInfo post = new OrderInfo(course_id,course_name,course_image,course_amount,course_qty);
                        orderInfoList.add(post);
                        Log.i("listArraySize", String.valueOf(orderInfoList.size()));
                        total_items.setText(String.valueOf(orderInfoList.size())+" Items");
                        recyclerView.getAdapter().notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("ErrorOrderInfo",error.toString());
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
    public void onButtonClicked(String text) throws JSONException {
      //  Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        //cancel the order
        if (text.equals("cancel"))
        {
            if (order_status_code == 1)
            {
                AlertDialog dig = new AlertDialog.Builder(OrderInfo_Page.this).setTitle("Please Select").setMessage("Are you sure want to cancel the Order ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                    try {
                                        String url = base_app_url+"api/order/"+id;
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("status",6);
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response)
                                            {
                                                try {
                                                    String message = response.getString("message");
                                                    if (message.equals("Success"))
                                                    {
                                                        Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(OrderInfo_Page.this, History_Page.class);
                                                        startActivity(intent1);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

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
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dig.show();
            }
            else if (order_status_code == 2 ||order_status_code == 3) {
                AlertDialog dig = new AlertDialog.Builder(OrderInfo_Page.this).setTitle("Please Select").setMessage("Request to cancel the Order ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                    try {
                                        String url = base_app_url+"api/order/" + id;
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("status", 7);
                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    String message = response.getString("message");
                                                    if (message.equals("Success")) {
                                                        Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(OrderInfo_Page.this, History_Page.class);
                                                        startActivity(intent1);
                                                    } else {
                                                        Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }) {
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("Content-Type", "application/json");
                                                params.put("Authorization", session_id);
                                                return params;
                                            }
                                        };
                                        requestQueue.add(jsonObjectRequest);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dig.show();

            }
            else if (order_status_code == 7)
            {
                AlertDialog dig = new AlertDialog.Builder(OrderInfo_Page.this).setTitle("Please Select").setMessage("Are you sure want to undo the  cancel Request ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                try {
                                    String url = base_app_url+"api/order/"+id;
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("status",3);
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response)
                                        {
                                            try {
                                                String message = response.getString("message");
                                                if (message.equals("Success"))
                                                {
                                                    Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                    Intent intent1 = new Intent(OrderInfo_Page.this, History_Page.class);
                                                    startActivity(intent1);
                                                }
                                                else
                                                {
                                                    Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dig.show();
            }
        }
        //redirect to chat
        else if(text.equals("proceed"))
        {
            if (order_status_code == 2)
            {
                String url = base_app_url+"api/order/"+id;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status",3);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String message = response.getString("message");
                            if (message.equals("Success"))
                            {
                                Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(OrderInfo_Page.this, History_Page.class);
                                startActivity(intent1);
                            }
                            else
                            {
                                Toast.makeText(OrderInfo_Page.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
        }
    }
}