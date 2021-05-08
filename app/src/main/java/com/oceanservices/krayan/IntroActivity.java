package com.oceanservices.krayan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroActivity extends AppCompatActivity {

    int count = 0;
    LottieAnimationView lottieAnimationView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        lottieAnimationView = findViewById(R.id.anim);
        textView = findViewById(R.id.message);
    }
    public void next(View view){
        String text = "";
        if(count == 0){
            lottieAnimationView.setAnimation(R.raw.krayan_no_ads);
            lottieAnimationView.playAnimation();
            text = "We don't show you annoying advertisements";
        }else if(count == 1){
            lottieAnimationView.setAnimation(R.raw.krayan_indian_flag);
            lottieAnimationView.playAnimation();
            text = "Most importantly, we are Handcrafted with love in India!";
        }else if(count == 2){
            startActivity(new Intent(IntroActivity.this,LoginActivity.class));
        }else{
            startActivity(new Intent(IntroActivity.this,LoginActivity.class));
        }
        textView.setText(text);
        count = count + 1;
    }
}