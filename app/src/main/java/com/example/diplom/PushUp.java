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
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diplom.Models.Exercise;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class PushUp extends AppCompatActivity {

    private static final String TAG = "PushUp";

    private FirebaseFirestore db;
    private TextView tvReps;
    private ImageView ivQuestion;
    private Button btnFinish;
    private ArrayList<Exercise> exerciseList;
    private int currentExerciseIndex;
    private int totalCaloriesBurned;
    private Exercise currentExercise;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_push_up);

        tvReps = findViewById(R.id.Reps);
        ivQuestion = findViewById(R.id.question);
        btnFinish = findViewById(R.id.FinishThirdExercise);

        db = FirebaseFirestore.getInstance();
        loadRepsFromDatabase();

        exerciseList = (ArrayList<Exercise>) getIntent().getSerializableExtra("exerciseList");
        currentExerciseIndex = getIntent().getIntExtra("currentExerciseIndex", 0);
        totalCaloriesBurned = getIntent().getIntExtra("totalCaloriesBurned", 0);

        if (exerciseList != null && currentExerciseIndex < exerciseList.size()) {
            currentExercise = exerciseList.get(currentExerciseIndex);
        }

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishExercise();
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

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PushUp.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TextView textViewExerciseName = bottomSheetView.findViewById(R.id.tvExerciseName);
        TextView textViewExerciseReps = bottomSheetView.findViewById(R.id.tvExerciseReps);
        TextView textViewExerciseInstructions = bottomSheetView.findViewById(R.id.tvExerciseInstructions);
        Button buttonWatchVideo = bottomSheetView.findViewById(R.id.buttonWatchVideo);

        textViewExerciseName.setText(currentExercise.getName());
        textViewExerciseReps.setText("Reps: " + currentExercise.getSetsReps());
        textViewExerciseInstructions.setText(currentExercise.getDescription());

        buttonWatchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = currentExercise.getDemonstration();
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.putExtra("force_fullscreen", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(PushUp.this, "Видео недоступно", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        Log.d(TAG, "BottomSheetDialog shown");
    }

    private void finishExercise() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("currentIndex", currentExerciseIndex);
        resultIntent.putExtra("caloriesBurned", currentExercise.getCalories());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
