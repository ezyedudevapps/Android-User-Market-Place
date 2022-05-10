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
import com.ezyedu.student.InstitutionActivity;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Institution_Groups;
import com.ezyedu.student.model.SearchCourse;

import java.util.ArrayList;
import java.util.List;

public class InstiGroupAdapter extends RecyclerView.Adapter<InstiGroupAdapter.GroupHolder> {


    private Context context;
    private List<Institution_Groups> institution_groupsList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;
    public InstiGroupAdapter(Context context, List<Institution_Groups> institution_groupsList) {
        this.context = context;
        this.institution_groupsList = institution_groupsList;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insti_group_adapter,parent,false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position)
    {
        Institution_Groups a = institution_groupsList.get(position);
        holder.tittle.setText(a.getGroup_name());
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InstitutionActivity.class);
                intent.putExtra("ven_id",a.getGroup_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return institution_groupsList == null ?0: institution_groupsList.size();
    }

    //search filter for ......
    public void filterList(ArrayList<Institution_Groups> filteredList)
    {
        institution_groupsList= filteredList;
        notifyDataSetChanged();
    }



    public class GroupHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        RelativeLayout relativeLayout;
        ImageView imageView;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.ven_group_name);
            imageView = itemView.findViewById(R.id.insti_grp_img);
            relativeLayout = itemView.findViewById(R.id.group_relative);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
