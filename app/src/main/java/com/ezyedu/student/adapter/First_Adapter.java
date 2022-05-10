package com.ezyedu.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.student.model.First_model;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class First_Adapter extends RecyclerView.Adapter<First_Adapter.ViewHolder> {

    ArrayList<First_model> firstModel;
    Context context;


    public First_Adapter (Context context,ArrayList<First_model>firstModel)
    {
        this.context = context;
        this.firstModel = firstModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_adapter1,parent
        ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.circleImageView.setImageResource(firstModel.get(position).getIcon());
        holder.head_txt.setText(firstModel.get(position).getHead_text());
        holder.t1.setText(firstModel.get(position).getBody_text1());
        holder.t2.setText(firstModel.get(position).getBody_text2());
        holder.t3.setText(firstModel.get(position).getBody_text3());
    }

    @Override
    public int getItemCount() {
        return firstModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //initialize variables
        CircleImageView circleImageView;
        TextView head_txt,t1,t2,t3;


        //retrive base url
        Globals sharedData = Globals.getInstance();
        String base_app_url;

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();
        String img_url_base;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.img1);
            head_txt = itemView.findViewById(R.id.txt_head);
            t1 = itemView.findViewById(R.id.txt1);
            t2 = itemView.findViewById(R.id.txt2);
            t3 = itemView.findViewById(R.id.txt3);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
