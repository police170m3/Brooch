package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.mint.testbluetoothspp.BTService;

/**
 * Created by MSI on 2016-08-17.
 */
public class StatisticsMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);

        BTService.config_check = true;  //sejin 2016.10.24

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(StatisticsMain.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.stat_main_btn_stress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스트레스 통계화면으로 이동 처리
                Intent i = new Intent(StatisticsMain.this, StatisticsDay.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.stat_main_btn_rage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //화 통계화면으로 이동 처리
                Intent i = new Intent(StatisticsMain.this, RageStatisticsDay.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {     //sejin 2016.10.24
        super.onPause();
        BTService.config_check = false;  //sejin 2016.10.24
    }
}
