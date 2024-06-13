package com.example.diplom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diplom.Models.Exercise;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PressExercise extends AppCompatActivity {

    private static final String TAG = "PressExercise";
    private FirebaseFirestore db;
    private TextView tvReps;
    private ImageView ivQuestion;
    private Button btnFinish;
    private ArrayList<Exercise> exerciseList;
    private int currentExerciseIndex;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_exercise);

        tvReps = findViewById(R.id.Reps);
        ivQuestion = findViewById(R.id.question);
        btnFinish = findViewById(R.id.FinishSecondExercise);

        db = FirebaseFirestore.getInstance();
        loadRepsFromDatabase();

        exerciseList = (ArrayList<Exercise>) getIntent().getSerializableExtra("exerciseList");
        currentExerciseIndex = getIntent().getIntExtra("currentExerciseIndex", 0);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRestActivity();
            }
        });

        ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Question icon clicked");
                showBottomSheetDialog();
            }
        });
    }

    private void loadRepsFromDatabase() {
        db.collection("exercises")
                .whereEqualTo("name", "Stretching The Rhomboid Muscles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String reps = document.getString("setsReps");
                            tvReps.setText(reps);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void showBottomSheetDialog() {
        Log.d(TAG, "Attempting to show BottomSheetDialog");

        // Using dummy data for simplicity
        Exercise exercise = new Exercise("упражнения для пресса", "Лягте на спину, ноги согнуты в коленях. Руки перед собой или за головой. Поднимайте корпус.", "", "3 повтора по 10 повторений", 10);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PressExercise.this);
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
                    Toast.makeText(PressExercise.this, "Видео недоступно", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        Log.d(TAG, "BottomSheetDialog shown");
    }

    private void startRestActivity() {
        Intent intent = new Intent(PressExercise.this, RestActivity.class);
        intent.putExtra("exerciseList", exerciseList);
        intent.putExtra("currentExerciseIndex", currentExerciseIndex);
        startActivity(intent);
        finish();
    }
}
