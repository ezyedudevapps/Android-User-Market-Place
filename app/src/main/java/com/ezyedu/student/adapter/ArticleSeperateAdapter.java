package com.ezyedu.student.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.Html;
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
import com.ezyedu.student.Cart_Activity;
import com.ezyedu.student.Chat_List_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.articesSeperate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleSeperateAdapter extends RecyclerView.Adapter<ArticleSeperateAdapter.ArtSepHolder>
{

    private Context context;
    private List<articesSeperate> articesSeperateList = new ArrayList<>();
    String session_id = null;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    public  static String img_url_base;
    public static  String base_app_url;

    public ArticleSeperateAdapter(Context context, List<articesSeperate> articesSeperateList) {
        this.context = context;
        this.articesSeperateList = articesSeperateList;
    }

    @NonNull
    @Override
    public ArtSepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_article_adapter,parent,false);
        return new ArtSepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtSepHolder holder, int position)
    {
        articesSeperate articesSeperate = articesSeperateList.get(position);

        sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_new",session_id);


        holder.header.setText(articesSeperate.getHeading());
        holder.author.setText("Author : "+articesSeperate.getAuthor());

        holder.type.setText(articesSeperate.getType());
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog dig = new AlertDialog.Builder(context).setTitle("Please Select").setMessage("Add to Bookmarks ?").
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (TextUtils.isEmpty(session_id))
                                {
                                    Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    progressDialog = new ProgressDialog(context);
                                    progressDialog.show();
                                    progressDialog.setContentView(R.layout.progress_dialog);
                                    progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                                    if (TextUtils.isEmpty(session_id))
                                    {
                                        Toast.makeText(context, "Please Login to continue", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {
                                            addBookmark(articesSeperate.getHash_id());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
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
        });


        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, Cart_Activity.class);
                context.startActivity(intent1);
            }
        });
    //    String description = articesSeperate.getDescription();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            holder.description.setText(Html.fromHtml(articesSeperate.getDescription(),Html.FROM_HTML_MODE_COMPACT));
        }
        else
        {
            holder.description.setText(Html.fromHtml(articesSeperate.getDescription()));
        }

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(context, "Please Login to continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = new Intent(context, Chat_List_Activity.class);
                    context.startActivity(intent1);
                }
            }
        });

     //   holder.description.setText(articesSeperate.getDescription());
     //   String img = "https://dev-api.ezy-edu.com/images/ezyedu-blue.jpg";
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+articesSeperate.getImage()).into(holder.imageView);

        String date_all = articesSeperate.getDate();
        Log.i("dt",date_all);

        String [] date = date_all.split(" ");
        String date_new = date[0];
        holder.date.setText("Published On : "+date_new);
    }

    private void addBookmark(String hash_id) throws JSONException {
        RequestQueue requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item_type",2);
        jsonObject.put("item_id",hash_id);
        String url = base_app_url+"api/bookmark";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                  String message = response.getString("message");
                    Log.i("BookMarkMessage",message);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    progressDialog.dismiss();
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

    @Override
    public int getItemCount() {
        return articesSeperateList == null ?0: articesSeperateList.size();
    }

    public static class ArtSepHolder extends RecyclerView.ViewHolder {
        ImageView imageView,bookmark,share,chat,cart;
        TextView type,header,author,date,description;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public ArtSepHolder(@NonNull View itemView) {
            super(itemView);

            bookmark = itemView.findViewById(R.id.bookmark_img);
            chat = itemView.findViewById(R.id.chat_sep);
            share = itemView.findViewById(R.id.share_img);
            cart = itemView.findViewById(R.id.cart_img);


            imageView = itemView.findViewById(R.id.article_image);
            type = itemView.findViewById(R.id.type_txt);
            header = itemView.findViewById(R.id.Heading_text);
            author = itemView.findViewById(R.id.article_aythor);
            date = itemView.findViewById(R.id.article_date);
            description = itemView.findViewById(R.id.article_description);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
