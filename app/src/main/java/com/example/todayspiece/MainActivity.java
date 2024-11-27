package com.example.todayspiece;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView calendarRecyclerView = findViewById(R.id.calendarRecyclerView);

        // 화면 크기를 기준으로 셀 크기 계산
        int numberOfColumns = 7; // 열 개수
        int cellSize = calculateCellSize(numberOfColumns);

        // GridLayoutManager 설정
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        calendarRecyclerView.setLayoutManager(layoutManager);

        // 날짜 데이터 생성
        List<String> dates = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            dates.add(String.valueOf(i));
        }

        // 어댑터 설정
        CalendarAdapter adapter = new CalendarAdapter(this, dates, cellSize);
        calendarRecyclerView.setAdapter(adapter);
    }

    // 셀 크기를 화면 너비에 맞게 계산
    private int calculateCellSize(int numberOfColumns) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels; // 화면 너비
        int totalSpacing = 4 * (numberOfColumns - 1); // 4dp를 직접 사용
        return (screenWidth - totalSpacing) / numberOfColumns; // 셀 크기 계산
    }

    // dp 값을 px로 변환
    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }
}
