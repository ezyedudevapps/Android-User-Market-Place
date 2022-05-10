package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ezyedu.student.model.Cart_Details;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Checkout_Page extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView pay,Tot_price_txt;
    String session_id = null;
    RequestQueue requestQueue;
    EditText name,cardNumber,cvv,year;
    String month = "00";
    SharedPreferences sp1;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int pay_month = 1;
    int pay_type = 1;

    //emi dialog
    TextView categories,emi_calculation;
    Dialog dialog;
    ArrayList<String> mylist = new ArrayList<String>();
    String Total_price;


    private List<Cart_Details> cart_detailsList;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout__page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        pay = findViewById(R.id.pay_now_btn);
        Spinner spinner = findViewById(R.id.get_exp_mm);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Exp_month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_checkout",session_id);

        SharedPreferences s1 = getApplicationContext().getSharedPreferences("Total_price", Context.MODE_PRIVATE);
        Total_price = s1.getString("Tota1_prc","");
        Log.i("Total_price_received",Total_price);
        Tot_price_txt = findViewById(R.id.tot_amt);
        Tot_price_txt.setText(Total_price);

        sp1 = getApplicationContext().getSharedPreferences("Hash_Count",Context.MODE_PRIVATE);
        loadCartDetails();

        name = findViewById(R.id.get_name);
        cvv = findViewById(R.id.get_cvs);
        year = findViewById(R.id.get_exp_yy);
        cardNumber = findViewById(R.id.get_number);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    try {
                    RegisterPayment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             */
                String CardNmbr = cardNumber.getText().toString();

                String CVV = cvv.getText().toString();
                String yr = year.getText().toString();
                String Name = name.getText().toString();

                if (TextUtils.isEmpty(CardNmbr))
                {
                    Toast.makeText(Checkout_Page.this, "Please Enter the Card Number", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(CVV))
                {
                    Toast.makeText(Checkout_Page.this, "Please Enter the CVV", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(yr))
                {
                    Toast.makeText(Checkout_Page.this, "Year Field Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(Name))
                {
                    Toast.makeText(Checkout_Page.this, "Name Should Not be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i("CardVal", CVV);
                    Intent intent1 = new Intent(Checkout_Page.this, Payment_webPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TPR", Total_price);
                    bundle.putString("Name", Name);
                    bundle.putString("CVV", CVV);
                    bundle.putString("yr", yr);
                    bundle.putString("CardNumber", CardNmbr);
                    bundle.putString("Month", month);
                    bundle.putString("payMonth", String.valueOf(pay_month));
                    bundle.putString("payType", String.valueOf(pay_type));
                    Log.i("paying_Details", String.valueOf(bundle));
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });



        radioGroup = findViewById(R.id.radio_pay_mode);
        radioButton = findViewById(R.id.fuul_py_radio);
        radioButton.setChecked(true);
        Log.i("pay_count",String.valueOf(pay_month));

        emi_calculation = findViewById(R.id.emi_plan);

        try{

            double i = Double.parseDouble(Total_price) /3;
            double j =  Double.parseDouble(Total_price)  /6;
            double k =  Double.parseDouble(Total_price)  /9;
            double l = Double.parseDouble(Total_price) /12;



            int Three_month = (int) i;
            int Six_month = (int) j;
            int Nine_month = (int) k;
            int Twelve_month = (int) l;



            String a = String.valueOf(Three_month);
            String b = String.valueOf(Six_month);
            String c = String.valueOf(Nine_month);
            String d = String.valueOf(Twelve_month);


            String three_month = "3 Months - Rp "+a+" /Month";
            String six_month = "6 Months - Rp "+b+" /Month";
            String nine_month = "9 Months - Rp "+c+" /Month";
            String twelve_month = "12 Months - Rp "+d+" /Month";

            mylist.add(three_month);
            mylist.add(six_month);
            mylist.add(nine_month);
            mylist.add(twelve_month);
        } catch(NumberFormatException ex)
        {
            Log.i("ExceptionNum", String.valueOf(ex));
        }

    }

    public void checkButton(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        // Toast.makeText(this, ""+radioButton.getText(), Toast.LENGTH_SHORT).show();
        String message = (String) radioButton.getText();
        if (message.equals("Full Payment"))
        {
            emi_calculation.setVisibility(View.GONE);
            pay_month = 1;
            pay_type=1;
            Log.i("pay_count",String.valueOf(pay_month));
        }
        else if (message.equals("Installment"))
        {
            Log.i("pay_count",String.valueOf(pay_month));
            dialog = new Dialog(Checkout_Page.this);
            dialog.setContentView(R.layout.emi_spinner);
            dialog.getWindow().setLayout(850,800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();

            ListView listView = dialog.findViewById(R.id.list_view);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Checkout_Page.this,android.R.layout.simple_list_item_1,mylist);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    emi_calculation.setVisibility(View.VISIBLE);
                    emi_calculation.setText(adapter.getItem(position));
                    int pos = position;
                    Log.i("catPos", String.valueOf(pos));
                    pay_type =2;
                    if (pos == 0)
                    {
                        pay_month =3;
                    }
                    else if (pos ==1)
                    {
                        pay_month =6;
                    }
                    else if (pos ==2)
                    {
                        pay_month =9;
                    }
                    else if (pos == 3)
                    {
                        pay_month =12;
                    }
                    dialog.dismiss();
                }
            });
        }
    }


    private void loadCartDetails()
    {
        Gson gson = new Gson();
        String json = sp1.getString("HashCountValues",null);
        Type type = new TypeToken<ArrayList<Cart_Details>>() {}.getType();
        cart_detailsList = gson.fromJson(json,type);
        Log.i("ArraySizeCheck", String.valueOf(cart_detailsList.size()));
        Log.i("jsonCheck",json);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        month = parent.getItemAtPosition(position).toString();
        String []a = month.split("-");
        month = a[0];
       // Toast.makeText(this, month, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void RegisterPayment() throws JSONException {
        String url =base_app_url+"api/payment/register-items";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amt",100);

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

                    RegisterCard(timestamp,imId,referenceNo,amt,merchantoken);

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
        jsonObject.put("instmntType","2");
        jsonObject.put("instmntMon","1");
        jsonObject.put("recurrOpt","0");
        jsonObject.put("merFixAcctId","");
        jsonObject.put("billingNm","John Doe");
        jsonObject.put( "billingPhone", "08994142339");
        jsonObject.put("billingPhone", "08994142339");
        jsonObject.put( "billingEmail", "dewantara.tirta@gmail.com");
           jsonObject.put( "billingAddr", "Jl. Perumnas");
            jsonObject.put("billingCity", "Bandung");
            jsonObject.put("billingState", "Jawa Barat");
            jsonObject.put("billingPostCd", "55281");
           jsonObject.put( "billingCountry", "Indonesia");
            jsonObject.put("deliveryNm", "John Doe");
            jsonObject.put("deliveryPhone", "08994142339");
            jsonObject.put("deliveryAddr", "Jl. Perumnas");
            jsonObject.put("deliveryCity", "Bandung");
            jsonObject.put("deliveryState", "Jawa Barat");
            jsonObject.put("deliveryPostCd", "55281");
            jsonObject.put("deliveryCountry", "Indonesia");

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

    private void RedirectToWeb(String timestamp, String tXid, String merchantoken) {
        String url = "https://staging.nicepay.co.id/nicepay/direct/v2/payment?timeStamp=" + timestamp + "&tXid=" + tXid + "&merchantToken=" + merchantoken + "&cardNo=5576920037842624&cardExpYymm=2308&cardCvv=282&cardHolderNm=Jonathan Julian&recurringToken=&preauthToken=&clickPayNo=&dataField3=&clickPayToken=&callBackUrl=https://merchant.com/callBackUrl";
        Log.i("Url_to_pass", url);
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
             if (resultCd.equals("resultCd"))
             {
                 Toast.makeText(Checkout_Page.this, resultMsg, Toast.LENGTH_SHORT).show();
                 Log.i("resultMessage",resultMsg);
             }
             else {
                 Intent intent1 = new Intent(Checkout_Page.this,Payment_webPage.class);
                 intent1.putExtra("LoadUrl",url);
                 Log.i("loading_url",url);
                 startActivity(intent1);
             }
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