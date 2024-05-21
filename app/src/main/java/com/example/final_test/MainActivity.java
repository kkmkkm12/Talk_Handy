package com.example.final_test;

import static com.example.final_test.MyDatabaseHelper.COLUMN_ID;
import static com.example.final_test.MyDatabaseHelper.COLUMN_NAME;
import static com.example.final_test.MyDatabaseHelper.COLUMN_PATH;
import static com.example.final_test.MyDatabaseHelper.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private static final String DB_NAME = "MyDB.db";
    private static final int DB_VERSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button study_btn = findViewById(R.id.study_btn);
        Button test_btn = findViewById(R.id.test_btn);
        Button wrongAns_btn = findViewById(R.id.wrongAns_btn);

        dbHelper = new MyDatabaseHelper(this, DB_NAME, null, DB_VERSION);

        Cursor cursor = readDB();

        if(!(cursor != null && cursor.getCount() > 0)){
            writeDB("ㄱ", "string1");
            writeDB("ㄴ", "string2");
            writeDB("ㄷ", "string3");
            writeDB("ㄹ", "string4");
            writeDB("ㅁ", "string5");
            writeDB("ㅂ", "string6");
            writeDB("ㅅ", "string7");
            writeDB("ㅇ", "string8");
            writeDB("ㅈ", "string9");
            writeDB("ㅊ", "string10");
            writeDB("ㅋ", "string11");
            writeDB("ㅌ", "string12");
            writeDB("ㅍ", "string13");
            writeDB("ㅎ", "string14");
            writeDB("ㅏ", "stringa1");
            writeDB("ㅑ", "stringa2");
            writeDB("ㅓ", "stringa3");
            writeDB("ㅕ", "stringa4");
            writeDB("ㅗ", "stringa5");
            writeDB("ㅛ", "stringa6");
            writeDB("ㅜ", "stringa7");
            writeDB("ㅠ", "stringa8");
            writeDB("ㅡ", "stringa9");
            writeDB("ㅣ", "stringa10");
            writeDB("ㅐ", "stringa11");
            writeDB("ㅒ", "stringa12");
            writeDB("ㅔ", "stringa13");
            writeDB("ㅖ", "stringa14");
            writeDB("ㅚ", "stringa15");
            writeDB("ㅟ", "stringa16");
            writeDB("ㅢ", "stringa17");
        }

        cursor.close();

        study_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainActivity.class);
                startActivity(intent);
            }
        });
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
        wrongAns_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WrongAnsActivity.class);
                startActivity(intent);
            }
        });
    }

    private Cursor readDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] from = {COLUMN_ID, COLUMN_NAME, COLUMN_PATH,};
        Cursor cursor = db.query(TABLE_NAME, from, null, null, null, null, COLUMN_ID + " ASC");
        //startManagingCursor(cursor);
        return cursor;
    }

    private void writeDB(String name, String path) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PATH, path);
        db.insertOrThrow(TABLE_NAME, null, values);
    }
}