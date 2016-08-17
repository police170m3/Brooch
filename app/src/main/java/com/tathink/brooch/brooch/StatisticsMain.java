package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by MSI on 2016-08-17.
 */
public class StatisticsMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);

        ((Button)findViewById(R.id.stat_main_btn_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //설정 화면으로 이동
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
                /*Intent i = new Intent(MainActivity.this, StatisticsDay.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);*/
            }
        });

    }
}
