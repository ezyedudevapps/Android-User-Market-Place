package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentMethodActivity extends AppCompatActivity {

    RelativeLayout credit_debot_card,Installment;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    ImageView back_btn;


    int bank_rate_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);



        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
  //      Log.i("img_url_global",img_url_base);



        credit_debot_card = findViewById(R.id.cc_dc);
        Installment = findViewById(R.id.cc_im);

        back_btn = findViewById(R.id.bck_py);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PaymentMethodActivity.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });


        Installment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(PaymentMethodActivity.this,InstallmentPlanActivity.class);
                startActivity(intent1);
            }
        });

        credit_debot_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_rate_id = 0;
                Intent intent1 = new Intent(PaymentMethodActivity.this,New_Checkout_Page.class);
                intent1.putExtra("bank_rate_id",0);
                startActivity(intent1);
            }
        });
    }

}