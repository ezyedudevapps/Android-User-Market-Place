package com.ezyedu.student.adapter;

import android.content.Context;
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
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.VendorProducts;

import java.util.ArrayList;
import java.util.List;

public class VendorProductsAdapter extends RecyclerView.Adapter<VendorProductsAdapter.VenProdHolder>
{
    private Context context;
    private List<VendorProducts> vendorProductsList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public VendorProductsAdapter(Context context, List<VendorProducts> vendorProductsList) {
        this.context = context;
        this.vendorProductsList = vendorProductsList;
    }

    @NonNull
    @Override
    public VenProdHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_products_adapter,parent,false);
        return new VenProdHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VenProdHolder holder, int position)
    {
       VendorProducts vendorProducts = vendorProductsList.get(position);
        holder.t1.setText(vendorProducts.getStart_date());
        holder.t2.setText(vendorProducts.getCourse_duration());
        holder.t3.setText(vendorProducts.getCourse_title());
        holder.t4.setText(vendorProducts.getInstitution());
        String img_url ="https://dpzt0fozg75zu.cloudfront.net/";
        String path = vendorProducts.getCourses_image();

        Glide.with(context).load(img_url_base+path).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return vendorProductsList == null ?0: vendorProductsList.size();
    }

    public class VenProdHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView t1,t2,t3,t4;
        RelativeLayout relativeLayout;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public VenProdHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);
            t1 = itemView.findViewById(R.id.course_date);
            t2 = itemView.findViewById(R.id.course_duration);
            t3 = itemView.findViewById(R.id.text_course);
            t4 = itemView.findViewById(R.id.course_inst_author);
            relativeLayout = itemView.findViewById(R.id.course_card);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
