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
import com.ezyedu.student.Article_details_activity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.articles;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder>
{
    private Context context;
    private List<articles> articlesList =new ArrayList<>();
 public  static String img_url_base;

    public ArticlesAdapter(Context context, List<articles> articlesList) {
        this.context = context;
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles_adapter,parent,false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position)
    {
        articles fa = articlesList.get(position);
        holder.t1.setText(fa.getTittle());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+fa.getImage()).into(holder.Img);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Article_details_activity.class);
                intent.putExtra("id",fa.getHashid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
     //   return articlesList == null ?0: articlesList.size();
        return  articlesList.size();
    }


    public static class ArticleHolder extends RecyclerView.ViewHolder{

        TextView t1;
        ImageView Img;
        TextView btn;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public ArticleHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.txt_artiles);
            Img = itemView.findViewById(R.id.article_img_1);

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
