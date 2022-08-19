package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.ExpandableAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InstallmentPlanActivity extends AppCompatActivity {

    String session_id = null;
    RequestQueue requestQueue;

    ArrayList<String> mylist = new ArrayList<String>();
    ArrayList<Integer> plist = new ArrayList<Integer>();

    ArrayList<String> arrayList = new ArrayList<>();
    TextView BRI;

    Dialog dialog;

    TextView Continue_btn;
    int pos;
    int id;

    //expandable list....
    ExpandableListView expandableListView;
    ArrayList<String> bankGroup = new ArrayList<>();
    HashMap<String,ArrayList<String>> childbankgroup = new HashMap<>();
    ExpandableAdapter adapter;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment_plan);

        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();


        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
  //      Log.i("img_url_global",img_url_base);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_new_activity",session_id);

        expandableListView = findViewById(R.id.exp_list_view);
        adapter = new ExpandableAdapter(bankGroup,childbankgroup,plist);
        expandableListView.setAdapter(adapter);


        back_btn = findViewById(R.id.bck_py);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(InstallmentPlanActivity.this,Cart_Activity.class);
                startActivity(intent1);
            }
        });


        Continue_btn = findViewById(R.id.continue_ins_pay);
        Continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstallmentPlanActivity.this, New_Checkout_Page.class);
                intent.putExtra("bank_rate_id",id);
                startActivity(intent);
            }
        });


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

      /*  for(int g = 0;g<=10;g++)
        {
            bankGroup.add("Group"+ g);
            ArrayList<String> arrayList = new ArrayList<>();
            for (int c = 0; c<=5;c++)
            {
                arrayList.add("c"+c);
                adapter.notifyDataSetChanged();
            }
            adapter.notifyDataSetChanged();
            childbankgroup.put(bankGroup.get(g),arrayList);
        }
       */
        fetchData();

        BRI = findViewById(R.id.txt_categories);
        BRI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(InstallmentPlanActivity.this);
                dialog.setContentView(R.layout.dialog_installment_spinner);
                dialog.getWindow().setLayout(800,500);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ListView listView = dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(InstallmentPlanActivity.this,android.R.layout.simple_list_item_1,mylist);
                listView.setAdapter(adapter);


        
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                      //  categories.setText(adapter.getItem(position));
                        pos = position;
                        Log.i("InsPos", String.valueOf(pos));
                        //dialog.dismiss();


                        Log.i("valueselected",arrayList.get(position));


                       // bank_id = plist.get(position);
                        //Log.i("Hash_id_val", String.valueOf(bank_id));
                    }
                });
            }
        });

    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String SChild = intent.getStringExtra("quantity");

            String [] a = SChild.split(":");
            String idd = a[0];
            id = Integer.parseInt(idd);
            Continue_btn.setVisibility(View.VISIBLE);
            Continue_btn.setText("Continue with "+a[1]);
        }
    };
    private void fetchData()
    {
        String url = base_app_url+"api/bank/rate";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("respooinseINP",response.toString());
                    String message = response.getString("message");
                    if (message.equals("success"))
                    {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0;i<=jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int bank_id = jsonObject.getInt("bank_id");
                            String bank_name = jsonObject.getString("bank_name");
                            bankGroup.add(bank_name);
                            adapter.notifyDataSetChanged();

                            JSONArray jsonArray1 = jsonObject.getJSONArray("rate");
                            for (int j = 0;j<jsonArray1.length();j++)
                            {
                                try {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    int id = jsonObject1.getInt("id");
                                    String rate = jsonObject1.getString("rate");
                                    int month = jsonObject1.getInt("month");
                                    double amount_per_month = jsonObject1.getDouble("amount_per_month");

                                    String a = id+":" +String.valueOf(month) + "x @ "+ String.valueOf(rate) + "   Rp" + String.valueOf(amount_per_month) + " /month";
                                    Log.i("Emaiplans",a);
                                    //exp list
                                    arrayList.add(a);
                                    adapter.notifyDataSetChanged();
                                    mylist.add(String.valueOf(id)+":" +String.valueOf(month) + "x @ "+ String.valueOf(rate) + "   Rp" + String.valueOf(amount_per_month) + " /month");
                                    plist.add(id);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Log.i("dataCatch",e.toString());
                                }

                            }
                            childbankgroup.put(bankGroup.get(i),arrayList);
                        }
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

    public void getid(int pos)
    {
        int ins_id = plist.get(pos);
        Log.i("installment_id", String.valueOf(ins_id));
    }

}