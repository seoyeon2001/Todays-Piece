package com.example.todayspiece.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;


public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;
        // 데이터베이스 초기화
        dbHelper = new DatabaseHelper(context);
    }

    // 데이터 삽입
    public void insertEntry(LocalDate date, Bitmap image, String title, String details) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            // LocalDate를 String으로 변환하여 저장
            String dateString = DatabaseHelper.convertLocalDateToString(date);
            values.put(DatabaseHelper.COLUMN_DATE, dateString);

            // 이미지를 내부 저장소에 저장하고 경로를 데이터베이스에 저장
            if (image != null) {
                String imagePath = saveImageToInternalStorage(dateString, image);
                values.put(DatabaseHelper.COLUMN_IMAGE, imagePath);
            }

            values.put(DatabaseHelper.COLUMN_TITLE, title);
            values.put(DatabaseHelper.COLUMN_DETAILS, details);

            // 중복된 날짜가 있으면 기존 데이터 덮어쓰기
            db.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 저장 메서드 (내부 저장)
    private String saveImageToInternalStorage(String imageName, Bitmap image) {
        File directory = context.getFilesDir(); // 내부 저장소 경로
        File imageFile = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return imageFile.getAbsolutePath(); // 저장된 이미지 파일 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 저장된 데이터를 조회해 캘린더와 연동 (특정 날짜의 데이터 조회)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public CalendarEntry getEntry(LocalDate date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String dateString = DatabaseHelper.convertLocalDateToString(date);

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_IMAGE,
                        DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_DETAILS},
                DatabaseHelper.COLUMN_DATE + " = ?",
                new String[]{dateString}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String retrievedDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
            String details = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DETAILS));

            Bitmap image = loadImageFromInternalStorage(imagePath);

            cursor.close();

            // 날짜를 String에서 LocalDate로 변환하여 반환
            return new CalendarEntry(DatabaseHelper.convertStringToLocalDate(retrievedDate), image, title, details);
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // 이미지 불러오기 메서드
    private Bitmap loadImageFromInternalStorage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return null;

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imagePath);
        }
        return null;
    }

    // 데이터 업데이트
    public void updateEntry(LocalDate date, Bitmap image, String title, String details) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_DATE, DatabaseHelper.convertLocalDateToString(date)); // LocalDate -> String 변환
//        values.put(DatabaseHelper.COLUMN_IMAGE, imageToBytes(image)); // Bitmap -> BLOB 변환
//        values.put(DatabaseHelper.COLUMN_TITLE, title);
//        values.put(DatabaseHelper.COLUMN_DETAILS, details);
//
//        String whereClause = DatabaseHelper.COLUMN_DATE + " = ?";
//        String[] whereArgs = {DatabaseHelper.convertLocalDateToString(date)};

        String dateString = DatabaseHelper.convertLocalDateToString(date);
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DETAILS, details);

        // 이미지가 있는 경우 내부 저장소에 업데이트
        if (image != null) {
            String imagePath = saveImageToInternalStorage(dateString, image);
            values.put(DatabaseHelper.COLUMN_IMAGE, imagePath);
        }

        String whereClause = DatabaseHelper.COLUMN_DATE + " = ?";
        String[] whereArgs = {dateString};

        db.update(DatabaseHelper.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    // Bitmap -> byte[] 변환 유틸리티 메서드
    private byte[] imageToBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // 이미지 삭제 메서드
    private void deleteImageFromInternalStorage(String imageName) {
        File directory = context.getFilesDir();
        File imageFile = new File(directory, imageName + ".png");

        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (!deleted) {
                Log.d("DatabaseManager", "Failed to delete image: " + imageFile.getAbsolutePath());
            }
        }
    }

    // 데이터 삭제
    public void deleteEntry(LocalDate date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = DatabaseHelper.COLUMN_DATE + " = ?";
        String[] whereArgs = {DatabaseHelper.convertLocalDateToString(date)};

        // 이미지도 함께 삭제
        deleteImageFromInternalStorage(DatabaseHelper.convertLocalDateToString(date));

        db.delete(DatabaseHelper.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}
