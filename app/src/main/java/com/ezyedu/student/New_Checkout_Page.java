package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class New_Checkout_Page extends AppCompatActivity {

    String session_id = null;
    RequestQueue requestQueue;

    ImageView back_btn;
    EditText cardname,cardnumber,ex_month,ex_yr,cvs,billing_address,postal_code;
    String get_cardname,get_cardnumber,get_ex_month,get_ex_yr,get_cvs,get_billing_address,get_postal_code;
    Button button;
    TextView Tot_price_txt;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    int bank_rate_id;
    int new_pay_type;

    //new payment details...
    int new_amount;
    int new_pay_month;
    String new_timestamp;
    String new_imId;
    String new_referenceNo;
    String new_merchantoken;
    TextView bank_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__checkout__page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_checkout",session_id);

        back_btn = findViewById(R.id.bck_py);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(New_Checkout_Page.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });


        cardname = findViewById(R.id.cardnameedittext);
        cardnumber = findViewById(R.id.cardNumberEditText);
        ex_month = findViewById(R.id.cardmonthedittext);
        ex_yr = findViewById(R.id.cardyearedittext);
        cvs = findViewById(R.id.cardCVCEditText);
        billing_address = findViewById(R.id.billaddressget);
        postal_code = findViewById(R.id.billpostalget);
        button = findViewById(R.id.pay_btn);
        Tot_price_txt = findViewById(R.id.amt_new);

        bank_rate_id = getIntent().getIntExtra("bank_rate_id",0);
        Log.i("valuse_bank_rate_id", String.valueOf(bank_rate_id));


        bank_name = findViewById(R.id.bnk_nm);
        if (bank_rate_id == 0)
        {
            new_pay_type = 1;
            bank_name.setText("-");
        }
        else {
            new_pay_type = 2;
            bank_name.setText("BRI");
        }

        try {
            RegisterPayment();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_cardname = cardname.getText().toString();
                get_cardnumber = cardnumber.getText().toString();
                get_ex_month  = ex_month.getText().toString();
                get_ex_yr  = ex_yr.getText().toString();
                get_cvs  = cvs.getText().toString();
                get_billing_address  = billing_address.getText().toString();
                get_postal_code  = postal_code.getText().toString();

                if (TextUtils.isEmpty(get_cardnumber))
                {
                    Toast.makeText(New_Checkout_Page.this, "Please Enter the Card Number", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_cvs))
                {
                    Toast.makeText(New_Checkout_Page.this, "Please Enter the CVV", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_ex_yr))
                {
                    Toast.makeText(New_Checkout_Page.this, "Year Field Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_cardname))
                {
                    Toast.makeText(New_Checkout_Page.this, "Name Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_ex_month))
                {
                    Toast.makeText(New_Checkout_Page.this, "Expiry month Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_billing_address))
                {
                    Toast.makeText(New_Checkout_Page.this, "Billing Address Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(get_postal_code))
                {
                    Toast.makeText(New_Checkout_Page.this, "Postal code Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent1 = new Intent(New_Checkout_Page.this, Payment_webPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TPR", String.valueOf(new_amount));
                    bundle.putString("Name", get_cardname);
                    bundle.putString("CVV", get_cvs);
                    bundle.putString("yr", get_ex_yr);
                    bundle.putString("CardNumber", get_cardnumber);
                    bundle.putString("Month", get_ex_month);
                    bundle.putString("payMonth", String.valueOf(new_pay_month));
                    bundle.putString("payType", String.valueOf(new_pay_type));
                    bundle.putString("timestamp",new_timestamp);
                    bundle.putString("imId",new_imId);
                    bundle.putString("referenceNo",new_referenceNo);
                    bundle.putString("merchantoken",new_merchantoken);
                    bundle.putString("billing_address",get_billing_address);
                    bundle.putString("billing_postal_code",get_postal_code);
                    Log.i("paying_Details", String.valueOf(bundle));
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });




    }

    private void RegisterPayment() throws JSONException {
          String url =base_app_url+"api/payment/register-items";
        //String url = base_app_url+ "api/payment/test-register-items";
        JSONObject jsonObject = new JSONObject();
        if (bank_rate_id != 0)
        {
            jsonObject.put("bank_rate_id",bank_rate_id);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonPayFirstResponse",response.toString());
                try {
                    JSONObject jsonObject1 = response.getJSONObject("data");

                    new_timestamp = jsonObject1.getString("timestamp");
                    new_imId = jsonObject1.getString("imId");
                    new_referenceNo = jsonObject1.getString("referenceNo");
                    new_merchantoken = jsonObject1.getString("merchantoken");

                    new_amount = jsonObject1.getInt("amt");
                    new_pay_month = jsonObject1.getInt("month");

                    int new_amt_per_month = jsonObject1.getInt("amt_per_month");

                    if(new_pay_month == 1)
                    {
                        Tot_price_txt.setText(String.valueOf("Rp : "+new_amount));
                    }
                    else
                    {
                        Tot_price_txt.setText(String.valueOf(new_pay_month+"x"+ " @0%  Rp : "+new_amt_per_month+ " /month"));
                    }

                    // RegisterCard(timestamp,imId,referenceNo,amt,merchantoken);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonPayFirstError",error.toString());
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