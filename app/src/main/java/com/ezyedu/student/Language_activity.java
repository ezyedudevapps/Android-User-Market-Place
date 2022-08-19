package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Language_activity extends AppCompatActivity {

    TextView english,indonesia;
    SharedPreferences sp;
    String language = null;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_activity);

        sp = getSharedPreferences("Language", Context.MODE_PRIVATE);


        english = findViewById(R.id.eng);
        indonesia = findViewById(R.id.ind);

        SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        language = sharedPreferences1.getString("Language_select","");
        Log.i("Only_Language_main_activity",language);

        if (language.equals("Indonesia"))
        {
            indonesia.setTextColor(getResources().getColor(R.color.orange_500));
            english.setTextColor(getResources().getColor(R.color.black));
        }
        else
        {
            english.setTextColor(getResources().getColor(R.color.orange_500));
            indonesia.setTextColor(getResources().getColor(R.color.black));
        }


        english.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                english.setTextColor(getResources().getColor(R.color.orange_500));
                indonesia.setTextColor(getResources().getColor(R.color.black));
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Language_select","English");
                editor.commit();
                Intent intent1 = new Intent(Language_activity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        indonesia.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                indonesia.setTextColor(getResources().getColor(R.color.orange_500));
                english.setTextColor(getResources().getColor(R.color.black));
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Language_select","Indonesia");
                editor.commit();
                Intent intent1 = new Intent(Language_activity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}