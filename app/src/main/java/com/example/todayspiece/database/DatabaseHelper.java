package com.example.todayspiece.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 이름 및 버전
    private static final String DATABASE_NAME = "todayspiece.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 및 컬럼 이름
    public static final String TABLE_NAME = "CalendarEntries";
    public static final String COLUMN_DATE = "date"; // PK
    public static final String COLUMN_IMAGE = "image"; // 이미지
    public static final String COLUMN_TITLE = "title";  // 한줄의 제목
    public static final String COLUMN_DETAILS = "schedule_details"; // 일정

    // 테이블 생성 SQL
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_DATE + " TEXT PRIMARY KEY, " +
                    COLUMN_IMAGE + " BLOB, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DETAILS + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}