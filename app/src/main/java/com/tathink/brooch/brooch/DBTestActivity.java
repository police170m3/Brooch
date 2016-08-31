package com.tathink.brooch.brooch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kr.mint.testbluetoothspp.DBManager;

/**
 * Created by MSI on 2016-08-31.
 */
public class DBTestActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbtestactivity);
        final DBManager dbManager = new DBManager(getApplicationContext(), "STRESS.db", null, 1);

        // DB에 저장 될 속성을 입력받는다
        final EditText etName = (EditText) findViewById(R.id.date);
        final EditText etPrice = (EditText) findViewById(R.id.dB);

        // 쿼리 결과 입력
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);

        // Insert
        Button btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert into 테이블명 values (값, 값, 값...);
                String date = etName.getText().toString();
                String dB = etPrice.getText().toString();
                dbManager.insert("insert into STRESS_INFO values(null, '" + date + "', " + dB + ");");  //text는 '홍길동'  int는 500

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Update
        Button btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
                String date = etName.getText().toString();
                String dB = etPrice.getText().toString();
                dbManager.update("update STRESS_INFO set dB = " + dB + " where date = '" + date + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Delete
        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String date = etName.getText().toString();
                dbManager.delete("delete from STRESS_INFO where date = '" + date + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Select
        Button btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Del all
        Button btnDelAll = (Button) findViewById(R.id.btn_DelAll);
        btnDelAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String date = etName.getText().toString();
                dbManager.delete("delete from STRESS_INFO;");

                tvResult.setText( dbManager.PrintData() );
            }
        });
    }
}