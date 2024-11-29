package com.example.todayspiece.database;

import android.graphics.Bitmap;

import java.time.LocalDate;

public class CalendarEntry {
    private LocalDate date;
    private Bitmap image;
    private String title;
    private String details;

    public CalendarEntry(LocalDate date, Bitmap image, String title, String details) {
        this.date = date;
        this.image = image;
        this.title = title;
        this.details = details;
    }

    // Getter and Setter methods
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
