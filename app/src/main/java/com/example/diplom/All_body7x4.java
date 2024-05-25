package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class All_body7x4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_body7x4);

        TextView LeftArrow = findViewById(R.id.leftArrow);
        ImageView firstDayFirstWeekImageView = findViewById(R.id.FirstDayFirstWeek);

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

    private void  openViborTrenirovki()
        {
        Intent intent = new Intent(this,ViborTrenirovki.class );
        startActivity(intent);
    }

    private void openTren7x4()
    {
        Intent intent = new Intent(this, Tren_all_body_7x4.class);
        startActivity(intent);
    }
}