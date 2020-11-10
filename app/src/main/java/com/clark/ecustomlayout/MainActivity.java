package com.clark.ecustomlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ObjectAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.iv_image);
        loadImage();
        animator=startRotate();
        animator.start();
    }

    private void loadImage(){
        Glide.with(MainActivity.this)
                .load(R.drawable.ic_music)
                .circleCrop()
                .into(imageView);
    }

    private ObjectAnimator startRotate(){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(imageView,"rotation",360f);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(10000);
        objectAnimator.setDuration(4000);
        return objectAnimator;
    }
}
