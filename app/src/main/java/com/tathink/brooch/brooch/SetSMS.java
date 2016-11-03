package com.tathink.brooch.brooch;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetSMS extends Activity {
    public boolean prefSave = false;
    public String [] name = new String[3];
    public String [] sms = new String[3];
    public TextView textIn, textInName;
    Button buttonAdd;
    LinearLayout container;
    int cnt = 0, check = 0;
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

        getPreferences();
        for(int i = 0; i < 3; i++) {
            if(sms[i] != ""){
                cnt = cnt + 1;
            }
        }
        for(check = 0; check < 3; check++){
            if(sms[check] != ""){
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.setting_sms_row, null);
                textOut = (TextView) addView.findViewById(R.id.textout);
                textOutName = (TextView) addView.findViewById(R.id.textoutName);
                textOut.setText(sms[check]);
                textOutName.setText(name[check]);

                Button buttonRemove = (Button) addView.findViewById(R.id.remove);
                buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView removeTextView = (TextView) addView.findViewById(R.id.textout);
                        String temp = removeTextView.getText().toString();
                        for(int i = 0; i < 3; i++){
                            if (sms[i] == temp) {
                                name[i] = "";
                                sms[i] = "";
                                cnt--;
                                break;
                            }
                        }
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });

                container.addView(addView, 0);
            }
        }

        //이전, 다음 버튼 처리////////////////////////////////////////////////////////////////////// sejin 2016.11.02
        Button previousButton = (Button) findViewById(R.id.previous_btn);
        Button nextButton = (Button) findViewById(R.id.next_btn);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        //이전, 다음 버튼 처리//////////////////////////////////////////////////////////////////////

        //home 버튼 처리
        if (prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetSMS.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });

            //이전, 다음 버튼 활성화 및 이벤트 처리///////////////////////// sejin 2016.11.02
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            //이전 이벤트 처리
            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetSMS.this, SetText.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //다음 이벤트 처리
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetSMS.this, SetVibration.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //이전, 다음 버튼 활성화 및 이벤트 처리/////////////////////////
        }

        //등록하기 이벤트 처리
        ((Button)findViewById(R.id.setsms_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프리퍼런스에 저장
                savePreferences();

                //액티비티 이동
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

                    for(int i = 0; i < 3; i++){
                        if(sms[i] == ""){
                            name[i] = textOutName.getText().toString();
                            sms[i] = textOut.getText().toString();
                            break;
                        }
                    }

                    Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                    buttonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextView removeTextView = (TextView) addView.findViewById(R.id.textout);
                            String temp = removeTextView.getText().toString();
                            for(int i = 0; i < 3; i++){
                                if(sms[i] == temp){
                                    name[i] = "";
                                    sms[i] = "";
                                    break;
                                }
                            }

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

    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("sms1", sms[0]);
        editor.putString("sms2", sms[1]);
        editor.putString("sms3", sms[2]);
        editor.putString("name1", name[0]);
        editor.putString("name2", name[1]);
        editor.putString("name3", name[2]);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        sms[0] = pref.getString("sms1", "");
        sms[1] = pref.getString("sms2", "");
        sms[2] = pref.getString("sms3", "");
        name[0] = pref.getString("name1", "");
        name[1] = pref.getString("name2", "");
        name[2] = pref.getString("name3", "");
        prefSave = pref.getBoolean("prefSave", false);
    }
}
