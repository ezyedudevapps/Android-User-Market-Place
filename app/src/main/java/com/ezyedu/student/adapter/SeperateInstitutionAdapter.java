package com.ezyedu.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SeperateInstitution;

import java.util.ArrayList;
import java.util.List;

public class SeperateInstitutionAdapter extends RecyclerView.Adapter<SeperateInstitutionAdapter.InstiHolder>
{
    private Context context;
    private List<SeperateInstitution> seperateInstitutionList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public SeperateInstitutionAdapter(Context context, List<SeperateInstitution> seperateInstitutionList) {
        this.context = context;
        this.seperateInstitutionList = seperateInstitutionList;
    }

    @NonNull
    @Override
    public InstiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_institution_adapter,parent,false);
         return new InstiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstiHolder holder, int position)
    {
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        SeperateInstitution seperateInstitution = seperateInstitutionList.get(position);
        String img_path = seperateInstitution.getImage();
        if (img_path.equals("null"))
        {
            img_path = "images/courses/vneyy1fk-aOAEDo.jpeg";
            Glide.with(context).load(img_url_base+img_path).into(holder.head_img);
        }
        else {
            String img = img_url_base + img_path;
            Glide.with(context).load(img).into(holder.head_img);
        }
    }

    @Override
    public int getItemCount() {
        return seperateInstitutionList == null ?0: seperateInstitutionList.size();
    }

    public static class InstiHolder extends RecyclerView.ViewHolder
    {
        ImageView head_img;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public InstiHolder(@NonNull View itemView) {
            super(itemView);
            head_img = itemView.findViewById(R.id.img_insti_full);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }

}
