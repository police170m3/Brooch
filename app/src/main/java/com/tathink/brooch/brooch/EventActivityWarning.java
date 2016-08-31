package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by MSI on 2016-08-25.
 */
public class EventActivityWarning extends Activity{
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventactivity_warning);

        //꺼진 화면에서 화면 활성화///////////////////////////////////////////////////////////////////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //꺼진 화면에서 화면 활성화///////////////////////////////////////////////////////////////////

        //임시 이벤트 처리
        Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minions);
        playMusic(alert);
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

        findViewById(R.id.ringtone_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                playMusic(alert);
            }
        });

        findViewById(R.id.stop1234_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopMusic();
                finish();
            }
        });

        findViewById(R.id.play1_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kill_bill);
                playMusic(alert);
            }
        });
        findViewById(R.id.play2_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.magic_mamaliga);
                playMusic(alert);
            }
        });
        findViewById(R.id.play3_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.marry_you);
                playMusic(alert);
            }
        });
        findViewById(R.id.play4_btn).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Uri alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minions);
                playMusic(alert);
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
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {   // 현재 Alarm 볼륨 구함
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);    // Alarm 볼륨 설정
            player.setLooping(true);    // 음악 반복 재생
            player.prepare();   // 3. 재생 준비
            player.start();    // 4. 재생 시작
        }
    }

    private void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();     // 5. 재생 중지
            mMediaPlayer.release();    // 6. MediaPlayer 리소스 해제
            mMediaPlayer = null;
        }
    }
}