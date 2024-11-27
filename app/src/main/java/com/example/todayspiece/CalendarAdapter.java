package com.example.todayspiece;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private final List<String> dates;
    private final Context context;
    private final int cellSize;

    public CalendarAdapter(Context context, List<String> dates, int cellSize) {
        this.context = context;
        this.dates = dates;
        this.cellSize = cellSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.date_cell, parent, false);

        // 셀 크기 설정
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = cellSize;
        layoutParams.height = cellSize;
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date = dates.get(position);
        holder.dateText.setText(date);

        // 배경 이미지 설정
        if (date.equals("13")) {
            holder.dateBackground.setImageResource(R.drawable.special_image); // 특정 날짜 이미지
        } else {
            holder.dateBackground.setImageResource(R.drawable.default_image); // 기본 이미지
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        ImageView dateBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            dateBackground = itemView.findViewById(R.id.date_background);
        }
    }
}
