package com.scan.tony.scanprice.BasketProducts;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.scan.tony.scanprice.R;
import com.github.paolorotolo.appintro.AppIntro;


public class MyIntro extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        addSlide(SampleSlide.newInstance(R.layout.first_slide));
        addSlide(SampleSlide.newInstance(R.layout.second_slide));
        addSlide(SampleSlide.newInstance(R.layout.third_slide));


    }

    @Override
    public void onSkipPressed() {
        startActivity(new Intent(this, BasketActivity.class));
        finish();
    }

    @Override
    public void onNextPressed() {

    }


    @Override
    public void onDonePressed() {
        startActivity(new Intent(this, BasketActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged() {

    }


}