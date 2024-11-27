package com.example.todayspiece;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    ArrayList<LocalDate> dayList;

    // 생성자
    public CalendarAdapter(ArrayList<LocalDate> dayList) {
        this.dayList = dayList;
    }

    // 화면을 연결해주는 메서드
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    // 데이터를 연결해주는 메서드
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // day 변수에 날짜 담기
        LocalDate day = dayList.get(position);

        // 날짜 적용 - 없는 날은 빈칸
        if (day == null) {
            holder.dayText.setText("");
        } else {
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            // 현재 날짜의 일(모든 달의) 색 칠하기
            if (day.equals(CalendarUtil.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
        }

        // 텍스트 색상 지정
        if ( (position+1)%7 == 0) { // 토요일: 파랑
            holder.dayText.setTextColor(Color.BLUE);
        } else if ( position%7 == 0) { // 일요일: 빨강
            holder.dayText.setTextColor(Color.RED);
        }

        // 날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                int iYear = day.getYear();
                int iMonth = day.getMonthValue();
                int iDay = day.getDayOfMonth();

                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

                Toast.makeText(holder.itemView.getContext(), yearMonDay, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        // 초기화
        TextView dayText;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}
