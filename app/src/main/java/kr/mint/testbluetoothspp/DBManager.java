package kr.mint.testbluetoothspp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MSI on 2016-08-29.
 */
public class DBManager  extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE STRESS_INFO (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, signal INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from STRESS_INFO", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : date "
                    + cursor.getString(1)
                    + ", signal = "
                    + cursor.getInt(2)
                    + "\n";
        }

        return str;
    }

    public int SelectStress24h(int i, int hours){    //최근 24시간동안 db 횟수 체크
        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;
        int from = ((-1) * (i) + hours) + 9;    //hours: 현재, 1일전, 2일전 구분용도
        int to = ((-1) * (i + 1) + hours) + 9;  //+9는 국제시간을 한국시간으로 보정

        Cursor cursor = db.rawQuery("select count(*) from STRESS_INFO where date <= datetime('now', '" + from + " hours') and date >= datetime('now', '" + to + " hours');", null);
        while (cursor.moveToNext()) {
            cnt = cursor.getInt(0);
        }

        return cnt;
    }

    public int SelectStress1week(int i, int days){    //최근 1주일 동안 db 횟수 체크
        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;
        int from = (i) + (days);
        int to = (i - 1) + (days);

        Cursor cursor = db.rawQuery("select count(*) from STRESS_INFO where date <= datetime('now', '" + from + " days') and date >= datetime('now', '" + to + " days');", null);
        while (cursor.moveToNext()) {
            cnt = cursor.getInt(0);
        }

        return cnt;
    }

}