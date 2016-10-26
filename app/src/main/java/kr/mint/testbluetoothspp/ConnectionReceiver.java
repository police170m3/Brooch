package kr.mint.testbluetoothspp;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {
    public static boolean btCheck = false;
    @Override
    public void onReceive(final Context $context, Intent $intent) {
        ContextUtil.CONTEXT = $context;

        Log.i("ConnectionReceiver.java | onReceive", "|===========" + $intent.getAction() + "|");

        String action = $intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//         reconnect($context, PreferenceUtil.lastConnectedDeviceAddress());
            Log.i("ConnectionReceiver.java | onReceive", "|==" + "연결 시도 중" + "|");
            BTService btService = new BTService($context);
            btService.connect(PreferenceUtil.lastConnectedDeviceAddress());
            return;
        }

        BluetoothDevice device = $intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String address = device.getAddress();
        if (TextUtils.isEmpty(address)) {
            return;
        }

        if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            reconnect($context, address);
            btCheck = false;
        } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            btCheck = true;
            String lastRequestAddress = PreferenceUtil.lastRequestDeviceAddress();
            if (TextUtils.isEmpty(lastRequestAddress))
                return;

            if (address.equals(lastRequestAddress)) {
                PreferenceUtil.putLastConnectedDeviceAddress(lastRequestAddress);
                Log.i("ConnectionReceiver.java | onReceive", "|==" + "연결 완료" + "|" + lastRequestAddress);
                ReConnectService.instance($context).stopReconnect();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText($context, "블루투스가 연결되었습니다.", Toast.LENGTH_LONG).show();

/*            Intent intent = new Intent($context, MainActivity.class);
            intent.setAction("kr.mint.bluetooth.receive");
            intent.putExtra("msg", "연결 완료");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            $context.startActivity(intent);*/ //ninny
            }
        }
    }


    private void reconnect(Context $context, String $address) {
        String lastConnectAddress = PreferenceUtil.lastConnectedDeviceAddress();
        Log.i("ConnectionReceiver.java | onReceive", "|==연결 시도할 주소 : " + lastConnectAddress + "|");
        if (TextUtils.isEmpty(lastConnectAddress))
            return;

        // 연결이 끊기면 1분 마다 스캔을 다시 한다.
        if ($address.equals(lastConnectAddress)) {
            Log.i("DisconnectedReceiver.java | onReceive", "|==" + "스캔 다시하기" + "|");
            ReConnectService.instance($context).autoReconnect();
        }
    }
}
