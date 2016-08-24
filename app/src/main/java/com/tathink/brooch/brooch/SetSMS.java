package com.tathink.brooch.brooch;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetSMS extends Activity {
    public TextView textIn, textInName;
    Button buttonAdd;
    LinearLayout container;
    int cnt = 0;
//    TextView textView = (TextView)findViewById(R.id.textin);
    TextView textOut, textOutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sms);

        textIn = (TextView) findViewById(R.id.textin);
        textInName = (TextView)findViewById(R.id.textinName);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetSMS.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //설정하기 이벤트 처리
        ((Button)findViewById(R.id.setsms_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //설정 첫 액티비티로 이동 처리
                Intent i = new Intent(SetSMS.this, SetVibration.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        //다음에하기 이벤트 처리
        ((Button)findViewById(R.id.setsms_btn_nexttime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //설정 첫 액티비티로 이동 처리
                Intent i = new Intent(SetSMS.this, SetVibration.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        //텍스트뷰 클릭시 이벤트 처리
        //전화번호 텍스트뷰 누를때
        textIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소록에서 전화번호 가져오기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        //텍스트뷰 클릭시 이벤트 처리
        //이름 텍스트뷰 누를때
        textInName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소록에서 전화번호 가져오기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                if(cnt < 3) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.setting_sms_row, null);
                    textOut = (TextView) addView.findViewById(R.id.textout);
                    textOutName = (TextView) addView.findViewById(R.id.textoutName);
                    textOut.setText(textIn.getText().toString());
                    textOutName.setText(textInName.getText().toString());
                    Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) addView.getParent()).removeView(addView);
                            cnt--;
                        }
                    });


                    container.addView(addView, 0);
                    cnt++;
                } else {
                    Toast.makeText(SetSMS.this, "최대 3명까지 등록 가능합니다.", Toast.LENGTH_LONG).show();
                }
            }});

        LayoutTransition transition = new LayoutTransition();
        container.setLayoutTransition(transition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String name = cursor.getString(0);
            String number = cursor.getString(1);

            //긴 이름 짧게 처리 - 출력만 간략히...
            if(name.length() >= 8){
                textInName.setText(name.substring(0, 7) + "..");
                textIn.setText(number);
            } else {
                textInName.setText(name);
                textIn.setText(number);
            }
            cursor.close();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
