package com.tathink.brooch.brooch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MSI on 2016-08-24.
 */
public class SetBell extends Activity {
    TextView textView1, textView2;
    int temp;       //임시 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_bell);

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetBell.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        textView1 = (TextView)findViewById(R.id.setbell_textview_select);       //전화벨 종류 선택
        textView2 = (TextView)findViewById(R.id.setbell_textview_content);      //전화벨 종류 선택 문구

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화벨 종류 선택 처리
                showDialog(1);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화벨 종류 선택 처리
                showDialog(1);
            }
        });

        ((Button)findViewById(R.id.setbell_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(SetBell.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        switch (id){
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(SetBell.this);
                final String str[] = {"전화벨", "Kill Bill", "Magic Mamaliga", "Marry You", "Minions"};
                builder.setTitle("알림벨 종류 선택").setPositiveButton("선택완료", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(getApplicationContext(), str[temp] + "선택됨", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("취소", null).setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        temp = which;
                    }
                });

                return builder.create();
        }
        return super.onCreateDialog(id);

    }
}
