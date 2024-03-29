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
import com.ezyedu.student.adapter.Bookmarks_fragment_adapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class Bookmarks_Activity extends AppCompatActivity {


    TabLayout tableLayout;
    ViewPager2 pager2;
    Bookmarks_fragment_adapter adapter;
    ImageButton chat;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;
    String session_id = null;
    String language = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_);


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);



        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_cl_activity",session_id);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);


        chat = findViewById(R.id.messageFrag);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(Bookmarks_Activity.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent1 = new Intent(Bookmarks_Activity.this,Chat_List_Activity.class);
                    startActivity(intent1);
                }
            }
        });

        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new Bookmarks_fragment_adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(adapter);

        if (language.equals("Indonesia"))
        {
            tableLayout.addTab(tableLayout.newTab().setText("Institusi"));
            tableLayout.addTab(tableLayout.newTab().setText("Kursus"));
            tableLayout.addTab(tableLayout.newTab().setText("Artikel"));
        }
        else
        {
            tableLayout.addTab(tableLayout.newTab().setText("Institutions"));
            tableLayout.addTab(tableLayout.newTab().setText("Courses"));
            tableLayout.addTab(tableLayout.newTab().setText("Articles"));
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
        Intent intent1 = new Intent(Bookmarks_Activity.this,Others_Activity.class);
        startActivity(intent1);
    }
}