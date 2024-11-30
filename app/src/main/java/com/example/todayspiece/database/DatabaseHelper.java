package com.example.todayspiece.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todayspiece.db"; // 데이터베이스 이름
    private static final int DATABASE_VERSION = 1; // 데이터베이스 버전

    // 테이블 및 컬럼 이름
    public static final String TABLE_NAME = "CalendarEntries";
    public static final String COLUMN_DATE = "date"; // 날짜 (PK)
    public static final String COLUMN_IMAGE = "image"; // 이미지
    public static final String COLUMN_TITLE = "title"; // 제목
    public static final String COLUMN_DETAILS = "details"; // 내용

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
        // 테이블 생성
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 갱신
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // LocalDate를 String으로 변환 (yyyy년 MM월 dd일)
    public static String convertLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return date.format(formatter);
    }

    // String을 LocalDate로 변환 (yyyy년 MM월 dd일)
    public static LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return LocalDate.parse(dateString, formatter);
    }
}
