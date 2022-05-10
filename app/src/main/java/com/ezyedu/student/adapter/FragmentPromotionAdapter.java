package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.SeperatePromotion;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.fragmentPromotion;

import java.util.List;

public class FragmentPromotionAdapter extends RecyclerView.Adapter<FragmentPromotionAdapter.PromotionFHolder>
{

    private Context context;
    private List<fragmentPromotion> fragmentPromotionList;
    public  static String img_url_base;
    public static  String base_app_url;

    public FragmentPromotionAdapter(Context context, List<fragmentPromotion> fragmentPromotionList) {
        this.context = context;
        this.fragmentPromotionList = fragmentPromotionList;
    }

    @NonNull
    @Override
    public PromotionFHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_promo_adapter,parent,false);
        return new  PromotionFHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionFHolder holder, int position)
    {
        fragmentPromotion frag = fragmentPromotionList.get(position);
        holder.txt1.setText(frag.getTittle());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+frag.getImage()).into(holder.img);
        String hash = frag.getHash_id();
        Log.i("hash_pro",hash);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(hash))
                {
                    Intent intent = new Intent(context, SeperatePromotion.class);
                    intent.putExtra("id",hash);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, "No promotion Details Found", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return fragmentPromotionList == null ?0: fragmentPromotionList.size();
    }

    public static class PromotionFHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView txt1;
        RelativeLayout relativeLayout;
        Button button;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public PromotionFHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_promo_fragment);
            txt1 = itemView.findViewById(R.id.tittle_promo_fragment);
            relativeLayout = itemView.findViewById(R.id.relative_promotion);
            button = itemView.findViewById(R.id.button_promo_fragment);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
