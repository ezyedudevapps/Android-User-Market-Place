package com.ezyedu.student.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.Cart_Activity;
import com.ezyedu.student.Chat_List_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.SeperateInstitution;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.Promo_one_new;

import java.util.ArrayList;
import java.util.List;

public class SeperatePromotionAdapter extends RecyclerView.Adapter<SeperatePromotionAdapter.PromoHolder>
{
    private Context context;
    private List<Promo_one_new> promo_one_newList = new ArrayList<>();
    String session_id = null;
    public  static String img_url_base;
    public static  String base_app_url;


    public SeperatePromotionAdapter(Context context, List<Promo_one_new> promo_one_newList) {
        this.context = context;
        this.promo_one_newList = promo_one_newList;
    }

    @NonNull
    @Override
    public PromoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seperate_promotion_adapter,parent,false);
        return new PromoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoHolder holder, int position)
    {
        Promo_one_new promo_one_new = promo_one_newList.get(position);

        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("session_new",session_id);


        int vendor_id = promo_one_new.getVendor_id();
        String s_id = String.valueOf(vendor_id);

        holder.host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeperateInstitution.class);
                intent.putExtra("ven_id",s_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent1 = new Intent(context, Cart_Activity.class);
                    context.startActivity(intent1);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = new Intent(context, Chat_List_Activity.class);
                    context.startActivity(intent1);
                }
            }
        });


        String a = promo_one_new.getStart_at();
        String b = promo_one_new.getFinish_at();

        String [] aa = a.split(" ");
        String date = aa[0];
        Log.i("datea",date);
        String [] date_exact = date.split("-");

        String sd = date_exact[2];
        Log.i("datee",sd);
        String sy = date_exact[0];
        Log.i("year",sy);
        String sm = date_exact[1];
        Log.i("month",sm);

        String bb = promo_one_new.getFinish_at();
        String [] fb = bb.split("-");
        String fbb = fb[2];
        String[] fab = fbb.split(" ");
        Log.i("asdfg",fbb);

        String fy = fb[0];
        Log.i("yearf",fy);
        String fm = fb[1];
        Log.i("monthf",fm);
        String fd = fab[0];
        Log.i("fdate",fd);


        String fmonth = "";
        switch (fm) {
            case "01":
                fmonth = "Jan";
                break;
            case "02":
                fmonth = "Feb";
                break;
            case "03":
                fmonth = "Mar";
                break;
            case "04":
                fmonth = "Apr";
                break;
            case "05":
                fmonth = "May";
                break;
            case "06":
                fmonth = "June";
                break;
            case "07":
                fmonth = "July";
                break;
            case "08":
                fmonth = "Aug";
                break;
            case "09":
                fmonth = "Sep";
                break;
            case "10":
                fmonth = "Oct";
                break;
            case "11":
                fmonth = "Nov";
                break;
            default:
                fmonth = "Dec";
                break;
        }

Log.i("finish_month",fmonth);




        String month = "";
        switch (sm) {
            case "01":
                month = "Jan";
                break;
            case "02":
                month = "Feb";
                break;
            case "03":
                month = "Mar";
                break;
            case "04":
                month = "Apr";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "Aug";
                break;
            case "09":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            default:
                month = "Dec";
                break;
        }
        Log.i("month_val",month);


        String Start_complete_dtl = sd +" "+month +" "+sy;
        String finish_complete_dtl = fy + " "+fmonth +" "+fy;
        String complete_date = Start_complete_dtl +" - "+finish_complete_dtl;
        Log.i("complete_date_dtl",complete_date);

        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";

        Glide.with(context).load(img_url_base+promo_one_new.getImage()).into(holder.head_img);

        holder.tittle.setText(promo_one_new.getTittle());
        holder.duration.setText(complete_date);

        Glide.with(context).load(img_url_base+promo_one_new.getLogo()).into(holder.head_logo);
        holder.vtittle.setText(promo_one_new.getVtittle());
        holder.vaddress.setText(promo_one_new.getVaddress());
        holder.vdesc.setText(promo_one_new.getDescription_content());
        Double review = promo_one_new.getRating();

        if (review == 0.0)
        {
            holder.b1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 1.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.b2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 2.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.b3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 3.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.b4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 4.0)
        {
            holder.a1.setVisibility(View.VISIBLE);
            holder.a2.setVisibility(View.VISIBLE);
            holder.a3.setVisibility(View.VISIBLE);
            holder.a4.setVisibility(View.VISIBLE);
            holder.b5.setVisibility(View.VISIBLE);
        }
        else if (review == 5.0)
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
    public int getItemCount()
    {
        return promo_one_newList == null ?0: promo_one_newList.size();
    }

    public static class PromoHolder extends RecyclerView.ViewHolder
    {
        ImageView head_img,head_logo,a1,a2,a3,a4,a5,b1,b2,b3,b4,b5,chat,cart,back;;
        TextView tittle,duration,vtittle,vaddress,vdesc,host;


        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public PromoHolder(@NonNull View itemView) {
            super(itemView);

            head_img = itemView.findViewById(R.id.promo_sep_img);
            head_logo = itemView.findViewById(R.id.mg_promo);
            tittle = itemView.findViewById(R.id.tittle_promotion);
            duration = itemView.findViewById(R.id.promo_date);
            vtittle = itemView.findViewById(R.id.promo_tittle);
            vaddress = itemView.findViewById(R.id.address_promo);
            vdesc = itemView.findViewById(R.id.body_txt);
            host = itemView.findViewById(R.id.host_button);
            chat = itemView.findViewById(R.id.chat_sep);

            cart = itemView.findViewById(R.id.cart_img);
            back = itemView.findViewById(R.id.sc_back);


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

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }

    }
}
