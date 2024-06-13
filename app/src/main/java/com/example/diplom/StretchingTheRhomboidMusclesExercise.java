package com.example.diplom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diplom.Models.Exercise;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class StretchingTheRhomboidMusclesExercise extends AppCompatActivity {

    private static final String TAG = "StretchingExercise";
    private FirebaseFirestore db;
    private TextView tvReps;
    private ImageView ivQuestion;
    private Button btnFinishExercise;
    private ArrayList<Exercise> exerciseList;
    private int currentExerciseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stretching_the_rhomboid_muscles_exercise);

        tvReps = findViewById(R.id.Reps);
        ivQuestion = findViewById(R.id.question);
        btnFinishExercise = findViewById(R.id.FinishFirstExercise);

        db = FirebaseFirestore.getInstance();
        loadRepsFromDatabase();

        exerciseList = (ArrayList<Exercise>) getIntent().getSerializableExtra("exerciseList");
        currentExerciseIndex = getIntent().getIntExtra("currentExerciseIndex", 0);

        btnFinishExercise.setOnClickListener(new View.OnClickListener() {
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
        Exercise exercise = new Exercise("Растяжка ромбовидных мышц", "Встаньте ровно, ноги шире плеч. Руки параллельно земле, согните локти. Отводите локти назад и сведите лопатки", "", "3 подхода по 14 повторений", 2);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(StretchingTheRhomboidMusclesExercise.this);
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
                    Toast.makeText(StretchingTheRhomboidMusclesExercise.this, "Видео недоступно", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        Log.d(TAG, "BottomSheetDialog shown");
    }

    private void startRestActivity() {
        Intent intent = new Intent(StretchingTheRhomboidMusclesExercise.this, RestActivity.class);
        intent.putExtra("exerciseList", exerciseList);
        intent.putExtra("currentExerciseIndex", currentExerciseIndex);
        startActivity(intent);
        finish();
    }
}
