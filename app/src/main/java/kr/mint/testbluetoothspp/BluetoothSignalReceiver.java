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
    public boolean prefSave = false;
    private int signal = 0;
    private int signal_safe_DB = 0;   //ninny
    private  Context context;
    private static int danger_count=0;


    @Override
    public void onReceive(final Context $context, Intent $intent) {
        context = $context;

        //프리퍼런스 처리
        getPreferences($context);
/*        if (BTService.brooch_safe == false) {
            signal = Integer.parseInt($intent.getStringExtra("signal"));
        } else if(BTService.brooch_safe == true) {
            // signal_safe = true;
            signal_safe_DB = Integer.parseInt($intent.getStringExtra("signal_safe"));
        }*/

        if (prefSave != false) {

            if (BTService.brooch_safe == false) {
                signal = Integer.parseInt($intent.getStringExtra("signal"));
            } else {
               // signal_safe = true;
                signal_safe_DB = Integer.parseInt($intent.getStringExtra("signal_safe"));
            }
            avg = (min + max) / 2;
            avgSub = (min + avg) / 2;     //소리쳤을 때 min, max의 평균
            Log.d("----------구간별 값----------", "min:" + min + "   max:" + max + "   avg:" + avg + "   avgSub:" + avgSub + "   signal:" + signal);

            //DB 객체 생성
            final DBManager dbManager = new DBManager(ContextUtil.CONTEXT.getApplicationContext(), "STRESS.db", null, 1);

            Log.i("BluetoothSignalReceiver.java | onReceive", "|action : " + $intent.getAction() + "| signal : " + $intent.getStringExtra("signal") + " dB|");
            Toast.makeText($context, $intent.getStringExtra("signal"), Toast.LENGTH_SHORT).show();

            Log.i("BluetoothSignalReceiver.java | onReceive", "|action : " + $intent.getAction() + "| signal_safe : " + $intent.getStringExtra("signal_safe") + " dB|");
            Toast.makeText($context, $intent.getStringExtra("signal_safe"), Toast.LENGTH_SHORT).show();



            //db값 위험구간일 때 벨소리 처리----------------------------------------------------------------------
//            signal = Integer.parseInt($intent.getStringExtra("signal").substring(0, $intent.getStringExtra("signal").length()-3));
//        signal_safe = Integer.parseInt($intent.getStringExtra("signal_safe").substring(0, $intent.getStringExtra("signal_safe").length()-3));

            if(BTService.brooch_safe == true && signal_safe_DB < min && signal_safe_DB != 0 && BTService.config_check ==false){   //ninny
                //안전구간 Activity Event
                signal_safe_DB = 0;
                danger_count=0;
                Log.d("----------------------------------안 전 구 간----------------------------------", "EventActivitySafe");
                Intent i = new Intent($context, EventActivitySafe.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

/*                final CountDownTimer countDownTimer = new CountDownTimer(500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                                Log.d("-------BluetoothSignalReceiver   217   ","안 전 구 간안 전 구 간안 전 구 간");
                    }

                    @Override
                    public void onFinish() {
                        BTService.brooch_safe = false;
                    }
                };countDownTimer.start();*/

            }

           // Log.i("BluetoothSignalReceiver.java |", "danger_count|==" + danger_count + "|");
            else if(signal >= min && BTService.brooch_safe == false && BTService.config_check ==false) {
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
/*                    if(signal_safe == true && signal_safe_DB < min){
                        //안전구간 Activity Event
                        Log.d("----------------------------------안 전 구 간----------------------------------", "EventActivitySafe");
                        Intent i = new Intent($context, EventActivitySafe.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pi = PendingIntent.getActivity($context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pi.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                        signal_safe = false;
                    }*/

                    //경고, 심각, 경고 이벤트 처리
                    if (signal >= min && signal < avgSub) {
                        //주의 Brooch Event
                        signal = 0;
                        danger_count=0;
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

                        final CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
//                                Log.d("-------BluetoothSignalReceiver   217   ");
                            }

                            @Override
                            public void onFinish() {
//                                try {
//                                    Thread.sleep(3000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        };countDownTimer.start();

                    } else if (signal >= avgSub && signal < avg) {
                        signal = 0;
                        danger_count=0;
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

                        final CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
//                                Log.d("-------BluetoothSignalReceiver   217   ");
                            }

                            @Override
                            public void onFinish() {
//                                try {
//                                    Thread.sleep(3000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        };countDownTimer.start();

                    }
                    else if(signal >= avg && danger_count >= 1) {
                        signal = 0;
                        danger_count =0;
                        //경고구간 Activity Event2
                        try {
                            BTService.writesSelect(10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("----------------------------------경 고 구 간2----------------------------------", "EventActivityWarning");
                        Intent i = new Intent(context, EventActivityWarning2.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pi.send();
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }


//                        final CountDownTimer countDownTimer = new CountDownTimer(bvTime*1000, 1000) {
                        final CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
//                                Log.d("-------BluetoothSignalReceiver   217   ");
                            }

                            @Override
                            public void onFinish() {
//                                try {
//                                    Thread.sleep(bvTime*1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        };countDownTimer.start();
                    }
                    else if (signal >= avg ) {
                        signal = 0;
                        danger_count++;
                        Log.i("BluetoothSignalReceiver.java |", "danger_count  187|==" + danger_count + "|");
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

//                        final CountDownTimer countDownTimer = new CountDownTimer(bvTime*1000, 1000) {
                        final CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
//                                Log.d("-------BluetoothSignalReceiver   217   ");
                            }

                            @Override
                            public void onFinish() {
//                                try {
//                                    Thread.sleep(bvTime*1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        };countDownTimer.start();

                    }
                }
            }

            //db값 위험구간일 때 벨소리 처리----------------------------------------------------------------------
        } else {
            getPreferences($context);
        }

    }

    private void getPreferences(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        min = pref.getInt("rvMin", 0);
        max = pref.getInt("rvMax", 0);
        bvTime = pref.getInt("bvTime", 5);
        prefSave = pref.getBoolean("prefSave", false);
    }

}
