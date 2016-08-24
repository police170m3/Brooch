package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetRageVoice   extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ragevoice);

        ImageView home = (ImageView) findViewById(R.id.home);

        // Setup the new range seek bar
        RangeSeekBar rangeSeekBar = new RangeSeekBar(this);
        // Set the range
        rangeSeekBar.setTextAboveThumbsColor(R.color.common_signin_btn_dark_text_default);
        rangeSeekBar.setRangeValues(0, 300);
        rangeSeekBar.setSelectedMinValue(20);
        rangeSeekBar.setSelectedMaxValue(88);

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        ((Button)findViewById(R.id.setragevoice_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //측정된 값으로 설정하기
                /*측정 값 코드 작성.........................
                ............................................
                 */

                //저장 후 액티비티 이동
                //다음
                Intent i = new Intent(SetRageVoice.this, SetPic.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.setragevoice_btn_remeasure)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다시 측정하기 - 화났을때 목소리
                //현재 액티비티를 종료하여 이전 액티비티(SetRage)로 전환
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetRageVoice.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
    }
}
