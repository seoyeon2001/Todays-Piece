// 최종적으로는 삭제될 파일입니다. - 샘플
package com.example.todayspiece.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleData {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<CalendarEntry> generateSampleData(Context context) {
        List<CalendarEntry> entries = new ArrayList<>();

        // 임의의 날짜 생성
        LocalDate date1 = LocalDate.of(2024, 11, 25);
        LocalDate date2 = LocalDate.of(2024, 11, 26);
        LocalDate date3 = LocalDate.of(2024, 11, 27);

        // 임의의 제목과 세부사항
        String title1 = "첫 번째 일기";
        String details1 = "첫 번째 일기 입니다.";

        String title2 = "두 번째 일기";
        String details2 = "졸리다.";

        String title3 = "세 번째 일기";
        String details3 = "카공가서 따뜻한 라떼 시켰다.";

        // 각 날짜에 대해 이미지를 저장하고 경로 반환
        String imagePath1 = saveImageToInternalStorage(context, "image_20241125", createSampleBitmap(0xFFFF0000));
        String imagePath2 = saveImageToInternalStorage(context, "image_20241126", createSampleBitmap(0xFF00FF00));
        String imagePath3 = saveImageToInternalStorage(context, "image_20241127", createSampleBitmap(0xFF0000FF));

        // 각 날짜에 대해 CalendarEntry 객체 생성
        CalendarEntry entry1 = new CalendarEntry(date1, loadImageFromInternalStorage(imagePath1), title1, details1);
        CalendarEntry entry2 = new CalendarEntry(date2, loadImageFromInternalStorage(imagePath2), title2, details2);
        CalendarEntry entry3 = new CalendarEntry(date3, loadImageFromInternalStorage(imagePath3), title3, details3);

        // /data/user/0/com.example.todayspiece/files/image_20241125.png
        Log.d("ImagePath", "Saved Image Path: " + imagePath1);

        // 리스트에 추가
        entries.add(entry1);
        entries.add(entry2);
        entries.add(entry3);

        return entries;
    }

    // 샘플 이미지를 생성하는 메서드 (색상으로 간단하게 만들어봄)
    private static Bitmap createSampleBitmap(int color) {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(color);
        return bitmap;
    }

    // 내부 저장소에 이미지를 저장하고 파일 경로를 반환하는 메서드
    private static String saveImageToInternalStorage(Context context, String imageName, Bitmap image) {
        File directory = context.getFilesDir();  // 내부 저장소 경로
        File imageFile = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return imageFile.getAbsolutePath();  // 저장된 이미지 파일 경로 반환
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 내부 저장소에서 이미지를 불러오는 메서드
    private static Bitmap loadImageFromInternalStorage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return null;

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imagePath);
        }
        return null;
    }
}
