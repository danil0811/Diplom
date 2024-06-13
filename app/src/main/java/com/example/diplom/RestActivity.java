package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diplom.Models.Exercise;
import java.util.ArrayList;

public class RestActivity extends AppCompatActivity {

    private TextView tvNextExercise;
    private TextView tvTimer;
    private Button btnSkipRest;
    private ArrayList<Exercise> exerciseList;
    private int currentExerciseIndex;
    private int totalCaloriesBurned;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        tvNextExercise = findViewById(R.id.tvNextExercise);
        tvTimer = findViewById(R.id.tvTimer);
        btnSkipRest = findViewById(R.id.btnSkipRest);

        // Получаем список упражнений и текущий индекс упражнения из Intent
        exerciseList = (ArrayList<Exercise>) getIntent().getSerializableExtra("exerciseList");
        currentExerciseIndex = getIntent().getIntExtra("currentExerciseIndex", 0);
        totalCaloriesBurned = getIntent().getIntExtra("totalCaloriesBurned", 0);

        // Устанавливаем название следующего упражнения
        if (currentExerciseIndex + 1 < exerciseList.size()) {
            tvNextExercise.setText("Следующее упражнение: " + exerciseList.get(currentExerciseIndex + 1).getName());
        } else {
            tvNextExercise.setText("Все упражнения завершены");
            startEndActivity();
        }

        // Запускаем таймер на 30 секунд
        startTimer();

        btnSkipRest.setOnClickListener(v -> {
            stopTimer();
            startNextExercise();
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Отдых: " + millisUntilFinished / 1000 + " секунд");
            }

            public void onFinish() {
                startNextExercise();
            }
        }.start();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startNextExercise() {
        if (currentExerciseIndex + 1 < exerciseList.size()) {
            int nextExerciseIndex = currentExerciseIndex + 1;
            Exercise nextExercise = exerciseList.get(nextExerciseIndex);

            Intent intent;
            switch (nextExercise.getName()) {
                case "Отжимания от пола":
                    intent = new Intent(RestActivity.this, PushUp.class);
                    break;
                case "Пресс":
                    intent = new Intent(RestActivity.this, PressExercise.class);
                    break;
                case "Отжимание с широким упором":
                    intent = new Intent(RestActivity.this, PushupWithAWideFocus.class);
                    break;
                case "Растяжка ромбовидных мышц":
                    intent = new Intent(RestActivity.this, StretchingTheRhomboidMusclesExercise.class);
                    break;
                case "Отжимание с упором впереди":
                    intent = new Intent(RestActivity.this, PushupWithEmphasis.class);
                    break;
                default:
                    intent = new Intent(RestActivity.this, JumpingWithArmsAndLegs.class);
                    break;
            }
            intent.putExtra("exerciseList", exerciseList);
            intent.putExtra("currentExerciseIndex", nextExerciseIndex);
            intent.putExtra("totalCaloriesBurned", totalCaloriesBurned);
            startActivity(intent);
            finish();
        } else {
            // Все упражнения завершены
            startEndActivity();
        }
    }

    private void startEndActivity() {
        Intent intent = new Intent(RestActivity.this, All_body7x4.class);
        intent.putExtra("totalCaloriesBurned", totalCaloriesBurned);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer(); // Останавливаем таймер, если активность уничтожается
    }
}

