package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import kr.mint.testbluetoothspp.BluetoothSignalReceiver;

/**
 * Created by MSI on 2016-08-25.
 */
public class EventActivitySerious extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventactivity_serious);
        BluetoothSignalReceiver.activity_close = true;

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(EventActivitySerious.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        BluetoothSignalReceiver.activity_close = false;
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothSignalReceiver.activity_close = true;
    }
}
