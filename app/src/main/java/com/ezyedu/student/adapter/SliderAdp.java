package com.ezyedu.student.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;
import com.ezyedu.student.model.SliderImages;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdp extends SliderViewAdapter<SliderAdp.Holder>{
    private Context context;
    List<SliderImages> sliderImagesList = new ArrayList<>();

    public  static String img_url_base;
    public static  String base_app_url;

    public SliderAdp(Context context, List<SliderImages> sliderImagesList) {
        this.context = context;
        this.sliderImagesList = sliderImagesList;
    }

    @Override
    public SliderAdp.Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_image_slider,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdp.Holder viewHolder, int position) {
        SliderImages a = sliderImagesList.get(position);
        String img_url = "https://dpzt0fozg75zu.cloudfront.net/";
        Glide.with(context).load(img_url_base+a.getImage()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderImagesList == null ?0: sliderImagesList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        //retrive base url
        Globals sharedData = Globals.getInstance();


        //get img global url
        ImageGlobals shareData1 = ImageGlobals.getInstance();


        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_course);

            //get domain url
            base_app_url = sharedData.getValue();
            Log.i("domain_url",base_app_url);

            //get image loading url
            img_url_base = shareData1.getIValue();
            Log.i("img_url_global",img_url_base);

        }
    }
}