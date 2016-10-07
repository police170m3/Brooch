package kr.mint.testbluetoothspp;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class BluetoothSignalReceiver extends BroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;
    public int temp = 0;
    @Override
    public void onReceive(Context $context, Intent $intent) {

        //BT 연결 처리 - sejin
        if (BluetoothDevice.ACTION_ACL_DISCONNECTED == $intent.getAction()) {
            Log.d("TATHINK-----", "BT is Disconnected");
        }
//        //BT 연결 처리 - sejin

        /*//DB 객체 생성
        final DBManager dbManager = new DBManager(ContextUtil.CONTEXT.getApplicationContext(), "STRESS.db", null, 1);*/

        Log.i("BluetoothSignalReceiver.java | onReceive", "|action : " + $intent.getAction() + "| signal : " + $intent.getStringExtra("signal") + "|");
        Toast.makeText($context, $intent.getStringExtra("signal"), Toast.LENGTH_SHORT).show();
        //      Intent intent = new Intent($context, MainActivity.class);
//      intent.setAction($intent.getAction());
//      intent.putExtra("msg", $intent.getStringExtra("signal"));
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      $context.startActivity(intent);




        /*//db값 70 이상일때 벨소리 처리----------------------------------------------------------------------
        int signal = 0;
        signal = Integer.parseInt($intent.getStringExtra("signal").substring(0, $intent.getStringExtra("signal").length()-3));
        if(signal >= 70) {
            //dB값 70 이상일때 SQLite에 저장
            //android.text.format.DateFormat df = new android.text.format.DateFormat();
            //String date = df.format("yyyy-MM-dd HH:mm", new java.util.Date()).toString();
            //dbManager.insert("insert into STRESS_INFO values(null, '" + date + "', " + signal + ");");
            dbManager.insert("insert into STRESS_INFO values(null, datetime('now', '+9 hours'), " + signal + ");"); //UTC기준 국제표준시에 +9하면 우리나라 시간
            //dB값 70 이상일때 SQLite에 저장

            //꺼진 화면 wake up 처리/////////////////////////////////////////////////////////////////////
            if(sCpuWakeLock != null){
                return;
            }
            PowerManager pm = (PowerManager) $context.getSystemService(Context.POWER_SERVICE);
            sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "hi");
            sCpuWakeLock.acquire();
            if(sCpuWakeLock != null){
                sCpuWakeLock.release();
                sCpuWakeLock = null;
            }
            //꺼진 화면 wake up 처리/////////////////////////////////////////////////////////////////////

            Intent i = new Intent($context, EventActivityWarning.class);
            PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
            try {
                pi.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
        //db값 70 이상일때 벨소리 처리----------------------------------------------------------------------*/
    }

}
