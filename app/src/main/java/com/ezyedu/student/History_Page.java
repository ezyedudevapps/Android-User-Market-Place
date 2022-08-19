package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.ezyedu.student.adapter.HIstory_Fragment_Adapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class History_Page extends AppCompatActivity {
    TabLayout tableLayout;
    ViewPager2 pager2;
    HIstory_Fragment_Adapter hIstory_fragment_adapter;
    String session_id = null;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    String language = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__page);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_HPactivity",session_id);


        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);
        FragmentManager fragmentManager = getSupportFragmentManager();
        hIstory_fragment_adapter = new HIstory_Fragment_Adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(hIstory_fragment_adapter);

        if (language.equals("Indonesia"))
        {
            tableLayout.addTab(tableLayout.newTab().setText("Pending"));
            tableLayout.addTab(tableLayout.newTab().setText("Diproses"));
            tableLayout.addTab(tableLayout.newTab().setText("Selesai"));
            tableLayout.addTab(tableLayout.newTab().setText("Dibatalakan"));
        }
        else
        {
            tableLayout.addTab(tableLayout.newTab().setText("Pending"));
            tableLayout.addTab(tableLayout.newTab().setText("Processing"));
            tableLayout.addTab(tableLayout.newTab().setText("Completed"));
            tableLayout.addTab(tableLayout.newTab().setText("Cancelled"));
        }


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(History_Page.this,Others_Activity.class);
        startActivity(intent1);
    }
}