package com.tathink.brooch.brooch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by MSI on 2016-08-16.
 */
public class IntroSubActivity extends Activity {
    Handler h;//핸들러 선언

    //image random 처리
    int index = (int)(Math.random() * 5);
    int res = ran[index];
    public static final int ran[] = {
            R.drawable.ad01, R.drawable.ad02, R.drawable.ad03, R.drawable.ad04, R.drawable.ad05
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //인트로화면이므로 타이틀바를 없앤다
        //setTheme(android.R.style.Theme_NoTitleBar);
        setContentView(R.layout.intro_sub);

        //random image View에 출력
        ImageView imageView = (ImageView)findViewById(R.id.AdimageView) ;
        imageView.setImageResource(res);

        h= new Handler(); //딜래이를 주기 위해 핸들러 생성
        h.postDelayed(mrun, 1000); // 딜레이 ( 런어블 객체는 mrun, 시간 2초)
    }

    Runnable mrun = new Runnable(){
        @Override
        public void run(){
            Intent i = new Intent(IntroSubActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
        }
    };
    //인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    //미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }

}
