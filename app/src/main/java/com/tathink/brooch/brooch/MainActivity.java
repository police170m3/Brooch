package com.tathink.brooch.brooch;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import kr.mint.testbluetoothspp.BTService;
import kr.mint.testbluetoothspp.ConnectionReceiver;
import kr.mint.testbluetoothspp.Send;

public class MainActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 200;
    private BluetoothAdapter mBTAdapter;
    private BTService _btService;
    BroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BTService.config_check = false;

        ImageView gear = (ImageView) findViewById(R.id.set);
        ImageView bt = (ImageView) findViewById(R.id.bt);

        //BT 연결처리
        _btService = new BTService(getApplicationContext());
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        /*
        //BT연결 처리는 블루투스 아이콘 눌러서 처리
        if(!ConnectionReceiver.btCheck){
            ConnectionReceiver.btCheck = true;
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
        }*/

        //현상태점검
        if(ConnectionReceiver.btCheck) {
            try {                          //ninny
                BTService.writesSelect(10);
            } catch (IOException e) {
                e.printStackTrace();
            }                         //ninny
        }
        //현상태점검

        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(MainActivity.this, SetCall.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivityForResult(i, REQUEST_CONNECT_DEVICE_INSECURE);
            }
        });

        ((Button)findViewById(R.id.btnStatistics)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, StatisticsMain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });


        //테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////
        ((Button)findViewById(R.id.safe_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivitySafe.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.caution_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivityCaution.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.serious_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivitySerious.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.warning_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivityWarning.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.warning2_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, EventActivityWarning2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.bt_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, BTMainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.dbtest_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, DBTestActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        ((Button)findViewById(R.id.send_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, Send.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
        //테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onResume() {
        super.onResume();
        BTService.config_check = false;
        //현상태점검
        if(ConnectionReceiver.btCheck) {    //ninny
            try {
                BTService.writesSelect(10);
            } catch (IOException e) {
                e.printStackTrace();
            }                         //ninny
        }               //ninny
        //현상태점검
    }
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        unregisterReceiver(myReceiver);
//    }

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
            //현상태점검
/*            try {                          //ninny
                BTService.writesSelect(10);
            } catch (IOException e) {
                e.printStackTrace();
            }    */                     //ninny
            //현상태점검
        }
    }

    private void discovery() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }
}
