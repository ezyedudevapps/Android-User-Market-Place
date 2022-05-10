package com.ezyedu.student.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Save_Orders;

import java.util.ArrayList;
import java.util.List;

public class SaveOrderAdapter extends RecyclerView.Adapter<SaveOrderAdapter.SaveHolder> {

    private Context context;
    private List<Save_Orders> save_ordersList = new ArrayList<>();
    public  static String img_url_base;
    public static  String base_app_url;

    public SaveOrderAdapter(Context context, List<Save_Orders> save_ordersList) {
        this.context = context;
        this.save_ordersList = save_ordersList;
    }

    @NonNull
    @Override
    public SaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_order_adapter,parent,false);
        return new SaveHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SaveHolder holder, int position)
    {
        Save_Orders save_orders = save_ordersList.get(position);
        holder.ref_number.setText("Ref Number : "+save_orders.getOrder_id());
        holder.name.setText("Vendor Name : "+save_orders.getVen_name());
        holder.amt.setText("Amount : "+save_orders.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return save_ordersList == null ?0: save_ordersList.size();
    }

    public class SaveHolder extends RecyclerView.ViewHolder {
        TextView ref_number,name,amt;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();



        public SaveHolder(@NonNull View itemView) {
            super(itemView);
            ref_number = itemView.findViewById(R.id.order_ref_num);
            name = itemView.findViewById(R.id.ven_nm);
            amt = itemView.findViewById(R.id.Amt_course);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}
