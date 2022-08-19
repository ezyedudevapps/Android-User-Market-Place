package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.ezyedu.student.adapter.SearchFragmentAdapter;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class SearchActivity extends AppCompatActivity
{
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    SearchFragmentAdapter searchFragmentAdapter;


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
        setContentView(R.layout.activity_search);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        tabLayout = findViewById(R.id.search_tab);
        viewPager2 = findViewById(R.id.search_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        searchFragmentAdapter = new SearchFragmentAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(searchFragmentAdapter);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {

            tabLayout.addTab(tabLayout.newTab().setText("Institusi"));
            tabLayout.addTab(tabLayout.newTab().setText("Kursus"));
            tabLayout.addTab(tabLayout.newTab().setText("Artikel"));
        }
        else
        {

            tabLayout.addTab(tabLayout.newTab().setText("Institutions"));
            tabLayout.addTab(tabLayout.newTab().setText("Courses"));
            tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        }





        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
    @Override
    public void onPageSelected(int position) {
     //   super.onPageSelected(position);

       tabLayout.selectTab(tabLayout.getTabAt(position));
    }
});



    }
}