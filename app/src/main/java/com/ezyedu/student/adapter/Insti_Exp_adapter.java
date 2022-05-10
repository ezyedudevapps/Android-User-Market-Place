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
import com.ezyedu.student.model.instituExpAll;

import java.util.ArrayList;
import java.util.List;

public class Insti_Exp_adapter extends RecyclerView.Adapter<Insti_Exp_adapter.InstiExpAdapter>
{
    private Context context;
    private List<instituExpAll> instituExpAllArrayList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public Insti_Exp_adapter(Context context, List<instituExpAll> instituExpAllArrayList) {
        this.context = context;
        this.instituExpAllArrayList = instituExpAllArrayList;
    }

    @NonNull
    @Override
    public InstiExpAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insti_adapter_explore,parent,false);
        return new InstiExpAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstiExpAdapter holder, int position)
    {
        instituExpAll institutions = instituExpAllArrayList.get(position);
        holder.heading.setText(institutions.getVenor_name());
        holder.address.setText(institutions.getAddress());
        holder.type.setText(institutions.getGroupName());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+institutions.getVendor_logo()).into(holder.headImage);
        Integer id = institutions.getVendor_id();
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
        int review = institutions.getTotal_review();
        if (review == 0)
        {
            holder.b1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 1)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 2)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 3)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 4)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.a4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 5)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.a4.setVisibility(View.VISIBLE);
            holder.a5.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return instituExpAllArrayList == null ?0: instituExpAllArrayList.size();
    }

    public class InstiExpAdapter extends RecyclerView.ViewHolder {
        TextView type,heading,address;
        ImageView headImage,a1,a2,a3,a4,a5,b1,b2,b3,b4,b5;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public InstiExpAdapter(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.learn_type);
            heading = itemView.findViewById(R.id.insitute_tittle);
            address = itemView.findViewById(R.id.location_institute);

            a1 = itemView.findViewById(R.id.rating_img);
            a2 = itemView.findViewById(R.id.rating_img_1);
            a3 = itemView.findViewById(R.id.rating_img_2);
            a4 = itemView.findViewById(R.id.rating_img_3);
            a5 = itemView.findViewById(R.id.rating_img_4);
            b1 = itemView.findViewById(R.id.rating_img_empty);
            b2 = itemView.findViewById(R.id.rating_img_empty_1);
            b3 = itemView.findViewById(R.id.rating_img_empty_2);
            b4= itemView.findViewById(R.id.rating_img_empty_3);
            b5 = itemView.findViewById(R.id.rating_img_empty_4);

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
