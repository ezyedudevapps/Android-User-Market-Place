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
import com.ezyedu.student.Seperate_Feeds_Activity;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.InstitutionFeed;

import java.util.ArrayList;
import java.util.List;

public class InstiFeedAdapter extends RecyclerView.Adapter<InstiFeedAdapter.FeedHolder>
{
    private Context context;
    private List<InstitutionFeed> institutionFeedList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public InstiFeedAdapter(Context context, List<InstitutionFeed> institutionFeedList) {
        this.context = context;
        this.institutionFeedList = institutionFeedList;
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insti_feed_adapter,parent,false);
        return new FeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position)
    {
        InstitutionFeed institutionFeed = institutionFeedList.get(position);
        holder.headtext.setText(institutionFeed.getTittle());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        String path = institutionFeed.getImage();

        Glide.with(context).load(img_url_base+path).into(holder.img1);

        String hash = institutionFeed.getHash_id();
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Seperate_Feeds_Activity.class);
                intent.putExtra("id",hash);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return institutionFeedList == null ?0: institutionFeedList.size();
    }

    public static class FeedHolder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        RelativeLayout relativeLayout;
        TextView headtext;


        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public FeedHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.article_img_1);
            headtext = itemView.findViewById(R.id.txt_artiles);
            relativeLayout = itemView.findViewById(R.id.articles_relative);


            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
