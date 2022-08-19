package com.ezyedu.student.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ezyedu.student.Institution_Groups;
import com.ezyedu.student.MapActivity;
import com.ezyedu.student.NearMe_Activity;
import com.ezyedu.student.R;
import com.ezyedu.student.SearchActivity;
import com.ezyedu.student.Search_Course_Activity;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Fragment_Explore extends Fragment{

    RelativeLayout r1,r2,r3;

    String session_id = null;
    SharedPreferences sharedPreferences;


    TextView i,c,n;
    String language = null;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_Histry_activity",session_id);

        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            i.setText("Institusi");
            c.setText("Kursus");
            n.setText("Sekitar Saya");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment__explore, container, false);

        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
       // Log.i("img_url_global",img_url_base);


        i = view.findViewById(R.id.ei1);
        c = view.findViewById(R.id.ec2);
        n = view.findViewById(R.id.en3);

        r1 = view.findViewById(R.id.institu_btn);
        r2 = view.findViewById(R.id.rr2);
        r3 = view.findViewById(R.id.r33);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Institution_Groups.class));
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Search_Course_Activity.class));
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(getContext(), "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = (new Intent(getActivity(), MapActivity.class));
                    startActivity(intent1);
                }

            }
        });
        return view;

    }
}