package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
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

    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter adapter;
    private FirebaseFirestore db;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "Tren_all_body_7x4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tren_all_body7x4);

        TextView LeftArrow = findViewById(R.id.leftArrow);

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
                openTrenAllBody7x4();
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

                                Exercise exercise = new Exercise(name, description, demonstration, setsReps);
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

        textViewExerciseName.setText(exercise.getName());
        textViewExerciseReps.setText("Reps: " + exercise.getSetsReps());
        textViewExerciseInstructions.setText(exercise.getDescription());

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

    private void  openTrenAllBody7x4()
    {
        Intent intent = new Intent(this,All_body7x4.class );
        startActivity(intent);
    }
}