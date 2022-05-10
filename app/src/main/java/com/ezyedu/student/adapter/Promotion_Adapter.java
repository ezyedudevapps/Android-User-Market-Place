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
import com.ezyedu.student.SeperatePromotion;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.promotion;

import java.util.ArrayList;
import java.util.List;

public class Promotion_Adapter extends RecyclerView.Adapter<Promotion_Adapter.PromotionHolder>
{

    private Context context;
    private List<promotion> promotionList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public Promotion_Adapter(Context context, List<promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_home_adapter,parent,false);
        return new PromotionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionHolder holder, int position) {
        promotion promotions = promotionList.get(position);
        holder.heading.setText(promotions.getTittle());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+promotions.getImage()).into(holder.img);
        String hash = promotions.getHash_id();
        Log.i("hash_new_id_123",hash);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeperatePromotion.class);
                intent.putExtra("id",hash);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return promotionList == null ?0: promotionList.size();
    }

    public static class PromotionHolder extends RecyclerView.ViewHolder
    {
        TextView heading,days_to_go;
        ImageView img;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public PromotionHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.promo_heading);
            days_to_go = itemView.findViewById(R.id.day_count);
            img = itemView.findViewById(R.id.promo_icon);
            relativeLayout = itemView.findViewById(R.id.relative_promotion);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }

    }
}
