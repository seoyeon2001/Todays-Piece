package com.example.todayspiece;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todayspiece.database.CalendarEntry;
import com.example.todayspiece.database.DatabaseHelper;
import com.example.todayspiece.database.DatabaseManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private DatabaseManager databaseManager;
    private ArrayList<LocalDate> dayList;
    private Context context;

    public CalendarAdapter(Context context, ArrayList<LocalDate> dayList) {
        this.context = context;
        this.dayList = dayList;
        this.databaseManager = new DatabaseManager(context);
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        LocalDate day = dayList.get(position);

        if (day == null) {
            holder.dayButton.setText("");
            holder.dayImageButton.setVisibility(View.GONE);
        } else {
            holder.dayButton.setText(String.valueOf(day.getDayOfMonth()));
            holder.dayButton.setVisibility(View.VISIBLE);
            holder.dayImageButton.setVisibility(View.GONE);

            CalendarEntry entry = databaseManager.getEntry(day);
            if (entry != null && entry.getImage() != null) {
                holder.dayButton.setVisibility(View.GONE);
                holder.dayImageButton.setVisibility(View.VISIBLE);
                holder.dayImageButton.setImageBitmap(entry.getImage());
            } else {
                holder.dayButton.setVisibility(View.VISIBLE);
                holder.dayImageButton.setVisibility(View.GONE);
            }
        }

        if ((position + 1) % 7 == 0) {
            holder.dayButton.setTextColor(Color.BLUE);
        } else if (position % 7 == 0) {
            holder.dayButton.setTextColor(Color.RED);
        } else {
            holder.dayButton.setTextColor(Color.BLACK);
        }

        holder.dayButton.setOnClickListener(v -> openSecondActivity(day));
        holder.dayImageButton.setOnClickListener(v -> openSecondActivity(day));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openSecondActivity(LocalDate day) {
        if (day != null) {
            // DatabaseHelper를 사용하여 LocalDate를 문자열로 변환
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
