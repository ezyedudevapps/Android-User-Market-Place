package com.ezyedu.student.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ezyedu.student.Article_details_activity;
import com.ezyedu.student.Bookmarks_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.fragmentArticle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentArticleAdapter extends RecyclerView.Adapter<FragmentArticleAdapter.FragmentAViewHolder>
{
    Context context;
    private List<fragmentArticle> fragmentArticleList;
    public  static String img_url_base;
    public static  String base_app_url;





    RequestQueue requestQueue;
    String session_id = null;
    ProgressDialog progressDialog;

    public FragmentArticleAdapter(Context context, List<fragmentArticle> fragmentArticleList) {
        this.context = context;
        this.fragmentArticleList = fragmentArticleList;
    }

    @NonNull
    @Override
    public FragmentAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_adapter,parent,false);
        return new FragmentAViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentAViewHolder holder, int position) {

        fragmentArticle fa = fragmentArticleList.get(position);
        holder.t1.setText(fa.getName());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base + fa.getImage()).into(holder.Img);

        holder.bk_mrk.setColorFilter(ContextCompat.getColor(context, R.color.orange));
        String hash_id = fa.getHash_id();

        Log.i("BookArti", String.valueOf(fa.getBookmarks()));
        String bookmark = fa.getBookmarks();
        if (bookmark.equals("true")) {
            holder.bk_mrk.setVisibility(View.VISIBLE);
        }
        else if (bookmark.equals("false"))
        {
            holder.add_bookmark.setVisibility(View.VISIBLE);
        }

        holder.add_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    AddToBookMark(hash_id);
                    holder.add_bookmark.setVisibility(View.GONE);
                    holder.bk_mrk.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.bk_mrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    removeBookMark(hash_id);
                    holder.bk_mrk.setVisibility(View.GONE);
                    holder.add_bookmark.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Article_details_activity.class);
                intent.putExtra("id",fa.getHash_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void AddToBookMark(String hash_id) throws JSONException {
        String url = base_app_url+"api/bookmark";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",2);
        jsonObject.put("item_id",hash_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
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

    private void removeBookMark(String hash_id) throws JSONException {
        String url = base_app_url+"api/bookmark";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",2);
        jsonObject.put("item_id",hash_id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
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

    @Override
    public int getItemCount() {
        return fragmentArticleList == null ?0: fragmentArticleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public  class FragmentAViewHolder extends RecyclerView.ViewHolder
    {

        TextView t1;
        ImageView Img;
        TextView btn;
        RelativeLayout relativeLayout;

        ImageView bk_mrk,add_bookmark;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public FragmentAViewHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.txt_artiles);
            Img = itemView.findViewById(R.id.article_img_1);
            relativeLayout = itemView.findViewById(R.id.articles_relative);

            bk_mrk = itemView.findViewById(R.id.bk_mrk_ic);
            add_bookmark = itemView.findViewById(R.id.ad_bk);
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
