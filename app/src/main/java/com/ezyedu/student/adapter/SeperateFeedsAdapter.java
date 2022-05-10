package com.ezyedu.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SeperateFeeds;

import java.util.ArrayList;
import java.util.List;

public class SeperateFeedsAdapter extends RecyclerView.Adapter<SeperateFeedsAdapter.SepFeedHolder> {

    private Context context;
    private List<SeperateFeeds> seperateFeeds = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public SeperateFeedsAdapter(Context context, List<SeperateFeeds> seperateFeeds) {
        this.context = context;
        this.seperateFeeds = seperateFeeds;
    }

    @NonNull
    @Override
    public SeperateFeedsAdapter.SepFeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_feeds_adapter,parent,false);
        return new SepFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeperateFeedsAdapter.SepFeedHolder holder, int position) {

        SeperateFeeds a = seperateFeeds.get(position);
        holder.tittle.setText(a.getTittle());
        holder.label.setText(a.getLabel());
        holder.address.setText(a.getAddress());
        holder.description.setText(a.getDescription());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.img);
        Glide.with(context).load(img_url_base+a.getVen_logo()).into(holder.logo);

        holder.ven_name.setText(a.getName());

    }

    @Override
    public int getItemCount() {
        return seperateFeeds == null ?0: seperateFeeds.size();
    }

    public class SepFeedHolder extends RecyclerView.ViewHolder {
        ImageView img,logo;
        TextView tittle,label,description,address,ven_name;
        Button btn;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public SepFeedHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_feed);
            tittle = itemView.findViewById(R.id.feed_ttl);
            label = itemView.findViewById(R.id.feed_lbl);
            description = itemView.findViewById(R.id.feed_descrip);
            address = itemView.findViewById(R.id.feed_ven_address);
            logo = itemView.findViewById(R.id.ven_logo);
            btn = itemView.findViewById(R.id.feed_hst_btn);
            ven_name = itemView.findViewById(R.id.ven_nm);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
