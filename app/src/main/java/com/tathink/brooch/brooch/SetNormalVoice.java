package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import kr.mint.testbluetoothspp.BTService;

/**
 * Created by MSI on 2016-08-18.
 */
public class SetNormalVoice extends Activity {
    public boolean prefSave = false;
    private boolean flag = false;
    public int min = 0, max = 0;    //프리퍼런스 값 저장 변수(min-목소리 최소 dB, Max-목소리 최대 dB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_normalvoice);
        BTService.config_check = true;
        // Setup the new range seek bar
        final RangeSeekBar rangeSeekBar = new RangeSeekBar(this);
        // Set the range
        getPreferences();   //프리퍼런스 값 읽어오기....
        rangeSeekBar.setTextAboveThumbsColor(R.color.common_signin_btn_dark_text_default);
        rangeSeekBar.setRangeValues(30, 80);

        //이전, 다음 버튼 처리////////////////////////////////////////////////////////////////////// sejin 2016.11.02
        Button previousButton = (Button) findViewById(R.id.previous_btn);
        Button nextButton = (Button) findViewById(R.id.next_btn);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        //이전, 다음 버튼 처리//////////////////////////////////////////////////////////////////////

        if(BTService.callrecv_max != null && BTService.callrecv_min != null) {                         //ninny
            min = Integer.parseInt(BTService.callrecv_min);
            max = Integer.parseInt(BTService.callrecv_max);
        } else if (BTService.FREE_PASS == true) {
            BTService.FREE_PASS = false;
            Log.d("SetNormalVoice","----------FREE PASS----------"+BTService.FREE_PASS);
        } else {      //sejin 2016.11.01
            Toast.makeText(this, "목소리 측정 시간이 짧아서 실패. 다시걸기하세요", Toast.LENGTH_LONG).show();                         //ninny
        }

        Log.i("SetNormalVoice.java" , BTService.callrecv_max + "|" + BTService.callrecv_min);
        rangeSeekBar.setSelectedMinValue(min);   //추후 프리퍼런스의 값 읽어와서 인자전달
        rangeSeekBar.setSelectedMaxValue(max);   //추후 프리퍼런스의 값 읽어와서 인자전달

        /*rangeSeekBar.setSelectedMinValue(Integer.parseInt(BTService.callrecv_min));   //추후 프리퍼런스의 값 읽어와서 인자전달
        rangeSeekBar.setSelectedMaxValue(Integer.parseInt(BTService.callrecv_max));   //추후 프리퍼런스의 값 읽어와서 인자전달*/

        //home 버튼 처리
        if(prefSave){
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetNormalVoice.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    BTService.config_check = false;
                }
            });

            //이전, 다음 버튼 활성화 및 이벤트 처리///////////////////////// sejin 2016.11.02
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            //이전 이벤트 처리
            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetNormalVoice.this, SetCall.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //다음 이벤트 처리
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetNormalVoice.this, SetRage.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //이전, 다음 버튼 활성화 및 이벤트 처리/////////////////////////
        }

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                //range seek bar 드레그 했을때 max, max 값 구하기............
                min = (int) minValue;
                max = (int) maxValue;
            }
        });

        ((Button)findViewById(R.id.setnormalvoice_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //측정된 값으로 설정하기
                //minValue, maxValue값 preference에 저장하기
                savePreferences(min, max);

                //설정 후 액티비티 이동
                Intent i = new Intent(SetNormalVoice.this, SetRage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.setnormalvoice_btn_remeasure)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다시 측정하기 - 평소 목소리
                Intent i = new Intent(SetNormalVoice.this, SetCall.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });


    }

    private void savePreferences(int min, int max){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("nvMin", min);
        editor.putInt("nvMax", max);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        min = pref.getInt("nvMin", 0);
        max = pref.getInt("nvMax", 0);
        prefSave = pref.getBoolean("prefSave", false);
    }
}
