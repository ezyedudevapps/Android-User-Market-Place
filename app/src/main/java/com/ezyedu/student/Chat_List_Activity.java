package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.adapter.ChatListAdapter;
import com.ezyedu.student.model.ChatList;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat_List_Activity extends AppCompatActivity {


    String session_id = null;
    RequestQueue requestQueue;

    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    ImageView imageView;
    TextView textView;
    private List<ChatList> mlist = new ArrayList<>();


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__list_);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue = CourseVolleySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);

        recyclerView = findViewById(R.id.chat_list_recyc);
        imageView = findViewById(R.id.i11);
        textView=findViewById(R.id.t11);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        chatListAdapter = new ChatListAdapter(Chat_List_Activity.this, mlist);
        recyclerView.setAdapter(chatListAdapter);
        fetchChatList();

    }

    private void fetchChatList()
    {
        String url = base_app_url+"api/chat/list";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("jsonchatlist",response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String Name = jsonObject.getString("receiver_name");
                        Integer receiver_id = jsonObject.getInt("receiver_id");
                        String message = jsonObject.getString("last_message");
                        String image = jsonObject.getString("receiver_image");
                        String Time = jsonObject.getString("time");
                        Integer Unread_Count = jsonObject.getInt("unread_chat");
                        ChatList post = new ChatList(Name,message,image,Time,Unread_Count,receiver_id);
                        mlist.add(post);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (mlist.size() == 0)
                    {
                        Toast.makeText(Chat_List_Activity.this, "No Chat Found", Toast.LENGTH_SHORT).show();
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("jsonchatError",error.toString());
                Toast.makeText(Chat_List_Activity.this, "No Chats Found", Toast.LENGTH_SHORT).show();
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