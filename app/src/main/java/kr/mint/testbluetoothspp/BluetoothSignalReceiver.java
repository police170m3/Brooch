package kr.mint.testbluetoothspp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.tathink.brooch.brooch.EventActivityWarning;

public class BluetoothSignalReceiver extends BroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;

    @Override
    public void onReceive(Context $context, Intent $intent) {
        Log.i("BluetoothSignalReceiver.java | onReceive", "|action : " + $intent.getAction() + "| signal : " + $intent.getStringExtra("signal") + "|");
        Toast.makeText($context, $intent.getStringExtra("signal"), Toast.LENGTH_SHORT).show();
        //      Intent intent = new Intent($context, MainActivity.class);
//      intent.setAction($intent.getAction());
//      intent.putExtra("msg", $intent.getStringExtra("signal"));
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      $context.startActivity(intent);


        //db값 70 이상일때 벨소리 처리----------------------------------------------------------------------
        int signal = 0;
        signal = Integer.parseInt($intent.getStringExtra("signal").substring(0, $intent.getStringExtra("signal").length()-3));
        if(signal >= 70) {
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
        //db값 70 이상일때 벨소리 처리----------------------------------------------------------------------

        /* SQLite processing............................................................................................................*//*
        int db = 55;     //dB값 임시 저장

        //현재 시간
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date = df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();

        final DBManager dbManager = new DBManager($context, "DBINFO.db", null, 1);

        dbManager.insert("insert into DB_INFO value(null, '" + date + "', " + db + ");");

        *//* SQLite processing............................................................................................................*/
    }

}
