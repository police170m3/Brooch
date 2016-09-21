package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetTextInput extends Activity {
    public String eventText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_text_input);
        final EditText editText = (EditText) findViewById(R.id.settextinput_editText);

        getPreferences();
        if(eventText != ""){
            editText.setText(eventText);
        }

        //확인 버튼(입력값 저장 처리)
        ((Button)findViewById(R.id.settextinput_btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventText = editText.getText().toString();
                savePreferences(eventText);

                //액티비티 이동
                Intent i = new Intent(SetTextInput.this, SetSMS.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    private void savePreferences(String eventText){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("eventText", eventText);
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        eventText = pref.getString("eventText", "");
    }
}
