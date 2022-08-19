package com.ezyedu.student.adapter;

import android.annotation.SuppressLint;
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
import com.ezyedu.student.model.courses;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder>
{
    private Context context;
    private List<courses> coursesList=new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public CourseAdapter(Context context, List<courses> coursesList) {
        this.context = context;
        this.coursesList = coursesList;
    }


    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter,parent,false);
        return new CourseHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        courses courses = coursesList.get(position);

            holder.t2.setText(courses.getDuration()+ " days");

            String date_time = courses.getCccreated_at();
            String [] date_only = date_time.split(" ");
            String date = date_only[0];
        Log.i("date_only",date);
            holder.t1.setText(date);
          holder.t3.setText(courses.getTitle());
          holder.t4.setText(courses.getVname());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
          Glide.with(context).load(img_url_base+courses.getImage()).into(holder.imageView);




          holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(context, Course_one_new.class);
                  intent.putExtra("id",courses.getHash_id());
                  Log.i("course_hash",courses.getHash_id());
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);

              }
          });
    }

    @Override
    public int getItemCount() {
        return coursesList == null  ?0: coursesList.size();
    }

    public static class CourseHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView t1,t2,t3,t4;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public CourseHolder(@NonNull View itemView) {
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
