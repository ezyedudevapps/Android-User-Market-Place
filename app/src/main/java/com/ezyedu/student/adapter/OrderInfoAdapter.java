package com.ezyedu.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderInfoHolder>
{
    private Context context;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public OrderInfoAdapter(Context context, List<OrderInfo> orderInfoList) {
        this.context = context;
        this.orderInfoList = orderInfoList;
    }

    @NonNull
    @Override
    public OrderInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.order_info_adapter,parent,false);
        return new OrderInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderInfoHolder holder, int position)
    {
        OrderInfo a = orderInfoList.get(position);
        holder.Tittle.setText(a.getCourse_name());
        holder.quantity.setText("Qty - "+a.getCourse_qty());
        holder.price.setText("Price : "+a.getCourse_amount());

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getCourse_image()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return orderInfoList == null ?0: orderInfoList.size();
    }

    public class OrderInfoHolder extends RecyclerView.ViewHolder
    {
        TextView Tittle,quantity,price;
        ImageView imageView;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public OrderInfoHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.course_img);
            Tittle = itemView.findViewById(R.id.c_t);
            quantity = itemView.findViewById(R.id.c_qty);
            price = itemView.findViewById(R.id.prc);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);


        }
    }
}
