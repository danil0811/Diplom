package com.example.diplom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class All_body7x4 extends AppCompatActivity {

    private static final int REQUEST_CODE_TRAINING = 1;
    private ImageView firstDayFirstWeekImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_body7x4);

        TextView LeftArrow = findViewById(R.id.leftArrow);
        firstDayFirstWeekImageView = findViewById(R.id.FirstDayFirstWeek);

        LeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViborTrenirovki();
            }
        });

        firstDayFirstWeekImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTren7x4();
            }
        });
    }

    private void openViborTrenirovki() {
        Intent intent = new Intent(this, ViborTrenirovki.class);
        startActivity(intent);
    }

    private void openTren7x4() {
        Intent intent = new Intent(this, Tren_all_body_7x4.class);
        startActivityForResult(intent, REQUEST_CODE_TRAINING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TRAINING && resultCode == RESULT_OK) {
            boolean workoutCompleted = data.getBooleanExtra("WORKOUT_COMPLETED", false);
            if (workoutCompleted) {
                completeWorkout();
            }
        }
    }

    private void completeWorkout() {
        firstDayFirstWeekImageView.setColorFilter(Color.GREEN);
    }
}
