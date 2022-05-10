package com.ezyedu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import java.util.List;

public class SplashScreen extends AppCompatActivity {

    Globals sharedData = Globals.getInstance();


    ImageGlobals shareData1 = ImageGlobals.getInstance();


    //domain url
    String dev_url = "https://dev-api.ezy-edu.com/";
    String production_url = "https://prod-api.ezy-edu.com/";

    //image url
    String dev_image = "https://dpzt0fozg75zu.cloudfront.net/";
    String prod_image = "https://d2ozgbltrhzzw8.cloudfront.net/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        //set domain url
        sharedData.setValue(production_url);

        //set img url
        shareData1.SetValue(prod_image);


        Uri uri = getIntent().getData();
        if (uri != null)
        {
            List<String> params = uri.getPathSegments();
            String id = params.get(params.size()-1);
            Log.i("DeepLinkID",id);

            Intent intent11 = new Intent(SplashScreen.this,Course_one_new.class);
            intent11.putExtra("id",id);
            startActivity(intent11);

        }
        else
        {
            Intent intent1 = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent1);
        }
    }




}