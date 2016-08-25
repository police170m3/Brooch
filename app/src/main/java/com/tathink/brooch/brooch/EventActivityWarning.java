package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.Random;

import static com.tathink.brooch.brooch.R.raw.kill_bill;

/**
 * Created by MSI on 2016-08-25.
 */
public class EventActivityWarning extends Activity{
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventactivity_warning);

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

    private void startAlarm(MediaPlayer player) throws java.io.IOException, IllegalArgumentException, IllegalStateException{
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {   // 현재 Alarm 볼륨 구함
            player.setAudioStreamType(AudioManager.STREAM_ALARM);    // Alarm 볼륨 설정
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
