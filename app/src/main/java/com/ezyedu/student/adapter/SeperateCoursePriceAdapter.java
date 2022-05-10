package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezyedu.student.Cart_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.course_seperate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeperateCoursePriceAdapter extends RecyclerView.Adapter<SeperateCoursePriceAdapter.priceHolder> {


    String session_id = null;
    RequestQueue requestQueue;
    private Context context;
    private List<course_seperate> coList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public SeperateCoursePriceAdapter(Context context, List<course_seperate> coList) {
        this.context = context;
        this.coList = coList;
    }

    @NonNull
    @Override
    public priceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_course_price_adapter,parent,false);
        return new priceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull priceHolder holder, int position)
    {
        course_seperate courseSeperate = coList.get(position);
        String rp = "Rp ";
        int price = courseSeperate.getInitial_price();

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formatted = formatter.format(price);


        String.valueOf(price);
     //   if want to remove , use rp+ price
        String price_new = rp+formatted;


        holder.textView.setText(price_new);
        String hash = courseSeperate.getCourse_hash_id();

        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(session_id))
                {
                    try {
                        addCart(hash,session_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }

            }
        });


        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(session_id))
                {
                    try {
                        addCart(hash,session_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addCart(String hash, String s) throws JSONException {
        Log.i("session_val_adap",s);
        Log.i("hash_adap",hash);



        String url = base_app_url+"api/user/cart";
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(hash);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course_ids",jsonArray);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("addedToCart",response.toString());
                Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                Intent intent1= new Intent(context, Cart_Activity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
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
                params.put("Authorization",s);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        return coList == null ?0: coList.size();
    }

    public class priceHolder extends RecyclerView.ViewHolder{
        TextView textView,addToCart,buynow;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public priceHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.price_total);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            buynow= itemView.findViewById(R.id.buy_now_btn);


            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);

            requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
