package com.example.todayspiece;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseManager;
import com.example.todayspiece.database.SampleData;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView monthYearText;
    RecyclerView recyclerView;
    private DatabaseManager databaseManager; // 최종적으로 삭제할 코드입니다.

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        monthYearText = findViewById(R.id.monthYearText);
        ImageButton preBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.recyclerView);

        // DatabaseManager 초기화 - 최종적으로 삭제할 코드입니다.
        databaseManager = new DatabaseManager(this);

        // 샘플 데이터 생성 - 최종적으로 삭제할 코드입니다.
        List<CalendarEntry> sampleData = SampleData.generateSampleData(this);

        // 샘플 데이터를 데이터베이스에 삽입 - 최종적으로 삭제할 코드 입니다.
        for (CalendarEntry entry : sampleData) {
            databaseManager.insertEntry(entry.getDate(), entry.getImage(), entry.getTitle(), entry.getDetails());
        }

        // 현재 날짜
        CalendarUtil.selectedDate = LocalDate.now();

        // 화면 설정
        setMonthView();

        // 이전 달 버튼 클릭 이벤트
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        // 다음 달 버튼 클릭 이벤트
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthView();
            }
        });
    }

    // 날짜 보여주는 포맷 설정 - 11월 2024
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 yyyy");
        return date.format(formatter);
    }

    // 날짜 보여주는 포맷 설정 - 2024년 11월
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String yearMonthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월");
        return date.format(formatter);
    }

    // 화면 설정
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        // 년월 텍스트뷰 설정
        monthYearText.setText(monthYearFromDate(CalendarUtil.selectedDate));

        // 일
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);

        CalendarAdapter adapter = new CalendarAdapter(this, dayList);

        // 레이아웃 설정
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

        // 레이아웃 적용
        recyclerView.setLayoutManager(manager);

        // 어뎁터 적용
        recyclerView.setAdapter(adapter);
    }

    // 날짜 생성
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> dayList = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);

        // 해당 월의 마지막 날 가져오기(28, 30, 31)
        int lastDay = yearMonth.lengthOfMonth();

        // 해당 월의 첫 날 가져오기
        LocalDate firstDay = CalendarUtil.selectedDate.withDayOfMonth(1);

        // 첫 날 요일 가져오기(월:1 ~ 일:7)
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        for(int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > lastDay+dayOfWeek) {
                dayList.add(null);
            } else {
                dayList.add(LocalDate.of(CalendarUtil.selectedDate.getYear(), CalendarUtil.selectedDate.getMonth(), i-dayOfWeek));
            }
        }
        return dayList;
    }

//    // 클릭하면 Toast - 날짜 어뎁터에서 넘긴 데이터를 받는 메서드
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void onItemClick(String dayText) {
//        String yearMonDay = yearMonthFromDate(CalendarUtil.selectedDate) + " " + dayText + "일";
//        Toast.makeText(this, yearMonDay, Toast.LENGTH_SHORT).show();
//    }
}