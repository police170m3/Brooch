package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetPic extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_pic);

        //ImageView 버튼 처리
        View mViewPic1 = findViewById(R.id.setPicimageView1);
        mViewPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
            }
        });

        View mViewPic2 = findViewById(R.id.setPicimageView2);
        mViewPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
            }
        });

        View mViewPic3 = findViewById(R.id.setPicimageView3);
        mViewPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
            }
        });

        View mViewPic4 = findViewById(R.id.setPicimageView4);
        mViewPic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
            }
        });

        View mViewPic5 = findViewById(R.id.setPicimageView5);
        mViewPic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
            }
        });
    }
}
