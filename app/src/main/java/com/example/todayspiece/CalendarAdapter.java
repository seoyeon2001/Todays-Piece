package com.example.todayspiece;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private DatabaseManager databaseManager;
    private ArrayList<LocalDate> dayList; // 날짜 리스트
    private Context context; // Context 멤버 변수 추가

    // 생성자
    public CalendarAdapter(Context context, ArrayList<LocalDate> dayList) {
        this.context = context; // Context 저장
        this.dayList = dayList; // 날짜 리스트 저장
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    // 데이터와 View를 연결
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate day = dayList.get(position);

        // view처리
        // 날짜가 없는 경우 - 일자 버튼, 이미지 버튼 둘다 숨김
        if (day == null) {
            holder.dayButton.setText("");
            holder.dayImageButton.setVisibility(View.GONE);
        } else { // 기본적으로 일자 버튼 표시(이미지 버튼 숨김)
            holder.dayButton.setText(String.valueOf(day.getDayOfMonth()));
            holder.dayButton.setVisibility(View.VISIBLE);
            holder.dayImageButton.setVisibility(View.GONE);

            // 일기 작성 여부를 체크
            boolean isDiaryWritten = checkDiaryWrittenForDay(day, holder);

            // 일기를 작성했다면 이미지 버튼이 표시되어야 함
            if (isDiaryWritten) {
                holder.dayButton.setVisibility(View.GONE);
                holder.dayImageButton.setVisibility(View.VISIBLE);
            } else {
                holder.dayButton.setVisibility(View.VISIBLE);
                holder.dayImageButton.setVisibility(View.GONE);
            }

//            // 선택된 날짜 배경 색 설정
//            if (day.equals(CalendarUtil.selectedDate)) {
//                holder.parentView.setBackgroundColor(Color.LTGRAY);
//            } else {
//                holder.parentView.setBackgroundColor(Color.TRANSPARENT);
//            }
        }

        // 일자 버튼 텍스트 색상 설정 (요일별)
        if ((position + 1) % 7 == 0) { // 토요일: 파랑
            holder.dayButton.setTextColor(Color.BLUE);
        } else if (position % 7 == 0) { // 일요일: 빨강
            holder.dayButton.setTextColor(Color.RED);
        } else { // 평일: 검정
            holder.dayButton.setTextColor(Color.BLACK);
        }

        // 날짜 버튼 클릭 이벤트 설정
        holder.dayButton.setOnClickListener(v -> openSecondActivity(day));

        // 이미지 버튼 클릭 이벤트 설정 (일기가 있는 경우)
        holder.dayImageButton.setOnClickListener(v -> openSecondActivity(day));
    }

    // 다음 페이지로 이동
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openSecondActivity(LocalDate day) {
        if (day != null) {
            int iYear = day.getYear();
            int iMonth = day.getMonthValue();
            int iDay = day.getDayOfMonth();
            String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

            Intent intent = new Intent(context, SecondActivity.class);
            intent.putExtra("selectedDate", yearMonDay); // 선택된 날짜를 전달
            context.startActivity(intent);
        }
    }

    // 일기 작성 여부 확인
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkDiaryWrittenForDay(LocalDate day, CalendarViewHolder holder) {

        // DatabaseManager 초기화
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }

        // 선택된 날짜에 대한 데이터 조회
        CalendarEntry entry = databaseManager.getEntry(day);

        if (entry != null) {
            Log.d("CalendarEntry", "Date: " + entry.getDate());
            Log.d("CalendarEntry", "Title: " + entry.getTitle());
            Log.d("CalendarEntry", "Details: " + entry.getDetails());

            if (entry.getImage() != null) {
                Log.d("CalendarEntry", "Image: Image is available");

                // 이미지를 ImageButton에 설정
                holder.dayImageButton.setImageBitmap(entry.getImage());
            } else {
                Log.d("CalendarEntry", "Image: No image available");
            }
        }

        // entry가 null이 아니면 일기가 있는 것으로 간주
        return entry != null;
    }

    // 전체 날짜 수 반환
    @Override
    public int getItemCount() {
        return dayList.size();
    }

    // ViewHolder 클래스
    class CalendarViewHolder extends RecyclerView.ViewHolder {
        Button dayButton;
        ImageButton dayImageButton;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayButton = itemView.findViewById(R.id.dayButton);
            dayImageButton = itemView.findViewById(R.id.dayImageButton);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}
