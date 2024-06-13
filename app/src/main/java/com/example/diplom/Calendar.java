package com.example.diplom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Calendar extends AppCompatActivity {
    private static final String PREFS_NAME = "TrainingPrefs";
    private static final String KEY_COMPLETED_DATES = "completedDates";

    private CalendarView calendarView;
    private Set<String> completedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        // Загружаем выполненные даты из SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        completedDates = prefs.getStringSet(KEY_COMPLETED_DATES, new HashSet<>());

        // Отмечаем выполненные даты на календаре
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            if (completedDates.contains(selectedDate)) {
                completedDates.remove(selectedDate);
            } else {
                completedDates.add(selectedDate);
            }

            // Сохраняем изменения в SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(KEY_COMPLETED_DATES, completedDates);
            editor.apply();
        });
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new java.util.Date(year - 2024, month, dayOfMonth));
    }
}
