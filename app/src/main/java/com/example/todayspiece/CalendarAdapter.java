package com.example.todayspiece;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseHelper;
import com.example.todayspiece.database.DatabaseManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final DatabaseManager databaseManager; // 데이터베이스 작업 관리
    private final ArrayList<LocalDate> dayList; // 달력의 날짜 리스트
    private final Context context; // Context 객체

    public CalendarAdapter(Context context, ArrayList<LocalDate> dayList) {
        this.context = context;
        this.dayList = dayList;
        this.databaseManager = new DatabaseManager(context); // 데이터베이스 매니저 초기화
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 달력 셀 레이아웃 설정
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate day = dayList.get(position);

        if (day == null) {
            // 공백 날짜 처리
            holder.dayButton.setText("");
            holder.dayImageButton.setVisibility(View.GONE);
        } else {
            // 날짜 버튼에 숫자 설정
            holder.dayButton.setText(String.valueOf(day.getDayOfMonth()));
            holder.dayButton.setVisibility(View.VISIBLE);
            holder.dayImageButton.setVisibility(View.GONE);

            // 해당 날짜에 저장된 데이터 확인
            CalendarEntry entry = databaseManager.getEntry(day);
            if (entry != null && entry.getImage() != null) {
                // 이미지가 있으면 버튼 대신 이미지 표시
                holder.dayButton.setVisibility(View.GONE);
                holder.dayImageButton.setVisibility(View.VISIBLE);
                holder.dayImageButton.setImageBitmap(entry.getImage());
            }
        }

        // 요일별 텍스트 색상 설정
        if ((position + 1) % 7 == 0) {
            holder.dayButton.setTextColor(Color.BLUE); // 일요일
        } else if (position % 7 == 0) {
            holder.dayButton.setTextColor(Color.RED); // 월요일
        } else {
            holder.dayButton.setTextColor(Color.BLACK); // 기타
        }

        // 클릭 시 세부 화면으로 이동
        holder.dayButton.setOnClickListener(v -> openSecondActivity(day));
        holder.dayImageButton.setOnClickListener(v -> openSecondActivity(day));
    }

    private void openSecondActivity(LocalDate day) {
        if (day != null) {
            // 날짜를 String으로 변환하여 Intent에 전달
            String yearMonDay = DatabaseHelper.convertLocalDateToString(day);
            Intent intent = new Intent(context, SecondActivity.class);
            intent.putExtra("selectedDate", yearMonDay);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    // ViewHolder 클래스: 셀 뷰 구성
    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        Button dayButton; // 날짜 표시 버튼
        ImageButton dayImageButton; // 이미지 표시 버튼

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayButton = itemView.findViewById(R.id.dayButton);
            dayImageButton = itemView.findViewById(R.id.dayImageButton);
        }
    }
}
