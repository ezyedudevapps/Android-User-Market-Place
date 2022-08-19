package com.ezyedu.student.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.ezyedu.student.Login_Activity;
import com.google.gson.Gson;
import com.ezyedu.student.Cart_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Cart_Details;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>
{
    private Context context;
    private List<cart> cartList = new ArrayList<>();
    private List<Cart_Details> cart_detailsList = new ArrayList<>();
    RequestQueue requestQueue;
    String session_id = null;
    Double price = 0.0;
    Double Total_price = 0.0;
    SharedPreferences sp,sp1;

    ProgressDialog progressDialog;

    public  static String img_url_base;
    public static  String base_app_url;

    public CartAdapter(Context context, List<cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_adapter,parent,false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position)
    {
        cart a = cartList.get(position);
        final int[] count = {1};
        holder.institution.setText(a.getVendor_name());
        holder.tittle.setText(a.getTitle());
        Log.i("tittleCart",holder.tittle.toString());
        holder.date.setText(a.getStart_date());
        price = a.getPrice();
        Log.i("price",String.valueOf(price));
        String rp = "Rp ";
        holder.price.setText(String.valueOf(rp + price));
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        String path = a.getImage();
        Glide.with(context).load(img_url_base+path).into(holder.imageView);

        Total_price = Total_price+a.getPrice();
        Log.i("Total_price_cart", String.valueOf(Total_price));
        Log.i("Total_cart_price", String.valueOf(Total_price));

        Log.i("TotCount", String.valueOf(count[0]));
        holder.decrement_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!(count[0] <=1))
                {
                    count[0]--;
                    Log.i("TotCount", String.valueOf(count[0]));
                    try {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        updateCartQty(a.getCourse_hash_id(),count[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    holder.count_btn.setText(""+ count[0]);
                    price = a.getPrice();
                    holder.price.setText("Rp"+ count[0] *a.getPrice());
                    Total_price = Total_price - a.getPrice();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Tota1_prc", String.valueOf(Total_price));
                    editor.apply();
                    Log.i("Total_price_sent",Total_price.toString());

                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1 = sp1.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(cart_detailsList);
                    editor1.putString("HashCountValues",json);
                    editor1.apply();
                }
            }
        });

        holder.increment_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                count[0]++;
                Log.i("TotCount", String.valueOf(count[0]));
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    updateCartQty(a.getCourse_hash_id(),count[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.count_btn.setText(""+ count[0]);
                price = a.getPrice();
                holder.price.setText("Rp"+ count[0] *a.getPrice());
                Total_price  = Total_price+a.getPrice();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Tota1_prc", String.valueOf(Total_price));
                editor.apply();
                Log.i("Total_price_sent",Total_price.toString());
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1 = sp1.edit();
                Gson gson = new Gson();
                String json = gson.toJson(cart_detailsList);
                editor1.putString("HashCountValues",json);
                editor1.apply();

            }
        });
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Tota1_prc", String.valueOf(Total_price));
        editor.commit();
        Log.i("Total_price_sent",Total_price.toString());



        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hash = a.getCourse_hash_id();
                Log.i("hash_remove",hash);

                if (!TextUtils.isEmpty(session_id))
                {
                    AlertDialog dig = new AlertDialog.Builder(context).setTitle("Please Select").setMessage("Are you Sure Want to Remove this Item ? ?").
                            setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                        try {
                                            removeCart(session_id,hash);
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
        });

        Cart_Details c = new Cart_Details(a.getCourse_hash_id(),count);
        cart_detailsList.add(c);
        Log.i("ArraySizeValCart", String.valueOf(cart_detailsList.size()));

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1 = sp1.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cart_detailsList);
        editor1.putString("HashCountValues",json);
        editor1.apply();

        }

    private void updateCartQty(String course_hash_id, int i) throws JSONException {
        String url = base_app_url+"/api/user/cart-qty";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course_id",course_hash_id);
        jsonObject.put("qty",i);
        Log.i("qtyJson",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    if (message.equals("qty added"))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    private void removeCart(String session_id, String hash) throws JSONException {
        String url = base_app_url+"api/user/cart";

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(hash);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("course_ids",jsonArray);
        Log.i("objrem",jsonObject.toString());


        //updating cart
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    Log.i("removdsuccess", response.toString());
                    String message = response.getString("message");
                    Log.i("messagetag",message);
                    Toast.makeText(context, "Removed Succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Cart_Activity.class);
                    context.startActivity(intent);
                }
                catch (Exception e)
                {
                    Log.i("excepremove",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("removingError",error.toString());
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
    public int getItemCount() {
        return cartList == null ?0: cartList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder{
        TextView institution,remove,tittle,date,price,decrement_btn,increment_btn,count_btn;
        ImageView imageView;



        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public CartHolder(@NonNull View itemView) {

            super(itemView);

            institution = itemView.findViewById(R.id.Cart_insti_name);
            remove = itemView.findViewById(R.id.remove_cart_course);
            tittle = itemView.findViewById(R.id.Cart_course_tittle);
            date = itemView.findViewById(R.id.Cart_date_txt);
            price = itemView.findViewById(R.id.Cart_course_price);

            imageView = itemView.findViewById(R.id.cart_course_img);

            decrement_btn = itemView.findViewById(R.id.decrement);
            increment_btn = itemView.findViewById(R.id.increment);
            count_btn = itemView.findViewById(R.id.count);

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
            session_id = sharedPreferences.getString("session_val","");
            Log.i("session_new",session_id);



            sp = context.getApplicationContext().getSharedPreferences("Total_price", Context.MODE_PRIVATE);
            sp1 = context.getApplicationContext().getSharedPreferences("Hash_Count",Context.MODE_PRIVATE);

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
