package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by MSI on 2016-08-25.
 */
public class EventActivityWarning extends Activity{
    public int pbTime, pbKind;
    private MediaPlayer mMediaPlayer;
    int volume, currentTime = 0;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventactivity_warning);

        getPreferences();

        //꺼진 화면에서 화면 활성화///////////////////////////////////////////////////////////////////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //꺼진 화면에서 화면 활성화///////////////////////////////////////////////////////////////////

        //설정된 벨소리에 따라 이벤트 처리
        Uri alert;
        switch (pbKind) {
            case 0:
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                break;
            case 1:
                alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kill_bill);
                break;
            case 2:
                alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.magic_mamaliga);
                break;
            case 3:
                alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.marry_you);
                break;
            case 4:
                alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minions);
                break;
            default:
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                break;
        }
        playMusic(alert);

        CountDownTimer cntr_aCounter = new CountDownTimer(pbTime*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("EventActivityWarning : ", "Playing......");
            }

            @Override
            public void onFinish() {
                stopMusic();
            }
        };cntr_aCounter.start();
        ///////////////////


        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(EventActivityWarning.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        findViewById(R.id.stop1234_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopMusic();
                finish();
            }
        });
    }

    private void playMusic(Uri alert) {
        stopMusic();
        mMediaPlayer = new MediaPlayer();

        try{
            mMediaPlayer.setDataSource(this, alert);
            startAlarm(mMediaPlayer);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void startAlarm(MediaPlayer player) throws IOException, IllegalArgumentException, IllegalStateException{
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);    // Alarm 볼륨 설정
        player.setLooping(true);    // 음악 반복 재생
        player.prepare();   // 3. 재생 준비
        player.start();    // 4. 재생 시작
    }

    private void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();     // 5. 재생 중지
            if(volume != 15){
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }
            mMediaPlayer.release();    // 6. MediaPlayer 리소스 해제
            mMediaPlayer = null;
        }
    }

    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pbTime = pref.getInt("pbTime", 5);
        pbKind = pref.getInt("pbKind", 0);
    }
}
