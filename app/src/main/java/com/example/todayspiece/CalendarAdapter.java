package com.example.todayspiece;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

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
        LocalDate day = dayList.get(position); // 현재 위치의 날짜 가져오기

        // 날짜가 없는 경우 처리
        if (day == null) {
            holder.dayImageButton.setVisibility(View.INVISIBLE); // 버튼 숨기기
        } else {
            holder.dayImageButton.setVisibility(View.VISIBLE); // 버튼 보이기
            holder.dayImageButton.setTag(day); // 버튼에 날짜 데이터 저장

            // 선택된 날짜 배경 색 설정
            if (day.equals(CalendarUtil.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.parentView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // 텍스트 색상 설정 (요일별)
        if ((position + 1) % 7 == 0) { // 토요일: 파랑
            holder.dayImageButton.setColorFilter(Color.BLUE);
        } else if (position % 7 == 0) { // 일요일: 빨강
            holder.dayImageButton.setColorFilter(Color.RED);
        } else { // 평일: 검정
            holder.dayImageButton.setColorFilter(Color.BLACK);
        }

        // 버튼 클릭 이벤트 설정
        holder.dayImageButton.setOnClickListener(v -> {
            LocalDate clickedDay = (LocalDate) holder.dayImageButton.getTag(); // 클릭한 날짜 가져오기

            if (clickedDay != null) {
                // 클릭한 날짜 정보 가져오기
                int iYear = clickedDay.getYear();
                int iMonth = clickedDay.getMonthValue();
                int iDay = clickedDay.getDayOfMonth();

                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

                // SecondActivity로 이동
                Intent intent = new Intent(context, SecondActivity.class); // 전달받은 Context 사용
                intent.putExtra("selectedDate", yearMonDay); // 선택된 날짜를 전달
                context.startActivity(intent); // Activity 시작
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size(); // 전체 날짜 수 반환
    }

    // ViewHolder 클래스
    class CalendarViewHolder extends RecyclerView.ViewHolder {
        ImageButton dayImageButton;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayImageButton = itemView.findViewById(R.id.dayImageButton); // 버튼 초기화
            parentView = itemView.findViewById(R.id.parentView); // 부모 뷰 초기화
        }
    }
}
