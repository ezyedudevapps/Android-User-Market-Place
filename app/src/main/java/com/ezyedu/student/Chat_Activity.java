package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.GetChatAdapter;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.GetChat;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chat_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editText;
    ImageButton imageButton;
    GetChatAdapter getChatAdapter;
    private List<GetChat> mlist = new ArrayList<>();
    String Institute = null;
    Integer vendor_id;
    TextView textView;
    RequestQueue requestQueue;
    String txt = null;
    SharedPreferences sp,sp1;
    String session_id = null;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_chat_activity",session_id);


        imageButton = findViewById(R.id.send_chat_btn);
        editText = findViewById(R.id.text_get);
        Institute = getIntent().getStringExtra("Institution_name");
        Log.i("ven_inf_name",Institute);
        vendor_id = getIntent().getIntExtra("Vendor_id",0);
        Log.i("ven_inf_id",vendor_id.toString());
        //storing vendor chat id
        sp = getSharedPreferences("ven_ch_ide", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vendor_chat_id", String.valueOf(vendor_id));
        editor.commit();

        textView = findViewById(R.id.institution_nm);
        textView.setText("Welcome to "+Institute);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();

        recyclerView = findViewById(R.id.chat_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        getChatAdapter = new GetChatAdapter(this,mlist);
        recyclerView.setAdapter(getChatAdapter);
       // recyclerView.scrollToPosition(mlist.size()-1);
        fetchData(vendor_id);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

              //  mlist.clear();
                fetchData(vendor_id);
                handler.postDelayed(this,5000);
            }
        };handler.postDelayed(runnable,5000);

   /*     editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(mlist.size()-1);
                getChatAdapter.notifyDataSetChanged();
            }
        });
    */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    txt = editText.getText().toString();
                    if (TextUtils.isEmpty(txt))
                    {
                        Toast.makeText(Chat_Activity.this, "Message Field Shold not be empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendText(txt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendText( String txt) throws JSONException {
        editText.setText(null);
        //333 is a dummy value
        GetChat a = new GetChat(333,txt);
        mlist.add(a);
        recyclerView.scrollToPosition(mlist.size()-1);
      //  getChatAdapter.notifyDataSetChanged();

        String base = base_app_url+"api/chat/user/";
        String url = base+vendor_id;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",txt);
        final String mRequestBody = jsonObject.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("jsonChatsend",response.toString());

             //   fetchData(vendor_id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonchatFaill",error.toString());
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

    private void fetchData(Integer vendor_id) {
        String base = base_app_url+"api/chat/user/";
        String url = base+vendor_id;
        Log.i("url_chat_full",url);
        Log.i("vendor_passed_id",vendor_id.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonObjChat",response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.i("arrJSLen", String.valueOf(jsonArray.length()));
                    if (mlist.size()== jsonArray.length())
                    {
                        Log.i("Nfgvh","No Data");
                    }
                    else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Integer sender_id = jsonObject.getInt("sender_id");
                            String message = jsonObject.getString("content");
                            Log.i("first_message", message);
                            GetChat post = new GetChat(sender_id, message);
                            mlist.add(post);
                            Log.i("ArSiz", String.valueOf(mlist.size()));
                        }
                        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                        recyclerView.scrollToPosition(mlist.size()-1);
                    }
                    //getChatAdapter.notifyItemInserted(mlist.size());
                   // getChatAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonChatError",error.toString());

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
}