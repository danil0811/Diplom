package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button BtnGoToMenu= (Button)findViewById(R.id.BtnGoToUpraz);

        View.OnClickListener oclBtnGoToUpraz = view -> {
            Intent intent = new Intent(HomeActivity.this, HeightAndWeight.class);
            startActivity(intent);
        };

        BtnGoToMenu.setOnClickListener(oclBtnGoToUpraz);
    }
}