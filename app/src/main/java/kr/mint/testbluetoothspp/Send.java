package kr.mint.testbluetoothspp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tathink.brooch.brooch.R;

import java.io.IOException;

/**
 * Created by MSI on 2016-10-10.
 */

public class Send   extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

        ((Button)findViewById(R.id.SEND1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.SEND2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.SEND3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.SEND4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.SEND5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.SEND6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send event
                try {
                    BTService.writesSelect(6);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}