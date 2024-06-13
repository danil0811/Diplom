package com.example.diplom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.diplom.Models.Exercise;
import com.example.diplom.Models.ExerciseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tren_all_body_7x4 extends AppCompatActivity {
    private static final String PREFS_NAME = "TrainingPrefs";
    private static final String KEY_TRAININGS_COMPLETED = "trainingsCompleted";
    private TextView countTren;
    private TextView countMin;
    private long workoutStartTime;
    private int completedWorkoutsCount = 0; // Новый счетчик завершённых тренировок
    private ActivityResultLauncher<Intent> exerciseActivityResultLauncher;
    private ActivityResultLauncher<Intent> restActivityResultLauncher;
    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter adapter;
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "Tren_all_body_7x4";
    private int totalCaloriesBurned = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tren_all_body7x4);

        TextView LeftArrow = findViewById(R.id.leftArrow);
        Button StartProgramm = findViewById(R.id.StartProgramm);
        countTren = findViewById(R.id.CountTren); // Инициализация TextView для количества тренировок
        countMin = findViewById(R.id.CountMin); // Инициализация TextView для времени тренировки

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExerciseAdapter(new ArrayList<>());
        exerciseRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadExercisesFromFirestore();

        LeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllBody7x4();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadExercisesFromFirestore();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(exerciseRecyclerView);

        adapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
                showBottomSheetDialog(exercise);
            }
        });

        StartProgramm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFirstExercise();
            }
        });

        exerciseActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            handleActivityResult(data, 1); // 1 для упражнений
                        }
                    }
                }
        );

        restActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            handleActivityResult(data, 0); // 0 для отдыха
                        }
                    }
                }
        );
    }

    private void loadExercisesFromFirestore() {
        db.collection("exercises")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (task.isSuccessful()) {
                            List<Exercise> exercisesList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String description = document.getString("description");
                                String demonstration = document.getString("demonstration");
                                String setsReps = document.getString("setsReps");
                                Long caloriesLong = document.getLong("caloriesBurned");
                                int calories = (caloriesLong != null) ? caloriesLong.intValue() : 0;

                                Exercise exercise = new Exercise(name, description, demonstration, setsReps, calories);
                                exercisesList.add(exercise);
                            }
                            adapter.setExercises(exercisesList);
                        } else {
                            Log.d(TAG, "Ошибка при получении данных из Firestore", task.getException());
                            Toast.makeText(Tren_all_body_7x4.this, "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showBottomSheetDialog(Exercise exercise) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Tren_all_body_7x4.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TextView textViewExerciseName = bottomSheetView.findViewById(R.id.tvExerciseName);
        TextView textViewExerciseReps = bottomSheetView.findViewById(R.id.tvExerciseReps);
        TextView textViewExerciseInstructions = bottomSheetView.findViewById(R.id.tvExerciseInstructions);
        Button buttonWatchVideo = bottomSheetView.findViewById(R.id.buttonWatchVideo);

        textViewExerciseName.setText(exercise.getName());
        textViewExerciseReps.setText("Reps: " + exercise.getSetsReps());
        textViewExerciseInstructions.setText(exercise.getDescription());

        buttonWatchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = exercise.getDemonstration();
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.putExtra("force_fullscreen", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(Tren_all_body_7x4.this, "Видео недоступно", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(adapter.getExercises(), fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // No action needed for swipe
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            updateExerciseOrderInFirestore();
        }
    };

    private void updateExerciseOrderInFirestore() {
        List<Exercise> exercises = adapter.getExercises();
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            db.collection("exercises").document(exercise.getName()).update("order", i);
        }
    }

    private void startFirstExercise() {
        List<Exercise> exercises = adapter.getExercises();
        if (!exercises.isEmpty()) {
            workoutStartTime = System.currentTimeMillis(); // Установите время начала тренировки
            openExerciseActivity(0);
        }
    }

    private void startRestActivity(int currentExerciseIndex) {
        Intent intent = new Intent(Tren_all_body_7x4.this, RestActivity.class);
        intent.putExtra("exerciseList", new ArrayList<>(adapter.getExercises()));
        intent.putExtra("currentExerciseIndex", currentExerciseIndex);
        intent.putExtra("totalCaloriesBurned", totalCaloriesBurned);
        restActivityResultLauncher.launch(intent);
    }

    public void openExerciseActivity(int currentExerciseIndex) {
        List<Exercise> exercises = adapter.getExercises();
        if (currentExerciseIndex < exercises.size()) {
            Exercise exercise = exercises.get(currentExerciseIndex);
            totalCaloriesBurned += exercise.getCalories(); // Увеличиваем общее количество сожженных калорий
            Intent intent;
            switch (exercise.getName()) {
                case "Отжимания от пола":
                    intent = new Intent(Tren_all_body_7x4.this, PushUp.class);
                    break;
                case "Пресс":
                    intent = new Intent(Tren_all_body_7x4.this, PressExercise.class);
                    break;
                case "Отжимание с широким упором":
                    intent = new Intent(Tren_all_body_7x4.this, PushupWithAWideFocus.class);
                    break;
                case "Растяжка ромбовидных мышц":
                    intent = new Intent(Tren_all_body_7x4.this, StretchingTheRhomboidMusclesExercise.class);
                    break;
                case "Отжимание с упором впереди":
                    intent = new Intent(Tren_all_body_7x4.this, PushupWithEmphasis.class);
                    break;
                default:
                    intent = new Intent(Tren_all_body_7x4.this, JumpingWithArmsAndLegs.class);
                    break;
            }
            intent.putExtra("exerciseList", new ArrayList<>(exercises));
            intent.putExtra("currentExerciseIndex", currentExerciseIndex);
            intent.putExtra("caloriesBurned", exercise.getCalories());
            exerciseActivityResultLauncher.launch(intent);
        }
    }

    private void handleActivityResult(Intent data, int requestCode) {
        int caloriesBurned = data.getIntExtra("caloriesBurned", 0);
        totalCaloriesBurned += caloriesBurned;
        int currentIndex = data.getIntExtra("currentIndex", -1);
        if (currentIndex >= 0 && currentIndex < adapter.getExercises().size() - 1) {
            if (requestCode == 1) {
                startRestActivity(currentIndex + 1);
            } else {
                openExerciseActivity(currentIndex + 1);
            }
        } else {
            // Calculate time spent on training
            long endTime = System.currentTimeMillis();
            long durationMillis = endTime - workoutStartTime;
            long durationMinutes = durationMillis / (1000 * 60);
            countMin.setText(String.valueOf(durationMinutes));

            // Increase the number of completed trainings
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            int trainingsCompleted = prefs.getInt(KEY_TRAININGS_COMPLETED, 0);
            trainingsCompleted++;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_TRAININGS_COMPLETED, trainingsCompleted);
            editor.apply();

            countTren.setText(String.valueOf(trainingsCompleted));

            // Передать общее количество калорий в ViborTrenirovki
            Intent intent = new Intent(this, ViborTrenirovki.class);
            intent.putExtra("totalCaloriesBurned", totalCaloriesBurned);
            startActivity(intent);
            Toast.makeText(this, "Тренировка завершена! Сожжено калорий: " + totalCaloriesBurned, Toast.LENGTH_LONG).show();
        }
    }

    public void openAllBody7x4() {
        Intent intent = new Intent(this, All_body7x4.class);
        startActivity(intent);
    }
}
