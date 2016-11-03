package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import kr.mint.testbluetoothspp.BTService;

/**
 * Created by MSI on 2016-08-19.
 */
public class SetRage extends Activity{
    public boolean prefSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_rage);
        BTService.config_check = true;
        Button callButton = (Button)findViewById(R.id.setrage_btn_set);

        //이전, 다음 버튼 처리////////////////////////////////////////////////////////////////////// sejin 2016.11.02
        Button previousButton = (Button) findViewById(R.id.previous_btn);
        Button nextButton = (Button) findViewById(R.id.next_btn);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        //이전, 다음 버튼 처리//////////////////////////////////////////////////////////////////////

        //home 버튼 처리
        getPreferences();
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetRage.this, MainActivity.class);
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
                    Intent i = new Intent(SetRage.this, SetNormalVoice.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //다음 이벤트 처리
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    BTService.FREE_PASS = true;
                    Intent i = new Intent(SetRage.this, SetRageVoice.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //이전, 다음 버튼 활성화 및 이벤트 처리/////////////////////////
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {                         //ninny
                    BTService.writesSelect(9);
                } catch (IOException e) {
                    e.printStackTrace();
                }                         //ninny

                Intent i = new Intent(SetRage.this, SetRageMeasure.class);
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
