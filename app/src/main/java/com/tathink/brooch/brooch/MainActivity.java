package com.tathink.brooch.brooch;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import kr.mint.testbluetoothspp.BTService;
import kr.mint.testbluetoothspp.BluetoothSignalReceiver;
import kr.mint.testbluetoothspp.ConnectionReceiver;
import kr.mint.testbluetoothspp.ReConnectService;

public class MainActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 200;
    private BluetoothAdapter mBTAdapter;
    private BTService _btService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BTService.config_check = false;
        BluetoothSignalReceiver.activity_close = true;

        ImageView gear = (ImageView) findViewById(R.id.set);
        ImageView bt = (ImageView) findViewById(R.id.bt);

        //BT 연결처리
        _btService = new BTService(getApplicationContext());
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        checkBluetooth();

        /*
        //BT연결 처리는 블루투스 아이콘 눌러서 처리
        if(!ConnectionReceiver.btCheck){
            ConnectionReceiver.btCheck = true;
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
        }*/

        //현상태점검
/*        if(ConnectionReceiver.btCheck) {
            try {                          //ninny
                BTService.writesSelect(10);
            } catch (IOException e) {
                e.printStackTrace();
            }                         //ninny
        }*/
        //현상태점검

        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(MainActivity.this, SetCall.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                if(ConnectionReceiver.btCheck != true) {
                    ReConnectService.ReConnectServiceStop = true; //ninny 10월 26일 6시35분
                    Intent i = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(i, REQUEST_CONNECT_DEVICE_INSECURE);
                }
                else
                    Toast.makeText(getApplicationContext(), "블루투스가 연결되어 있습니다", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.btnStatistics)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(MainActivity.this, StatisticsMain.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

      /*//테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////
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
        //테스트용 버튼 이벤트//////////////////////////////////////////////////////////////////////*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        BTService.config_check = false;
        BluetoothSignalReceiver.activity_close = true;
        //현상태점검
/*        if(ConnectionReceiver.btCheck) {    //ninny
            try {
                BTService.writesSelect(10);
            } catch (IOException e) {
                e.printStackTrace();
            }                         //ninny
        }    */           //ninny
        //현상태점검
    }
/*    @Override
    protected void onPause(){
        super.onPause();

        BluetoothSignalReceiver.activity_close = false;
        Log.d("onPause99999999999999999999999999999999999999999999", " : " + BluetoothSignalReceiver.activity_close);
        //unregisterReceiver(myReceiver);
    }*/

    @Override
    public void onBackPressed() {
        BluetoothSignalReceiver.activity_close = false;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("MainActivity.java | onActivityResult", "requestCode==" + requestCode + "resultCode==" + resultCode + "(ok = " + RESULT_OK + ")|" + data);
        if (resultCode != RESULT_OK || ConnectionReceiver.btCheck == true) {   //ninny
            return;
        }
        else if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) { // 블루투스 활성화 상태
                Toast.makeText(getApplicationContext(), "블루투스를 활성화 합니다", Toast.LENGTH_LONG).show();
                Log.i("211MainActivity.java | onActivityResult", "requestCode==" + requestCode + "resultCode==" + resultCode + "(ok = " + RESULT_OK + ")|" + data);
                discovery();
            } else if (resultCode == RESULT_CANCELED) { // 블루투스 비활성화 상태 (종료)
                Toast.makeText(getApplicationContext(), "블루투스를 사용할 수 없어 프로그램을 종료합니다", Toast.LENGTH_LONG).show();
                Log.i("215MainActivity.java | onActivityResult", "requestCode==" + requestCode + "resultCode==" + resultCode + "(ok = " + RESULT_OK + ")|" + data);
                finish();
            }                                                                     //ninny
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

    void checkBluetooth() {
        /*
               getDefaultAdapter() : 만일 폰에 블루투스 모듈이 없으면 null 을 리턴한다.
                               이경우 Toast를 사용해 에러메시지를 표시하고 앱을 종료한다.
		 */
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null) {  // 블루투스 미지원
            Toast.makeText(getApplicationContext(), "기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            finish();  // 앱종료
        } else { // 블루투스 지원
            /** isEnable() : 블루투스 모듈이 활성화 되었는지 확인.
             *               true : 지원 ,  false : 미지원
             */
            if (!mBTAdapter.isEnabled()) { // 블루투스 지원하며 비활성 상태인 경우.
                Toast.makeText(getApplicationContext(), "현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // REQUEST_ENABLE_BT : 블루투스 활성 상태의 변경 결과를 App 으로 알려줄 때 식별자로 사용(0이상)
				/*
                  startActivityForResult 함수 호출후 다이얼로그가 나타남
                  "예" 를 선택하면 시스템의 블루투스 장치를 활성화 시키고
                  "아니오" 를 선택하면 비활성화 상태를 유지 한다.
                  	선택 결과는 onActivityResult 콜백 함수에서 확인할 수 있다.
				 */
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void discovery() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }
}