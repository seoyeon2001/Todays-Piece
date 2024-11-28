package com.example.todayspiece.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;


public class DatabaseManager {
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        // 데이터베이스 초기화
        dbHelper = new DatabaseHelper(context);
    }

    // 데이터 삽입
    public void insertEntry(String date, Bitmap image, String title, String details) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_DATE, date);

            // 이미지(Bitmap -> BLOB 변환)
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageData = stream.toByteArray();
                values.put(DatabaseHelper.COLUMN_IMAGE, imageData);
            }

            values.put(DatabaseHelper.COLUMN_TITLE, title);
            values.put(DatabaseHelper.COLUMN_DETAILS, details);

            // 중복된 날짜가 있으면 기존 데이터 덮어쓰기
            db.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 저장된 데이터를 조회해 캘린더와 연동 (특정 날짜의 데이터 조회)
    public CalendarEntry getEntry(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_IMAGE,
                        DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DETAILS},
                DatabaseHelper.COLUMN_DATE + " = ?",
                new String[]{date}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
            int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE);
            int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
            int detailsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DETAILS);

            if (dateIndex != -1 && imageIndex != -1 && titleIndex != -1 && detailsIndex != -1) {
                String retrievedDate = cursor.getString(dateIndex);

                byte[] imageBytes = cursor.getBlob(imageIndex);
                Bitmap image = null;
                if (imageBytes != null) {
                    image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }

                String title = cursor.getString(titleIndex);
                String details = cursor.getString(detailsIndex);

                cursor.close();
                return new CalendarEntry(retrievedDate, image, title, details);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }




    // 데이터 업데이트
    public void updateEntry(String date, Bitmap image, String title, String details) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IMAGE, imageToBytes(image)); // Bitmap -> BLOB 변환
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DETAILS, details);

        String whereClause = DatabaseHelper.COLUMN_DATE + " = ?";
        String[] whereArgs = {date};

        db.update(DatabaseHelper.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    // Bitmap -> byte[] 변환 유틸리티 메서드
    private byte[] imageToBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // 데이터 삭제
    public void deleteEntry(String date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.COLUMN_DATE + " = ?";
        String[] whereArgs = {date};

        db.delete(DatabaseHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}
