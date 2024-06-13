package com.example.diplom;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnSuccessListener;

public class ExerciseBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_EXERCISE_ID = "exerciseId";

    private TextView tvExerciseName;
    private TextView tvExerciseReps;
    private TextView tvExerciseInstructions;
    private VideoView videoView;

    public static ExerciseBottomSheetFragment newInstance(String exerciseId) {
        ExerciseBottomSheetFragment fragment = new ExerciseBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_ID, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        tvExerciseName = view.findViewById(R.id.tvExerciseName);
        tvExerciseReps = view.findViewById(R.id.tvExerciseReps);
        tvExerciseInstructions = view.findViewById(R.id.tvExerciseInstructions);

        if (getArguments() != null) {
            String exerciseId = getArguments().getString(ARG_EXERCISE_ID);
            loadExerciseData(exerciseId);
        }

        return view;
    }

    private void loadExerciseData(String exerciseId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("exercises").document(exerciseId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String reps = documentSnapshot.getString("reps");
                    String instructions = documentSnapshot.getString("instructions");
                    String videoUrl = documentSnapshot.getString("videoUrl");
                    String calories = documentSnapshot.getString("calories");

                    tvExerciseName.setText(name);
                    tvExerciseReps.setText("Reps: " + reps);
                    tvExerciseInstructions.setText(instructions);

                    // Загрузка видео из URL-адреса в VideoView
                    Uri videoUri = Uri.parse(videoUrl);
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                }
            }
        });
    }
}