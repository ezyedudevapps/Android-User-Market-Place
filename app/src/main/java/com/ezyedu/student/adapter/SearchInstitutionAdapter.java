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
import com.ezyedu.student.model.SearchInstitutionClass;

import java.util.ArrayList;
import java.util.List;

public class SearchInstitutionAdapter extends RecyclerView.Adapter<SearchInstitutionAdapter.SearchInstitutionHolder> {

    private Context context;
    private List<SearchInstitutionClass> searchInstitutionClassList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public SearchInstitutionAdapter(Context context, List<SearchInstitutionClass> searchInstitutionClassList) {
        this.context = context;
        this.searchInstitutionClassList = searchInstitutionClassList;
    }

    @NonNull
    @Override
    public SearchInstitutionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_institution_adapter,parent,false);
        return new SearchInstitutionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchInstitutionHolder holder, int position)
    {
        SearchInstitutionClass searchInstitutionClass = searchInstitutionClassList.get(position);
        holder.heading.setText(searchInstitutionClass.getTittle());
        holder.address.setText(searchInstitutionClass.getAddress());
        holder.type.setText(searchInstitutionClass.getLearn_type());


        String hashid = searchInstitutionClass.getHashid();

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";

        Glide.with(context).load(img_url_base+searchInstitutionClass.getImage()).into(holder.headImage);

        Double ratings = searchInstitutionClass.getRating();

        holder.rating.setText(String.valueOf(ratings));

        holder.review.setText(String.valueOf(searchInstitutionClass.getTotal_review())+" Ratings");
        Integer id = searchInstitutionClass.getId();

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

    }

    @Override
    public int getItemCount() {
        return searchInstitutionClassList ==  null ?0: searchInstitutionClassList.size();
    }

    public void filterList(ArrayList<SearchInstitutionClass> filteredList)
    {
        searchInstitutionClassList = filteredList;
        notifyDataSetChanged();
    }

    public class SearchInstitutionHolder extends RecyclerView.ViewHolder {

        TextView type,heading,address,rating,review;
        ImageView headImage;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();

        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public SearchInstitutionHolder(@NonNull View itemView) {
            super(itemView);


            type = itemView.findViewById(R.id.learn_type);
            heading = itemView.findViewById(R.id.insitute_tittle);
            address = itemView.findViewById(R.id.location_institute);

            rating = itemView.findViewById(R.id.rating_cnt);
            review = itemView.findViewById(R.id.tot_reviews);

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
