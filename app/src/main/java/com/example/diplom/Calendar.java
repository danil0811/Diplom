package com.example.diplom;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Calendar extends AppCompatActivity {
    private static final String PREFS_NAME = "WorkoutPrefs";
    private static final String KEY_COMPLETED_DATES = "completedDates";

    private CalendarView calendarView;
    private TextView tvTrainingInfo;
    private TextView tvBMIResult;
    private Set<String> completedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        tvTrainingInfo = findViewById(R.id.tvTrainingInfo);
        tvBMIResult = findViewById(R.id.tvBMIResult);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        completedDates = sharedPreferences.getStringSet(KEY_COMPLETED_DATES, new HashSet<>());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            if (completedDates.contains(selectedDate)) {
                tvTrainingInfo.setText("Тренировка на " + selectedDate);
                tvTrainingInfo.setVisibility(View.VISIBLE);
            } else {
                tvTrainingInfo.setText("Нет тренировок на " + selectedDate);
                tvTrainingInfo.setVisibility(View.VISIBLE);
            }
        });

        // Получение ИМТ из SharedPreferences
        SharedPreferences bmiPrefs = getSharedPreferences("BMI_Prefs", MODE_PRIVATE);
        if (bmiPrefs.contains("BMI_VALUE")) {
            float bmi = bmiPrefs.getFloat("BMI_VALUE", 0);
            displayBMIResult(bmi);
        }
    }

    private void displayBMIResult(float bmi) {
        String bmiResultText;
        int color;

        if (bmi < 18.5) {
            bmiResultText = "Недостаточный вес";
            color = Color.YELLOW;
        } else if (bmi >= 18.5 && bmi < 24.9) {
            bmiResultText = "Норма";
            color = Color.GREEN;
        } else if (bmi >= 25 && bmi < 29.9) {
            bmiResultText = "Присутствует избыточный вес";
            color = Color.YELLOW;
        } else {
            bmiResultText = "Ожирение";
            color = Color.RED;
        }

        tvBMIResult.setText("Ваш Индекс массы тела: " + String.format(Locale.getDefault(), "%.1f", bmi) + " (" + bmiResultText + ")");
        tvBMIResult.setTextColor(color);
    }

    public static void addWorkoutDate(SharedPreferences sharedPreferences, String date) {
        Set<String> workoutDates = sharedPreferences.getStringSet(KEY_COMPLETED_DATES, new HashSet<>());
        workoutDates.add(date);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_COMPLETED_DATES, workoutDates);
        editor.apply();
    }
}
