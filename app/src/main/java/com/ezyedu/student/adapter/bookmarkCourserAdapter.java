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
import com.ezyedu.student.model.Bookmark_courses;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.ArrayList;
import java.util.List;

public class bookmarkCourserAdapter extends RecyclerView.Adapter<bookmarkCourserAdapter.bcholder> {
    private Context context;
    private List<Bookmark_courses> bookmark_coursesList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;


    public bookmarkCourserAdapter(Context context, List<Bookmark_courses> bookmark_coursesList) {
        this.context = context;
        this.bookmark_coursesList = bookmark_coursesList;
    }

    @NonNull
    @Override
    public bookmarkCourserAdapter.bcholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_course_adapter,parent,false);
        return new bcholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookmarkCourserAdapter.bcholder holder, int position)
    {
        Bookmark_courses a = bookmark_coursesList.get(position);
        holder.t3.setText(a.getCourser_Name());
        holder.t4.setText(a.getVendor_name());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Course_one_new.class);
                intent.putExtra("id",a.getHash_id());
                Log.i("course_hash",a.getHash_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmark_coursesList ==  null ?0:bookmark_coursesList.size();
    }

    public class bcholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView t3,t4;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public bcholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);

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
