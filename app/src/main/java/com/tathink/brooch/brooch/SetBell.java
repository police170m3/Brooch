package com.tathink.brooch.brooch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MSI on 2016-08-24.
 */
public class SetBell extends Activity {
    TextView textView1, textView2;
    int temp;       //임시 변수
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_bell);

        ImageView home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView 클릭시 이벤트 처리........
                Intent i = new Intent(SetBell.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        textView1 = (TextView) findViewById(R.id.setbell_textview_select);       //전화벨 종류 선택
        textView2 = (TextView) findViewById(R.id.setbell_textview_content);      //전화벨 종류 선택 문구

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화벨 종류 선택 처리
                showDialog(1);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화벨 종류 선택 처리
                showDialog(1);
            }
        });

        ((Button) findViewById(R.id.setbell_btn_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통계화면으로 이동 처리
                Intent i = new Intent(SetBell.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(SetBell.this);
                final String str[] = {"전화벨", "Kill Bill", "Magic Mamaliga", "Marry You", "Minions"};

                builder.setTitle("알림벨 종류 선택").setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //선택완료 버튼 누르게 되면 토스트 출력
                        stopMusic();
                        Toast.makeText(getApplicationContext(), str[temp] + "선택됨", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setSingleChoiceItems(str, 0, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //Toast.makeText(getApplicationContext(), "음악이 들리지 않으면 볼륨을 확인하세요", Toast.LENGTH_SHORT).show();
                        Uri alert;
                        if(which == 0){
                            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            temp = 0;
                            playMusic(alert);
                        } else if(which == 1) {
                            alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.kill_bill);
                            temp = 1;
                            playMusic(alert);
                        } else if(which == 2) {
                            alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.magic_mamaliga);
                            temp = 2;
                            playMusic(alert);
                        } else if(which == 3) {
                            alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.marry_you);
                            temp = 3;
                            playMusic(alert);
                        } else if(which == 4) {
                            alert = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minions);
                            temp = 4;
                            playMusic(alert);
                        }
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener(){
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        if(keyCode == KeyEvent.KEYCODE_BACK){
                            stopMusic();
                        }
                        return false;
                    }
                });

                return builder.create();
        }
        return super.onCreateDialog(id);

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

    private void startAlarm(final MediaPlayer player) throws java.io.IOException, IllegalArgumentException, IllegalStateException{
        final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {   // 현재 Alarm 볼륨 구함
            player.setAudioStreamType(AudioManager.STREAM_ALARM);    // Alarm 볼륨 설정
            player.setLooping(false);    // 음악 반복 재생
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
