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
import com.ezyedu.student.R;
import com.ezyedu.student.SeperateInstitution;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.institutions;

import java.util.ArrayList;
import java.util.List;

public class InstituteAdapter extends RecyclerView.Adapter<InstituteAdapter.InstituteHolder>
{
    private Context context;
    private List<institutions> institutionsList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;
    public InstituteAdapter(Context context, List<institutions> institutionsList) {
        this.context = context;
        this.institutionsList = institutionsList;
    }

    @NonNull
    @Override
    public InstituteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.institution_adapter,parent,false);
        return new InstituteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstituteHolder holder, int position)
    {
        institutions institutions = institutionsList.get(position);
        holder.heading.setText(institutions.getTittle());
        holder.address.setText(institutions.getAddress());
        holder.type.setText(institutions.getLearn_type());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+institutions.getImage()).into(holder.headImage);

        String hashid = institutions.getHashid();

        Integer id = institutions.getId();

        String s_id = String.valueOf(id);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeperateInstitution.class);
                intent.putExtra("ven_id",s_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        Double review = institutions.getRating();
        holder.rating.setText(review +"*");

        holder.total_review.setText(institutions.getTotal_review() + " Ratings");


    }

    @Override
    public int getItemCount() {
        return institutionsList == null ?0: institutionsList.size();
    }

    public static class InstituteHolder extends RecyclerView.ViewHolder
    {
        TextView type,heading,address,rating,total_review;
        ImageView headImage,a1,a2,a3,a4,a5,b1,b2,b3,b4,b5;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public InstituteHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.learn_type);
            heading = itemView.findViewById(R.id.insitute_tittle);
            address = itemView.findViewById(R.id.location_institute);
            rating = itemView.findViewById(R.id.rating_cnt);
            total_review = itemView.findViewById(R.id.tot_reviews);

            headImage = itemView.findViewById(R.id.institution_image);

            relativeLayout = itemView.findViewById(R.id.insti_relative);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}

