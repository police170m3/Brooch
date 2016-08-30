package com.tathink.brooch.brooch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView gear = (ImageView) findViewById(R.id.set);
        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(MainActivity.this, SetCall.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.btnStatistics)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, StatisticsMain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });


        //테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////
        ((Button)findViewById(R.id.safe_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivitySafe.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.caution_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivityCaution.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.serious_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivitySerious.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.warning_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivityWarning.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.bt_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, BTMainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        //테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////
    }
}
