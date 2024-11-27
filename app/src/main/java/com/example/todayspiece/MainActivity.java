package com.example.todayspiece;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendarView = findViewById(R.id.calendarView);

        // 날짜 선택 이벤트 처리
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = year + "/" + (month + 1) + "/" + dayOfMonth;
            Toast.makeText(MainActivity.this, "Selected date: " + date, Toast.LENGTH_SHORT).show();
        });
    }
}
