package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by MSI on 2016-08-23.
 */
public class SetTextInput extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_text_input);

        EditText editText = (EditText) findViewById(R.id.settextinput_editText);
        /*에디트 텍스트에 입력된 값 처장 처리
        .......................................
        .......................................
        .......................................
         */

        //확인 버튼(입력값 저장 처리)
        ((Button)findViewById(R.id.settextinput_btn_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력 문장값 저장
                /*문장 등록 처리 코드 작성.........................................................
                ...................................................................................
                 */
                //액티비티 이동
                Intent i = new Intent(SetTextInput.this, SetSMS.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }
}
