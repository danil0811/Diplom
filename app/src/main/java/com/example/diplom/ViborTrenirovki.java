package com.example.diplom;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ViborTrenirovki extends AppCompatActivity {

    private static final String PREFS_NAME = "WorkoutPrefs";
    private static final String KEY_COMPLETED_WORKOUTS = "completedWorkoutsCount";
    private static final String KEY_WORKOUT_DURATION = "workoutDurationMinutes";
    private static final String KEY_CALORIES_BURNED = "totalCaloriesBurned";

    private SharedPreferences sharedPreferences;

    private TextView countCcalTextView;
    private TextView countTrenTextView;
    private TextView countMinTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibor_trenirovki);

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Инициализация текстовых полей
        countCcalTextView = findViewById(R.id.CountCcal);
        countTrenTextView = findViewById(R.id.CountTren);
        countMinTextView = findViewById(R.id.CountMin);

        LinearLayout allBodyLayout = findViewById(R.id.AllBody);
        Button btnAllBody = findViewById(R.id.BtnAllBody);

        LinearLayout lowerBodyLayout = findViewById(R.id.LowerBody);
        Button btnLowerBody = findViewById(R.id.BtnLowerBody);

        LinearLayout programmaPress = findViewById(R.id.Press);
        Button btnPress = findViewById(R.id.BtnPress);

        LinearLayout programmaChest = findViewById(R.id.Chest);
        Button btnChest = findViewById(R.id.BtnChest);

        LinearLayout programmaHand = findViewById(R.id.Hand);
        Button btnHand = findViewById(R.id.BtnHand);

        int completedWorkoutsCount = sharedPreferences.getInt(KEY_COMPLETED_WORKOUTS, 0);
        long workoutDurationMinutes = sharedPreferences.getLong(KEY_WORKOUT_DURATION, 0);
        int totalCaloriesBurned = sharedPreferences.getInt(KEY_CALORIES_BURNED, 0);

        countTrenTextView.setText("Потраченное время на тренировке:"+completedWorkoutsCount);
        countMinTextView.setText("Потраченное время на тренировке:" + workoutDurationMinutes);
        countCcalTextView.setText("Сожженные калории: " + totalCaloriesBurned);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("exercises").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    int totalCalories = 0;
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String caloriesStr = document.getString("calories");
                        if (caloriesStr != null) {
                            int calories = Integer.parseInt(caloriesStr);
                            totalCalories += calories;
                        }
                    }
                    // Обновляем TextView с полученными данными о калориях
                    countCcalTextView.setText("Сожженные калории: " + totalCalories);
                }
            } else {
                Log.d(TAG, "Ошибка при получении документов: ", task.getException());
            }
        });

        updateWorkoutData(1, 200);

        // Установите действия для кнопок и лэйаутов
        View.OnClickListener allBodyClickListener = v -> openActivity(All_body7x4.class);
        allBodyLayout.setOnClickListener(allBodyClickListener);
        btnAllBody.setOnClickListener(allBodyClickListener);

//        View.OnClickListener lowerBodyClickListener = v -> openActivity(Lower.class);
//        lowerBodyLayout.setOnClickListener(lowerBodyClickListener);
//        btnLowerBody.setOnClickListener(lowerBodyClickListener);
//
//        View.OnClickListener pressClickListener = v -> openActivity(PressActivity.class);
//        programmaPress.setOnClickListener(pressClickListener);
//        btnPress.setOnClickListener(pressClickListener);
//
//        View.OnClickListener chestClickListener = v -> openActivity();
//        programmaChest.setOnClickListener(chestClickListener);
//        btnChest.setOnClickListener(chestClickListener);
//
//        View.OnClickListener handClickListener = v -> openActivity(HandActivity.class);
//        programmaHand.setOnClickListener(handClickListener);
//        btnHand.setOnClickListener(handClickListener);
    }

    private void updateWorkoutData(long workoutDurationMinutes, int caloriesBurned) {
        float completedWorkoutsCount = sharedPreferences.getInt(KEY_COMPLETED_WORKOUTS, 0) + 1;
        long totalWorkoutDuration = sharedPreferences.getLong(KEY_WORKOUT_DURATION, 0) + workoutDurationMinutes;
        int totalCaloriesBurned = sharedPreferences.getInt(KEY_CALORIES_BURNED, 0) + caloriesBurned;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_COMPLETED_WORKOUTS, (int) completedWorkoutsCount);
        editor.putLong(KEY_WORKOUT_DURATION, totalWorkoutDuration);
        editor.putInt(KEY_CALORIES_BURNED, totalCaloriesBurned);
        editor.apply();

        // Обновление TextView
        countTrenTextView.setText("Потраченное время на тренировке:" + completedWorkoutsCount);
        countMinTextView.setText("Потраченное время на тренировке:" + workoutDurationMinutes);
        countCcalTextView.setText("Сожженные калории: " + totalCaloriesBurned);
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}

