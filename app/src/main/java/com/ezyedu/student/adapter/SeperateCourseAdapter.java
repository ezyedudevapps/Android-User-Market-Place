package com.ezyedu.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.ezyedu.student.R;
import com.ezyedu.student.SeperateInstitution;
import com.ezyedu.student.model.CourseVolleySingleton;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.course_seperate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeperateCourseAdapter extends RecyclerView.Adapter<SeperateCourseAdapter.SeperateCourseHolder>
{
    public Context context;
    private List<course_seperate> courseSeperateList = new ArrayList<>();
    String session_id = null;
    int rating = 0;
    String review;
    String hash_id;
    RequestQueue requestQueue;

    public  static String img_url_base;
    public static  String base_app_url;


    public SeperateCourseAdapter(Context context, List<course_seperate> courseSeperateList) {
        this.context = context;
        this.courseSeperateList = courseSeperateList;
    }

    @NonNull
    @Override
    public SeperateCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_course_adapter,parent,false);
        return new SeperateCourseHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SeperateCourseHolder holder, int position)
    {
        course_seperate course_seperate = courseSeperateList.get(position);


        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_new",session_id);

        requestQueue = CourseVolleySingleton.getInstance(context).getRequestQueue();

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";

        String ven_img = course_seperate.getVen_logo();
        Glide.with(context).load(img_url_base+ven_img).into(holder.host_icon);

        holder.tittle.setText(course_seperate.getCourse_title());
        String rp = "Rp ";
        int dup = course_seperate.getDiscount_price();
        String.valueOf(dup);
        String dup_price = rp+dup;
        holder.dup_price.setText(dup_price);
        holder.dup_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        int org = course_seperate.getInitial_price();
        String.valueOf(org);

        String final_price = rp+org;
        holder.org_price.setText(final_price);
       // holder.address_txt.setText(course_seperate.getVen_address());

      //  holder.highlight_body
        holder.desc_body_txt.setText(course_seperate.getCourse_description());

        holder.host_tittle.setText(course_seperate.getVen_name());

        holder.host_address.setText(course_seperate.getVen_address());
        holder.address_txt.setText(course_seperate.getVen_address());


       int course_rating = course_seperate.getCourse_rating();

       Log.i("CourseRate_val", String.valueOf(course_rating));
       if (course_rating == 1)
       {
           holder.f1.setVisibility(View.VISIBLE);

           holder.e2.setVisibility(View.VISIBLE);
           holder.e3.setVisibility(View.VISIBLE);
           holder.e4.setVisibility(View.VISIBLE);
           holder.e5.setVisibility(View.VISIBLE);
       }
       else if (course_rating == 2)
       {
           holder.f1.setVisibility(View.VISIBLE);
           holder.f2.setVisibility(View.VISIBLE);

           holder.e3.setVisibility(View.VISIBLE);
           holder.e4.setVisibility(View.VISIBLE);
           holder.e5.setVisibility(View.VISIBLE);
       }

       else if (course_rating == 3)
       {
           holder.f1.setVisibility(View.VISIBLE);
           holder.f2.setVisibility(View.VISIBLE);
           holder.f3.setVisibility(View.VISIBLE);

           holder.e4.setVisibility(View.VISIBLE);
           holder.e5.setVisibility(View.VISIBLE);
       }
       else if (course_rating == 4)
       {
           holder.f1.setVisibility(View.VISIBLE);
           holder.f2.setVisibility(View.VISIBLE);
           holder.f3.setVisibility(View.VISIBLE);
           holder.f4.setVisibility(View.VISIBLE);

           holder.e5.setVisibility(View.VISIBLE);

       }
       else if (course_rating == 5)
       {
           holder.f1.setVisibility(View.VISIBLE);
           holder.f2.setVisibility(View.VISIBLE);
           holder.f3.setVisibility(View.VISIBLE);
           holder.f4.setVisibility(View.VISIBLE);
           holder.f5.setVisibility(View.VISIBLE);
       }
       else
       {
           holder.e1.setVisibility(View.VISIBLE);
           holder.e2.setVisibility(View.VISIBLE);
           holder.e3.setVisibility(View.VISIBLE);
           holder.e4.setVisibility(View.VISIBLE);
           holder.e5.setVisibility(View.VISIBLE);
       }






        holder.tot_course_rate.setText(course_seperate.getTotal_course_rating()+ " ratings");




        int vendor_id = course_seperate.getVendor_id();
        String s_id = String.valueOf(vendor_id);
        Log.i("ven_new_id", String.valueOf(vendor_id));
        holder.Host_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeperateInstitution.class);
                intent.putExtra("ven_id",s_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    holder.a1.setVisibility(View.VISIBLE);
                    rating = 1;
                    Log.i("ratingVal", String.valueOf(rating));
                }catch (Exception e)
                {
                    Log.i("ErrorStar",e.toString());
                }
            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.a1.setVisibility(View.VISIBLE);
                holder.a2.setVisibility(View.VISIBLE);

                rating = 2;
            }
        });
        holder.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.a1.setVisibility(View.VISIBLE);
                holder.a2.setVisibility(View.VISIBLE);
                holder.a3.setVisibility(View.VISIBLE);
                rating = 3;
            }
        });
        holder.b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.a1.setVisibility(View.VISIBLE);
                holder.a2.setVisibility(View.VISIBLE);
                holder.a3.setVisibility(View.VISIBLE);
                holder.a4.setVisibility(View.VISIBLE);
                rating = 4;
            }
        });
        holder.b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.a1.setVisibility(View.VISIBLE);
                holder.a2.setVisibility(View.VISIBLE);
                holder.a3.setVisibility(View.VISIBLE);
                holder.a4.setVisibility(View.VISIBLE);
                holder.a5.setVisibility(View.VISIBLE);

                rating = 5;
            }
        });



        hash_id = course_seperate.getCourse_hash_id();

        holder.review_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (rating != 0)
                    {
                        try {
                            review = holder.get_review.getText().toString();
                            addReview();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Rating Should Not be null", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    private void addReview() throws JSONException {
        String url = base_app_url+"api/user/course-review/"+hash_id;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rate",rating);
        if (!TextUtils.isEmpty(review))
        {
            jsonObject.put("description",review);
        }
        Log.i("JsonReview",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResReview",response.toString());
                if (response.has("message"))
                {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("ErrRev",error.toString());
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
        return courseSeperateList == null ?0: courseSeperateList.size();
    }

    public static class SeperateCourseHolder extends RecyclerView.ViewHolder
    {
        ImageView main_imageView,rating_img,host_icon,hst_rating,a1,a2,a3,a4,a5,b1,b2,b3,b4,b5;
        TextView tittle,dup_price,org_price,address_txt,highlight_body,desc_body_txt,host_tittle,host_address,course_rate,tot_course_rate,course_offer;
        Button Host_btn;
        EditText get_review;
        Button review_submit;

        //course rating...
        ImageView f1,f2,f3,f4,f5,e1,e2,e3,e4,e5;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        @SuppressLint("CutPasteId")
        public SeperateCourseHolder(@NonNull View itemView)
        {
            super(itemView);


            tittle = itemView.findViewById(R.id.tittle_course);
            dup_price = itemView.findViewById(R.id.dup_price);
            org_price = itemView.findViewById(R.id.org_price);
            address_txt = itemView.findViewById(R.id.address);
        //    highlight_body = itemView.findViewById(R.id.h_b_txt);
            desc_body_txt = itemView.findViewById(R.id.c_b_txt);
            host_icon = itemView.findViewById(R.id.ven_logo);
            host_tittle = itemView.findViewById(R.id.ven_tittle);
            host_address = itemView.findViewById(R.id.ven_address);
            hst_rating = itemView.findViewById(R.id.ven_rating);
            Host_btn = itemView.findViewById(R.id.view_host_btn);

            course_rate = itemView.findViewById(R.id.rating_cnt);
            tot_course_rate = itemView.findViewById(R.id.tot_reviews);
            course_offer = itemView.findViewById(R.id.offer_precent);


            get_review = itemView.findViewById(R.id.get_review_ed);
            review_submit = itemView.findViewById(R.id.submit_review);


            //full star img
            a1 = itemView.findViewById(R.id.rating_img);
            a2 = itemView.findViewById(R.id.rating_img_1);
            a3 = itemView.findViewById(R.id.rating_img_2);
            a4 = itemView.findViewById(R.id.rating_img_3);
            a5 = itemView.findViewById(R.id.rating_img_4);

            //empty img
            b1 = itemView.findViewById(R.id.rating_img_empty);
            b2 = itemView.findViewById(R.id.rating_img_empty_1);
            b3 = itemView.findViewById(R.id.rating_img_empty_2);
            b4= itemView.findViewById(R.id.rating_img_empty_3);
            b5 = itemView.findViewById(R.id.rating_img_empty_4);


            f1 = itemView.findViewById(R.id.full1);
            f2 = itemView.findViewById(R.id.full2);
            f3 = itemView.findViewById(R.id.full3);
            f4 = itemView.findViewById(R.id.full4);
            f5 = itemView.findViewById(R.id.full5);

            e1 = itemView.findViewById(R.id.empty1);
            e2 = itemView.findViewById(R.id.empty2);
            e3 = itemView.findViewById(R.id.empty3);
            e4 = itemView.findViewById(R.id.empty4);
            e5 = itemView.findViewById(R.id.empty5);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
