package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import kr.mint.testbluetoothspp.BTService;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetRageMeasure extends Activity {
    public boolean prefSave = false;
    public int angry_min = 0, angry_max = 0;    //프리퍼런스 값 저장 변수(min-목소리 최소 dB, Max-목소리 최대 dB)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_ragemeasure);

        final Toast toast = Toast.makeText(this, "측정중입니다.\n예문에 따라 소리쳐 주세요", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        findViewById(R.id.setragemeasure_btn_set).setEnabled(false); //ninny   측정중일때 버튼 비활성화

/*        try {// 화난목소리 측정
            Thread.sleep(33000);
            try {
                BTService.writesSelect(2);
                Thread.sleep(3000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        final CountDownTimer countDownTimer = new CountDownTimer(11*1000, 1000) {                         //ninny
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("-------min, max 측정중-------", "SetCall Class");
            }

            @Override
            public void onFinish() {
                Log.d("-------min, max 측정완료!!!-------", "SetCall Class");
                try {
                    BTService.writesSelect(2);
                    try {
                        Thread.sleep(3000);
                        findViewById(R.id.setragemeasure_btn_set).setEnabled(true); //ninny   측정후 버튼 활성화
                        Toast.makeText(SetRageMeasure.this,"측정완료 확인을 누르세요", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };countDownTimer.start();                         //ninny

        Button callButton = (Button)findViewById(R.id.setragemeasure_btn_set);

        //home 버튼 처리
        getPreferences();
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetRageMeasure.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetRageMeasure.this, SetRageVoice.class);
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

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        prefSave = pref.getBoolean("prefSave", false);
    }

}
