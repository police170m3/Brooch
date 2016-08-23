package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetText extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_text);

        //설정하기 버튼
        ((Button) findViewById(R.id.settext_btn_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //문장 입력하러 액티비티 이동
                Intent i = new Intent(SetText.this, SetTextInput.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        //다음에하기 버튼
        ((Button) findViewById(R.id.settext_btn_nexttime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //액티비티 이동
                Intent i = new Intent(SetText.this, SetSMS.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }
}
