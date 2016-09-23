package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by MSI on 2016-09-22.
 */

public class EventActivityWarning2 extends Activity {
    public String pic1, pic2, pic3, pic4, pic5, eventText, picURI;
    private MediaPlayer mMediaPlayer;
    int volume, temp, degree;
    private AudioManager audioManager;
    Bitmap resizeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventactivity_warning2);

        //사진 프리퍼런스 읽어오기
        getPreferences();

        if (temp != 0) {

            int randNum = (int) (Math.random() * temp);
            switch (randNum) {
                case 0:
                    degree = ImageUtil.GetExifOrientation(pic1);
                    resizeBitmap = ImageUtil.loadBackgroundBitmap(EventActivityWarning2.this, pic1);
                    break;
                case 1:
                    degree = ImageUtil.GetExifOrientation(pic2);
                    resizeBitmap = ImageUtil.loadBackgroundBitmap(EventActivityWarning2.this, pic2);
                    break;
                case 2:
                    degree = ImageUtil.GetExifOrientation(pic3);
                    resizeBitmap = ImageUtil.loadBackgroundBitmap(EventActivityWarning2.this, pic3);
                    break;
                case 3:
                    degree = ImageUtil.GetExifOrientation(pic4);
                    resizeBitmap = ImageUtil.loadBackgroundBitmap(EventActivityWarning2.this, pic4);
                    break;
                case 4:
                    degree = ImageUtil.GetExifOrientation(pic5);
                    resizeBitmap = ImageUtil.loadBackgroundBitmap(EventActivityWarning2.this, pic5);
                    break;
            }
            Bitmap rotateBitmap = ImageUtil.GetRotatedBitmap(resizeBitmap, degree);
            Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(rotateBitmap);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(roundBitmap);
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.unknown);
        }

        //프리퍼런스에 저장된 이벤트 텍스트 읽어서 화면에 출력
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(eventText);

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
                Intent i = new Intent(EventActivityWarning2.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new Button.OnClickListener(){
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

    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pic1 = pref.getString("pic1", "");
        pic2 = pref.getString("pic2", "");
        pic3 = pref.getString("pic3", "");
        pic4 = pref.getString("pic4", "");
        pic5 = pref.getString("pic5", "");
        eventText = pref.getString("eventText", "");

        temp = 0;
        if(pic1 != ""){ temp += 1;  }
        if(pic2 != ""){ temp += 1;  }
        if(pic3 != ""){ temp += 1;  }
        if(pic4 != ""){ temp += 1;  }
        if(pic5 != ""){ temp += 1;  }
    }
}
