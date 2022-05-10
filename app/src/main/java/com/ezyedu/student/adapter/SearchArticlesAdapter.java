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
import com.ezyedu.student.model.SearchArticlesClass;

import java.util.ArrayList;
import java.util.List;

public class SearchArticlesAdapter extends RecyclerView.Adapter<SearchArticlesAdapter.SearchArticleHolder>
{

    private Context context;
    private List<SearchArticlesClass> searchArticlesClassList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public SearchArticlesAdapter(Context context, List<SearchArticlesClass> searchArticlesClassList) {
        this.context = context;
        this.searchArticlesClassList = searchArticlesClassList;
    }

    @NonNull
    @Override
    public SearchArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_articles_adapter,parent,false);
        return new SearchArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchArticleHolder holder, int position)
    {
        SearchArticlesClass fa = searchArticlesClassList.get(position);

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
    public int getItemCount() {
        return searchArticlesClassList == null ?0: searchArticlesClassList.size();
    }

    public void filterList(ArrayList<SearchArticlesClass> filteredList)
    {
        searchArticlesClassList = filteredList;
        notifyDataSetChanged();
    }

    public class SearchArticleHolder extends RecyclerView.ViewHolder {
        TextView t1;
        ImageView Img;
        TextView btn;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public SearchArticleHolder(@NonNull View itemView) {
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
