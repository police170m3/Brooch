package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetVibration extends Activity {
    public boolean prefSave = false;
    public int bvTime = 5;
    RadioButton radioBtn_on;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_vibration);

        //프리퍼런스 값 읽어서 선택처리
        getPreferences();
        switch (bvTime) {
            case 5:
                radioBtn_on = (RadioButton)findViewById(R.id.radioButton1);
                radioBtn_on.setChecked(true);
                break;
            case 7:
                radioBtn_on = (RadioButton)findViewById(R.id.radioButton2);
                radioBtn_on.setChecked(true);
                break;
            case 10:
                radioBtn_on = (RadioButton)findViewById(R.id.radioButton3);
                radioBtn_on.setChecked(true);
                break;
            case 15:
                radioBtn_on = (RadioButton)findViewById(R.id.radioButton4);
                radioBtn_on.setChecked(true);
                break;
        }

        //라디오 버튼 선택 값 가져오기
        RadioGroup rdgroup = (RadioGroup)findViewById(R.id.rdgroup);
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //RadioButton radio_btn = (RadioButton)findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radioButton1:
                        bvTime = 5;
                        break;
                    case R.id.radioButton2:
                        bvTime = 7;
                        break;
                    case R.id.radioButton3:
                        bvTime = 10;
                        break;
                    case R.id.radioButton4:
                        bvTime = 15;
                        break;
                }
            }
        });
        //라디오 버튼 선택 값 가져오기

        //home 버튼 처리
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetVibration.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        ((Button)findViewById(R.id.setvibration_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프리퍼런스 값 저장
                savePreferences();

                //통계화면으로 이동 처리
                Intent i = new Intent(SetVibration.this, SetBell.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("bvTime", bvTime);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        bvTime = pref.getInt("bvTime", 5);
        prefSave = pref.getBoolean("prefSave", false);
    }
}
