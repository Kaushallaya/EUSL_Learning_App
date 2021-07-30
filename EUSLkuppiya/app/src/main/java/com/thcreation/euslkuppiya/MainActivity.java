package com.thcreation.euslkuppiya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    ImageView logo,baground;
    TextView tv;
    LottieAnimationView lottieAnimationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        baground = findViewById(R.id.spbg);
        tv = findViewById(R.id.name);
        lottieAnimationView = findViewById(R.id.lotte);


        //baground.animate().translationY(-2500).setDuration(1000).setStartDelay(6000);
        logo.animate().translationY(-2000).setDuration(1000).setStartDelay(6000);
        tv.animate().translationY(-2000).setDuration(1000).setStartDelay(6000);
        lottieAnimationView.animate().translationY(2000).setDuration(1000).setStartDelay(6000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(in);
                finish();
            }
        },6500);

    }

    public void start(View v) {

//        Intent intent = new Intent(this,PDFActivity.class);
//        startActivity(intent);

        Intent in = new Intent(this,HomeActivity.class);
        startActivity(in);
    }
}