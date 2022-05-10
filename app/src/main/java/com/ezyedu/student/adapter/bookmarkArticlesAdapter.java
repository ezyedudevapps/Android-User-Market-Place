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
import com.ezyedu.student.model.bookmark_articles;

import java.util.ArrayList;
import java.util.List;

public class bookmarkArticlesAdapter extends RecyclerView.Adapter<bookmarkArticlesAdapter.baHolder> {
    private Context context;
    private List<bookmark_articles> bookmarkArticlesList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;


    public bookmarkArticlesAdapter(Context context, List<bookmark_articles> bookmarkArticlesList) {
        this.context = context;
        this.bookmarkArticlesList = bookmarkArticlesList;
    }

    @NonNull
    @Override
    public bookmarkArticlesAdapter.baHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_articles_adapter,parent,false);
        return new baHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookmarkArticlesAdapter.baHolder holder, int position)
    {
        bookmark_articles a = bookmarkArticlesList.get(position);
        holder.txt1.setText(a.getTittle());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.img1);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Article_details_activity.class);
                intent.putExtra("id",a.getHash_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return bookmarkArticlesList == null ?0: bookmarkArticlesList.size();
    }

    public class baHolder extends RecyclerView.ViewHolder {
        TextView txt1,btn1;
        ImageView img1;
        RelativeLayout relativeLayout;

        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public baHolder(@NonNull View itemView) {
            super(itemView);

            txt1 = itemView.findViewById(R.id.txt_artiles);
            img1 = itemView.findViewById(R.id.article_img_1);
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
