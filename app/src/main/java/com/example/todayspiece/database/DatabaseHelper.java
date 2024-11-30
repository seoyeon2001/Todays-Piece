package com.example.todayspiece.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 이름 및 버전
    private static final String DATABASE_NAME = "todayspiece.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 및 컬럼 이름
    public static final String TABLE_NAME = "CalendarEntries";
    public static final String COLUMN_DATE = "date"; // PK
    public static final String COLUMN_IMAGE = "image"; // 이미지
    public static final String COLUMN_TITLE = "title";  // 한줄의 제목
    public static final String COLUMN_DETAILS = "details"; // 일기

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

    // 테이블을 삭제한 후 다시 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // LocalDate를 String으로 변환하여 데이터베이스에 저장
    public static String convertLocalDateToString(LocalDate date) {
        // yyyy년 MM월 dd일 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return date.format(formatter);
    }

    // String을 LocalDate로 변환
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate convertStringToLocalDate(String dateString) {
        // yyyy년 MM월 dd일 형식에서 LocalDate로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return LocalDate.parse(dateString, formatter);
    }
}
