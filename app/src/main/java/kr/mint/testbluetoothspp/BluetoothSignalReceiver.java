package kr.mint.testbluetoothspp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.tathink.brooch.brooch.EventActivityCaution;
import com.tathink.brooch.brooch.EventActivitySafe;
import com.tathink.brooch.brooch.EventActivitySerious;
import com.tathink.brooch.brooch.EventActivityWarning;
import com.tathink.brooch.brooch.EventActivityWarning2;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class BluetoothSignalReceiver extends BroadcastReceiver {
    private static PowerManager.WakeLock sCpuWakeLock;
    public int min = 0, max = 0, avg = 0, avgSub = 0, bvTime=0;    //프리퍼런스 값 저장 변수(min-목소리 최소 dB, Max-목소리 최대 dB, avg-y3+y5/2, avg2-y5(평균값))
    public  int signal = 0;
    private  Context context;

    @Override
    public void onReceive(Context $context, Intent $intent) {
        context = $context;

        //프리퍼런스 처리
        getPreferences($context);
        avg = (min + max) / 2;
        avgSub = (min + avg) / 2;     //소리쳤을 때 min, max의 평균
        Log.d("----------구간별 값----------", "min:" + min + "   max:" + max + "   avg:" + avg + "   avgSub:" + avgSub);

        //DB 객체 생성
        final DBManager dbManager = new DBManager(ContextUtil.CONTEXT.getApplicationContext(), "STRESS.db", null, 1);

        Log.i("BluetoothSignalReceiver.java | onReceive", "|action : " + $intent.getAction() + "| signal : " + $intent.getStringExtra("signal") + "|");
        Toast.makeText($context, $intent.getStringExtra("signal"), Toast.LENGTH_SHORT).show();
        //      Intent intent = new Intent($context, MainActivity.class);
//      intent.setAction($intent.getAction());
//      intent.putExtra("msg", $intent.getStringExtra("signal"));
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      $context.startActivity(intent);




        //db값 위험구간일 때 벨소리 처리----------------------------------------------------------------------
        signal = Integer.parseInt($intent.getStringExtra("signal").substring(0, $intent.getStringExtra("signal").length()-3));
        if(signal >= min) {
            //dB값 위험 구간중 최소값 이상일 때 SQLite에 저장
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

            //각 구간별 이벤트 액티비티 처리
            if (avg!=0) {
                // avg!=0 설정을 진행했을때
                if (signal >= min && signal < avgSub) {
                    //주의 Brooch Event
                    try {
                        BTService.writesSelect(4);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //주의구간 Activity Event
                    Log.d("----------------------------------주 의 구 간----------------------------------", "EventActivityCaution");
                    Intent i = new Intent($context, EventActivityCaution.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                } else if (signal >= avgSub && signal < avg) {
                    //심각 Brooch Event
                    try {
                        BTService.writesSelect(5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //심각구간 Activity Event
                    Log.d("----------------------------------심 각 구 간----------------------------------", "EventActivitySerious");
                    Intent i = new Intent($context, EventActivitySerious.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                } else if (signal >= avg) {
                    //경고 Brooch Event
                    try {
                        BTService.writesSelect(6);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //경고구간 Activity Event
                    Log.d("----------------------------------경 고 구 간----------------------------------", "EventActivityWarning");
                    Intent i = new Intent($context, EventActivityWarning.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }

                    //CountDownTime 를 이용하여 브로치 진동 시간 후(프리퍼런스 값 이용) 여전히 경고라면
                    //EventActivityWarning2 처리
                    CountDownTimer countDownTimer = new CountDownTimer(bvTime*1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d("-------In now, It's Warning Time-------", "will be check after bvTime");
                        }

                        @Override
                        public void onFinish() {
                            if (signal >= avg) {
                                //경고구간 Activity Event2
                                Log.d("----------------------------------경 고 구 간2----------------------------------", "EventActivityWarning");
                                Intent i = new Intent(context, EventActivityWarning2.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                                try {
                                    pi.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            } else if (signal < min) {
                                //안전구간 Activity Safe
                                Log.d("----------------------------------안 전 구 간----------------------------------", "EventActivityWarning");
                                Intent i = new Intent(context, EventActivitySafe.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                                try {
                                    pi.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };countDownTimer.start();

                }

            }



        }
        //db값 위험구간일 때 벨소리 처리----------------------------------------------------------------------
    }

    private void getPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        min = pref.getInt("rvMin", 0);
        max = pref.getInt("rvMax", 0);
        bvTime = pref.getInt("bvTime", 5);
    }

}
