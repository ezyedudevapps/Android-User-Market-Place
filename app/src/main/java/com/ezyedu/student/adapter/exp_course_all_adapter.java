package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.Course_one_new;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.exp_all_courses;

import java.util.ArrayList;
import java.util.List;

public class exp_course_all_adapter extends RecyclerView.Adapter<exp_course_all_adapter.exAlCourseHolder>
{
    private Context context;
    private List<exp_all_courses> exp_all_coursesList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public exp_course_all_adapter(Context context, List<exp_all_courses> exp_all_coursesList) {
        this.context = context;
        this.exp_all_coursesList = exp_all_coursesList;
    }

    @NonNull
    @Override
    public exAlCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_course_adapter,parent,false);
        return new exAlCourseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull exAlCourseHolder holder, int position)
    {
        exp_all_courses a = exp_all_coursesList.get(position);
        holder.t2.setText(a.getDuration());

        String date_time = a.getCccreated_at();
        String [] date_only = date_time.split(" ");
        String date = date_only[0];
        Log.i("date_only",date);
        holder.t1.setText(date);
        holder.t3.setText(a.getTitle());
        holder.t4.setText(a.getVname());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Course_one_new.class);
                intent.putExtra("id",a.getHash_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return exp_all_coursesList == null ?0: exp_all_coursesList.size();
    }

    public class exAlCourseHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView t1,t2,t3,t4;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public exAlCourseHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);
            t1 = itemView.findViewById(R.id.course_date);
            t2 = itemView.findViewById(R.id.course_duration);
            t3 = itemView.findViewById(R.id.text_course);
            t4 = itemView.findViewById(R.id.course_inst_author);
            relativeLayout = itemView.findViewById(R.id.course_card);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
