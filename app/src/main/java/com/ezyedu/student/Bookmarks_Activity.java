package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.ezyedu.student.adapter.Bookmarks_fragment_adapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class Bookmarks_Activity extends AppCompatActivity {


    TabLayout tableLayout;
    ViewPager2 pager2;
    Bookmarks_fragment_adapter adapter;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

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



        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new Bookmarks_fragment_adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(adapter);

        tableLayout.addTab(tableLayout.newTab().setText("Courses"));
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
}