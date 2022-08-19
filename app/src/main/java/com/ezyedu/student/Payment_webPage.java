package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class Payment_webPage extends AppCompatActivity {

    String session_id = null;
    RequestQueue requestQueue;
    String TPR,CardNumber,CardHolderName,ExpMonth,ExpYear,CVV,pay_type,pay_month,billing_address,billing_postal_code;
    String new_timestamp;
    String new_imId;
    String new_referenceNo;
    String new_merchantoken;
    Double price = 0.0;
    WebView webView;
    private ProgressDialog LoadingBar;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        webView = findViewById(R.id.web_load_pay);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_checkout",session_id);

        Bundle bundle = getIntent().getExtras();
        TPR = bundle.getString("TPR");
        CardNumber = bundle.getString("CardNumber");
        CardHolderName  = bundle.getString("Name");
        ExpMonth = bundle.getString("Month");
        ExpYear =bundle.getString("yr");
        CVV = bundle.getString("CVV");
        pay_type = bundle.getString("payType");
        pay_month = bundle.getString("payMonth");

        new_timestamp = bundle.getString("timestamp");
        new_imId =  bundle.getString("imId");
        new_referenceNo =  bundle.getString("referenceNo");
        new_merchantoken = bundle.getString("merchantoken");
        billing_address = getIntent().getStringExtra("billing_address");
        billing_postal_code = getIntent().getStringExtra("billing_postal_code");


        Log.i("TotalValues",bundle.toString());

        Log.i("ValuesToSend",TPR+" "+CardNumber+" "+CardHolderName+" "+ExpMonth+" "+CVV+" "+ExpYear);
        price = Double.parseDouble(TPR);

        LoadingBar = new ProgressDialog(this);
        LoadingBar.setTitle("Please Wait");
        LoadingBar.setMessage("Loading Payment");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();


        try {
            RegisterCard(new_timestamp,new_imId,new_referenceNo,TPR,new_merchantoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
     /*   try {
            RegisterPayment();
        } catch (JSONException e) {
            e.printStackTrace();
        }

      */

    }

    private void RegisterPayment() throws JSONException {
        //test card
      //  String url = base_app_url+"api/payment/test-register-items";
        //real card
        String url =base_app_url+"api/payment/register-items";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amt",price);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonPayFirstResponse",response.toString());
                try {
                    JSONObject jsonObject1 = response.getJSONObject("data");
                    String timestamp = jsonObject1.getString("timestamp");
                    String imId = jsonObject1.getString("imId");
                    String referenceNo = jsonObject1.getString("referenceNo");
                    String amt = jsonObject1.getString("amt");
                    String merchantoken = jsonObject1.getString("merchantoken");

                 //   RegisterCard(timestamp,imId,referenceNo,amt,merchantoken);

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



    private void RegisterCard(String timestamp, String imId, String referenceNo, String amt, String merchantoken) throws JSONException {
        //dev
      //  String url = "https://dev.nicepay.co.id/nicepay/direct/v2/registration";
        //prod
        String url = "https://staging.nicepay.co.id/nicepay/direct/v2/registration";
        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("timeStamp",timestamp);
        jsonObject.put("iMid",imId);
        jsonObject.put("payMethod","01");
        jsonObject.put("currency","IDR");
        jsonObject.put("amt",amt);
        jsonObject.put("referenceNo",referenceNo);
        jsonObject.put("goodsNm","Test transaction");
        jsonObject.put("dbProcessUrl","https://ptsv2.com/t/test-nicepay-v2");
        jsonObject.put("vat","");
        jsonObject.put("fee","");
        jsonObject.put("notaxAmt","");
        jsonObject.put("description","");
        jsonObject.put("merchantToken",merchantoken);
        jsonObject.put("reqDt","");
        jsonObject.put("reqTm","");
        jsonObject.put("reqDomain","merchant.com");
        jsonObject.put("reqServerIP","127.0.0.1");
        jsonObject.put("reqClientVer","");
        jsonObject.put("userIP","127.0.0.1");
        jsonObject.put("userSessionID","x97CLk6=6habd4usu8mqeh4mpgt4ph63secf7ush");
        jsonObject.put("userAgent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML,like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        jsonObject.put("userLanguage","en-US,en;q=0.9,id;q=0.8");
        jsonObject.put("cartData","{\\\"count\\\":1,\\\"item\\\":[{\\\"img_url\\\":\\\"https://images.ctfassets.net/od02wyo8cgm5/14Njym0dRLAHaVJywF8WFL/1910357dd0da0ae38b61bc8ad3db86e4/cloudflyer_2-fw19-grey_lime-m-g1.png\\\",\\\"goods_name\\\":\\\"Shoe\\\",\\\"goods_detail\\\":\\\"Shoe\\\",\\\"goods_amt\\\":529500}]}");
        jsonObject.put("instmntType",pay_type);
        jsonObject.put("instmntMon",pay_month);
        jsonObject.put("recurrOpt","0");
        jsonObject.put("merFixAcctId","");
        jsonObject.put("billingNm","John Doe");
        jsonObject.put( "billingPhone", "08994142339");
        jsonObject.put("billingPhone", "08994142339");
        jsonObject.put( "billingEmail", "dewantara.tirta@gmail.com");
        jsonObject.put( "billingAddr", billing_address);
        jsonObject.put("billingCity", "Bandung");
        jsonObject.put("billingState", "Jawa Barat");
        jsonObject.put("billingPostCd", billing_postal_code);
        jsonObject.put( "billingCountry", "Indonesia");
        jsonObject.put("deliveryNm", "John Doe");
        jsonObject.put("deliveryPhone", "08994142339");
        jsonObject.put("deliveryAddr", "Jl. Perumnas");
        jsonObject.put("deliveryCity", "Bandung");
        jsonObject.put("deliveryState", "Jawa Barat");
        jsonObject.put("deliveryPostCd", "55281");
        jsonObject.put("deliveryCountry", "Indonesia");

        Log.i("JsonRegisrerCard",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("JsonResponseSecond",response.toString());
                try {
                    String tXid = response.getString("tXid");
                    RedirectToWeb(timestamp,tXid,merchantoken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("JsonErroreSecond",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void RedirectToWeb(String timestamp, String tXid, String merchantoken) {
        //dev
      //  String url = "https://dev.nicepay.co.id/nicepay/direct/v2/payment?timeStamp=" + timestamp + "&tXid=" + tXid + "&merchantToken=" + merchantoken + "&cardNo="+CardNumber+"&cardExpYymm="+ExpYear+ExpMonth+"&cardCvv="+CVV+"&cardHolderNm="+CardHolderName+"&recurringToken=&preauthToken=&clickPayNo=&dataField3=&clickPayToken=&callBackUrl=https://merchant.com/callBackUrl";
        //prod
        String url = "https://staging.nicepay.co.id/nicepay/direct/v2/payment?timeStamp=" + timestamp + "&tXid=" + tXid + "&merchantToken=" + merchantoken + "&cardNo="+CardNumber+"&cardExpYymm="+ExpYear+ExpMonth+"&cardCvv="+CVV+"&cardHolderNm="+CardHolderName+"&recurringToken=&preauthToken=&clickPayNo=&dataField3=&clickPayToken=&callBackUrl=https://merchant.com/callBackUrl";
        Log.i("Url_to_pass", url);
LoadingBar.dismiss();

        //to test url call this method..
        getResponse(url);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            private boolean isRedirected;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("AllUrl", url);
                if (url.contains("nicepay/direct/v2")) {
                    Log.i("Final_response_url", url);
                    getResponse(url);
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (isRedirected) {
                    Log.i("RedirectedUrl", url);
                    getResponse(url);
                }

            }
        });
    }

    private void getResponse(String url)
    {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("stringResponse",response);
                String res = response;
                Document document = Jsoup.parse(res);
                Elements elements = document.select("input");
                Log.i("Selected_data",elements.get(1).attr("value"));
                String resultCd = elements.get(1).attr("name");

                String resultCdValue = elements.get(1).attr("value");
                Log.i("resultOutput",resultCd+" "+resultCdValue);
                String resultMsg = elements.get(2).attr("value");
                Log.i("ResultMessage",resultMsg);
                String txid = elements.get(3).attr("value");
                Log.i("txid_final",txid);
                String reference_Number = elements.get(4).attr("value");
                String amt = elements.get(6).attr("value");
                Log.i("Final_Status",resultCdValue+" "+resultMsg+" "+" "+txid);

                if(resultCdValue.equals("0000")) {
                    Toast.makeText(Payment_webPage.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Payment_webPage.this, resultMsg, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(Payment_webPage.this,Payment_Response.class);
                Bundle bundle = new Bundle();
                bundle.putString("resultcd",resultCdValue);
                bundle.putString("resultMessage",resultMsg);
                bundle.putString("Transaction",txid);
                bundle.putString("ReferenceNumber",reference_Number);
                bundle.putString("amt",amt);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        requestQueue.add(request);
    }

}