package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public boolean prefSave = false;
    public int min = 0, max = 0;    //프리퍼런스 값 저장 변수(min-목소리 최소 dB, Max-목소리 최대 dB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ragevoice);

        // Setup the new range seek bar
        final RangeSeekBar rangeSeekBar = new RangeSeekBar(this);
        // Set the range
        getPreferences();   //프리퍼런스 값 읽어오기....
        rangeSeekBar.setTextAboveThumbsColor(R.color.common_signin_btn_dark_text_default);
        rangeSeekBar.setRangeValues(30, 80);
        rangeSeekBar.setSelectedMinValue(min);   //추후 프리퍼런스의 값 읽어와서 인자전달
        rangeSeekBar.setSelectedMaxValue(max);   //추후 프리퍼런스의 값 읽어와서 인자전달

        //home 버튼 처리
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
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
        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                //range seek bar 드레그 했을때 max, max 값 구하기............
                /*Log.d("min------------", ""+ minValue);
                Log.d("max------------", ""+ maxValue);*/
                min = (int) minValue;
                max = (int) maxValue;
            }
        });

        ((Button)findViewById(R.id.setragevoice_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //측정된 값으로 설정하기
                //minValue, maxValue값 preference에 저장하기
                savePreferences(min, max);

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

    private void savePreferences(int min, int max){
        /*Log.d("44444444444444", "" + min);
        Log.d("44444444444444", "" + max);*/
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("rvMin", min);
        editor.putInt("rvMax", max);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        min = pref.getInt("rvMin", 0);
        max = pref.getInt("rvMax", 0);
        prefSave = pref.getBoolean("prefSave", false);
        /*Log.d("5555555555555555", ""+ pref.getInt("nvMin", 0));
        Log.d("5555555555555555", ""+ pref.getInt("nvMax", 0));*/
    }
}
