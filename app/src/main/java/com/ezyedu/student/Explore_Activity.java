package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ezyedu.student.adapter.exp_fragment_adapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class Explore_Activity extends AppCompatActivity {

    TabLayout tableLayout;
    ViewPager2 pager2;
    ImageButton imageButton;
    exp_fragment_adapter adapter;
    String session_id = null;



    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);
        imageButton = findViewById(R.id.messageFrag);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(Explore_Activity.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Explore_Activity.this, Chat_List_Activity.class);
                    startActivity(intent);
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new exp_fragment_adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(adapter);

        tableLayout.addTab(tableLayout.newTab().setText("Explore"));
        tableLayout.addTab(tableLayout.newTab().setText("Promos"));
        tableLayout.addTab(tableLayout.newTab().setText("Articles"));

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

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("promotions",MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(Explore_Activity.this,MainActivity.class);
        startActivity(intent1);
        super.onBackPressed();
    }
}