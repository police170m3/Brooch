package com.tathink.brooch.brooch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import kr.mint.testbluetoothspp.BTService;
import kr.mint.testbluetoothspp.ConnectionReceiver;

/**
 * Created by MSI on 2016-08-18.
 */
public class SetCall extends Activity {
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 200;
    private BluetoothAdapter mBTAdapter;
    private BTService _btService;

    public boolean prefSave = false;
    static public boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_call);

        Button callButton = (Button)findViewById(R.id.setcall_btn_call);
        BTService.config_check = true;

        //이전, 다음 버튼 처리////////////////////////////////////////////////////////////////////// sejin 2016.11.01
        Button previousButton = (Button) findViewById(R.id.previous_btn);
        Button nextButton = (Button) findViewById(R.id.next_btn);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        //이전, 다음 버튼 처리//////////////////////////////////////////////////////////////////////

        //BT 연결처리
        _btService = new BTService(getApplicationContext());
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!ConnectionReceiver.btCheck){
            ConnectionReceiver.btCheck = true;
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
        }
        //BT 연결처리

        //home 버튼 처리
        getPreferences();
        if(prefSave) {
            ImageView home = (ImageView) findViewById(R.id.home);
            home.setImageResource(R.drawable.home);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView 클릭시 이벤트 처리........
                    Intent i = new Intent(SetCall.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    BTService.config_check = false;
                }
            });

            //이전, 다음 버튼 활성화 및 이벤트 처리///////////////////////// sejin 2016.11.01
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);

            //이전 이벤트 처리
            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent i = new Intent(SetCall.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            //다음 이벤트 처리
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    BTService.FREE_PASS = true;
                    Intent i = new Intent(SetCall.this, SetNormalVoice.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);
                }
            });
            //이전, 다음 버튼 활성화 및 이벤트 처리/////////////////////////
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //녹음명령
                try {                          //ninny
                    BTService.writesSelect(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }                         //ninny
                //녹음명령

                flag = true;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);

                try {                         //ninny
                    Thread.sleep(33000);
                    try {
                        BTService.writesSelect(2);
                        Thread.sleep(5000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }                         //ninny
            }
        });
    }

    @Override
    protected void onRestart(){
        if(flag == true) {
            flag = false;
            Intent i = new Intent(SetCall.this, SetNormalVoice.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            super.onRestart();
        }
        else {
            super.onRestart();
        }
    }

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        prefSave = pref.getBoolean("prefSave", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("MainActivity.java | onActivityResult", "|==" + requestCode + "|" + resultCode + "(ok = " + RESULT_OK + ")|" + data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ENABLE_BT) {
            discovery();
        } else if (requestCode == REQUEST_CONNECT_DEVICE_INSECURE) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            Log.i("MainActivity.java | onActivityResult", "|==" + address + "|");
            if (TextUtils.isEmpty(address)) {
                return;
            }

            BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
            _btService.connect(device);
        }
    }

    private void discovery() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }

}
