package kr.mint.testbluetoothspp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tathink.brooch.brooch.DeviceListActivity;
import com.tathink.brooch.brooch.R;

import java.io.IOException;

/**
 * Created by MSI on 2016-10-07.
 */

public class Send extends Activity {
    private static final int REQUEST_ENABLE_BT = 100;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 200;
    protected BTService.ConnectedThread mmConnectedThread;
    private BluetoothAdapter mBTAdapter;
    private BTService _btService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

        ContextUtil.CONTEXT = getApplicationContext();

        _btService = new BTService(getApplicationContext());

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null) {
            // device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "device does not support Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!mBTAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        checkIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("MainActivity.java | onActivityResult", "|==" + requestCode + "|" + resultCode + "(ok = " + RESULT_OK + ")|" + data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_ENABLE_BT) {
            discovery();
        } else if (requestCode == REQUEST_CONNECT_DEVICE_INSECURE) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            Log.i("MainActivity.java | onActivityResult", "|==" + address + "|");
            if (TextUtils.isEmpty(address))
                return;

            BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
            _btService.connect(device);
        }
    }


    private void checkIntent(Intent $intent) {
        Log.i("MainActivity.java | checkIntent", "|==" + $intent.getAction() + "|");
        if ("kr.mint.bluetooth.receive".equals($intent.getAction())) {
            Log.i("MainActivity.java | checkIntent", "|==" + $intent.getStringExtra("msg") + "|");

        }
    }

    public void onBtnSend(View v){
        try {
            mmConnectedThread.writesSelect(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onExitClick(View v) {

        Toast.makeText(this, "exit", Toast.LENGTH_LONG).show();
        mmConnectedThread.cancel();
        mBTAdapter.disable();
        ReConnectService.ReConnectServiceStop = true;

        finish();
    }

    private void discovery() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
    }

}
