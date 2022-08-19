package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

public class ms_livia_activity extends AppCompatActivity {

    RelativeLayout rhome,rchat,rexp,rcart,rothers;
    Button chat_btn;
    String session_id = null;

    TextView main_ms,f1,f2,f3;
    String language = null;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    TextView hom,ms,exp,crt,othrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_livia_activity);

        chat_btn = findViewById(R.id.chatwithmislivia);

        main_ms = findViewById(R.id.ms1);
        f1 = findViewById(R.id.ms_f1);
        f2 = findViewById(R.id.ms_f2);
        f3 = findViewById(R.id.txt3);
        hom = findViewById(R.id.home_txt);
        ms = findViewById(R.id.ms_livia_text);
        exp = findViewById(R.id.explore_txt);
        crt = findViewById(R.id.Cart_text);
        othrs = findViewById(R.id.Others_text);
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
            main_ms.setText("Kami akan bantu anda cari yang terbaik");
            f1.setText("Kami menyusun program edukasi terbaik di satu app");
            f3.setText("Memberi edukasi anda pengalaman terbaik");
            chat_btn.setText("Mulai Chatting Dengan Ms.Livia Sekarang");
        }
        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        //rel layout bottom nav
        rhome = findViewById(R.id.rel_1);
        rchat = findViewById(R.id.rel_chat);
        rexp = findViewById(R.id.rel_cen);
        rcart = findViewById(R.id.rel_cart);
        rothers = findViewById(R.id.rel_oth);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_main_activity",session_id);



        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(session_id))
                {
                    Toast.makeText(ms_livia_activity.this, "Please Login to Continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = new Intent(ms_livia_activity.this, Chat_Activity.class);
                    intent1.putExtra("Vendor_id", 1);
                    intent1.putExtra("Institution_name", "ms_livia");
                    startActivity(intent1);
                }
            }
        });

        rhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ms_livia_activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        rexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ms_livia_activity.this,Explore_Activity.class);
                startActivity(intent);
            }
        });
        rcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ms_livia_activity.this,Cart_Activity.class);
                startActivity(intent);
            }
        });
        rothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ms_livia_activity.this,Others_Activity.class);
                startActivity(intent);
            }
        });
    }
}