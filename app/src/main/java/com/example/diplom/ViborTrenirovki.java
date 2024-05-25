package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViborTrenirovki extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibor_trenirovki);

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


        //программа всего тела
        allBodyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllBodyActivity();
            }
        });

        btnAllBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllBodyActivity();
            }
        });

        //программа нижней части тела
//        lowerBodyLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { openLowerBodyActivity ();}
//        });
//
//        btnLowerBody.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { openLowerBodyActivity ();}
//        });
//
//        //программа пресса
//
//        programmaPress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { openProgrammPres();}
//        });
//
//        btnPress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { openProgrammPres();}
//        });
//
//        //программа груди
//        programmaChest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) { openProgrammChest();}
//        });
//
//        btnChest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {openProgrammChest();}
//        });
//
//        //программа рук
//        programmaHand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {openProgrammHand();}
//        });

//        btnHand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {openProgrammHand();}
//        });

    }

    private void openAllBodyActivity() {
        Intent intent = new Intent(this, All_body7x4.class);
        startActivity(intent);
    }

//    private void openLowerBodyActivity(){
//        Intent intent = new Intent(this, Activity_lower_part_of_body.class);
//        startActivity(intent);
//    }

//    private void openProgrammPres (){
//        Intent intent = new Intent(this, PressKachat.class);
//        startActivity(intent);
//    }

//    private void openProgrammChest (){
//        Intent intent = new Intent(this, ChestKachat.class);
//        startActivity(intent);
//    }

//    private void openProgrammHand (){
//        Intent intent = new Intent(this, HandKachat.class);
//        startActivity(intent);
//    }

}
