package com.example.todayspiece;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
//            holder.dayText.setText("");
            holder.dayImageButton.setVisibility(View.INVISIBLE);  // 비어있는 날은 버튼 숨기기
        } else {
//            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            holder.dayImageButton.setVisibility(View.VISIBLE);  // 날짜가 있는 버튼만 보이기
//            holder.dayImageButton.setImageResource(getDayImageResource(day.getDayOfMonth())); // 날짜에 따라 이미지 설정
            holder.dayImageButton.setTag(day);  // 버튼에 날짜 데이터를 저장

            // 현재 날짜의 일(모든 달의) 색 칠하기
//            if (day.equals(CalendarUtil.selectedDate)) {
//                holder.parentView.setBackgroundColor(Color.LTGRAY);
//            }
            if (day.equals(CalendarUtil.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.parentView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // 텍스트 색상 지정
        if ( (position+1)%7 == 0) { // 토요일: 파랑
//            holder.dayText.setTextColor(Color.BLUE);
            holder.dayImageButton.setColorFilter(Color.BLUE);
        } else if ( position%7 == 0) { // 일요일: 빨강
//            holder.dayText.setTextColor(Color.RED);
            holder.dayImageButton.setColorFilter(Color.RED);
        } else { // 평일: 검정
            holder.dayImageButton.setColorFilter(Color.BLACK);
        }

        // 날짜 클릭 이벤트
        holder.dayImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                LocalDate clickedDay = (LocalDate) holder.dayImageButton.getTag();  // 클릭한 날짜 가져오기

                if (clickedDay != null) {
                    int iYear = clickedDay.getYear();
                    int iMonth = clickedDay.getMonthValue();
                    int iDay = clickedDay.getDayOfMonth();

                    String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";
                    Toast.makeText(holder.itemView.getContext(), yearMonDay + " 클릭!", Toast.LENGTH_SHORT).show();
                }
//                int iYear = day.getYear();
//                int iMonth = day.getMonthValue();
//                int iDay = day.getDayOfMonth();
//
//                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";
//
//                Toast.makeText(holder.itemView.getContext(), yearMonDay, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

//    // 날짜에 따라 이미지를 설정하는 메서드
//    private int getDayImageResource(int dayOfMonth) {
//        // 이곳에 원하는 이미지를 숫자에 맞게 리턴하도록 설정
//        switch (dayOfMonth) {
//            case 1:
//                return R.drawable.ic_calendar_1;
//            case 2:
//                return R.drawable.ic_calendar_2;
//            default:
//                return R.drawable.ic_calendar_day; // 기본 이미지
//        }
//    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        // 초기화
//        TextView dayText;
        ImageButton dayImageButton;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
//            dayText = itemView.findViewById(R.id.dayText);
            dayImageButton = itemView.findViewById(R.id.dayImageButton);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}
