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
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    TextView search,explore_head;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    RelativeLayout rhome,rchat,rexp,rcart,rothers;

    String language = null;
    TextView hom,ms,exp,crt,othrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_);


        //get domain url
        base_app_url = sharedData.getValue();
//        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
  //      Log.i("img_url_global",img_url_base);


        hom = findViewById(R.id.home_txt);
        ms = findViewById(R.id.ms_livia_text);
        exp = findViewById(R.id.explore_txt);
        crt = findViewById(R.id.Cart_text);
        othrs = findViewById(R.id.Others_text);


        search = findViewById(R.id.search_all_ed);
        explore_head = findViewById(R.id.exp_head);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            hom.setText("Beranda");
            ms.setText("Ms.Livia");
            exp.setText("Explore");
            crt.setText("Keranjang");
            othrs.setText("Lainya");
            explore_head.setText("Explorasi");
            search.setText("Cari Kursus, Institusi, dan lainya");
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Explore_Activity.this,SearchActivity.class);
                startActivity(intent1);
            }
        });

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
        if (language.equals("Indonesia"))
        {
            tableLayout.addTab(tableLayout.newTab().setText("Explorasi"));
            tableLayout.addTab(tableLayout.newTab().setText("Promosi"));
            tableLayout.addTab(tableLayout.newTab().setText("Artikel"));
        }
        else
        {
            tableLayout.addTab(tableLayout.newTab().setText("Explore"));
            tableLayout.addTab(tableLayout.newTab().setText("Promos"));
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

        //rel layout bottom nav
        rhome = findViewById(R.id.rel_1);
        rchat = findViewById(R.id.rel_chat);
        rexp = findViewById(R.id.rel_cen);
        rcart = findViewById(R.id.rel_cart);
        rothers = findViewById(R.id.rel_oth);

        rhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Explore_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        rchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Explore_Activity.this,ms_livia_activity.class);
                startActivity(intent);
            }
        });
        rothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Explore_Activity.this,Others_Activity.class);
                startActivity(intent);
            }
        });
        rcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Explore_Activity.this,Cart_Activity.class);
                startActivity(intent);
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