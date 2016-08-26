package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by MSI on 2016-08-18.
 */
public class SetNormalVoice extends Activity {
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_normalvoice);
        ImageView home = (ImageView) findViewById(R.id.home);

        // Setup the new range seek bar
        final RangeSeekBar rangeSeekBar = new RangeSeekBar(this);
        // Set the range
        rangeSeekBar.setTextAboveThumbsColor(R.color.common_signin_btn_dark_text_default);
        rangeSeekBar.setRangeValues(0, 200);
        rangeSeekBar.setSelectedMinValue(20);   //추후 프리퍼런스의 값 읽어와서 인자전달
        rangeSeekBar.setSelectedMaxValue(50);   //추후 프리퍼런스의 값 읽어와서 인자전달


        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.seekbar_placeholder);
        layout.addView(rangeSeekBar);

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                //range seek bar 드레그 했을때 max, max 값 구하기............
                Log.d("min------------", ""+ minValue);
                Log.d("max------------", ""+ maxValue);
            }
        });

        //선택한 값 읽어오기
        Log.d("values-----", ""+rangeSeekBar.getSelectedMaxValue()+"----------"+rangeSeekBar.getSelectedMinValue());

        ((Button)findViewById(R.id.setnormalvoice_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //측정된 값으로 설정하기
                /*설정하는 코드 작성....................
                ........................................
                .......................................*/

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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetNormalVoice.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
}
