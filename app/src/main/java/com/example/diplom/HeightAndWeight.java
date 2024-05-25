package com.example.diplom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HeightAndWeight extends AppCompatActivity {

    private EditText editTextWeight;
    private EditText editTextHeight;
    private Spinner spinnerGender;
    private Button btnSaveData;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_and_weight);

        db = FirebaseFirestore.getInstance();

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveData = findViewById(R.id.btnSaveData);

        String[] genderOptions = {"Выберите ваш пол", "Мужской", "Женский"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderOptions);
        spinnerGender.setAdapter(genderAdapter);

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        String weight = editTextWeight.getText().toString().trim();
        String height = editTextHeight.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        // Проверка на пустые поля
        if (TextUtils.isEmpty(weight) || TextUtils.isEmpty(height)) {
            Toast.makeText(this, "Введите все данные", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создание Map с данными пользователя
        Map<String, Object> user = new HashMap<>();
        user.put("weight", Double.parseDouble(weight));
        user.put("height", Double.parseDouble(height));
        user.put("gender", gender);

        // Сохранение данных в Firestore
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HeightAndWeight.this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                        // Переход к следующей активности или действию
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HeightAndWeight.this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                    }
                });
        Intent intent = new Intent(HeightAndWeight.this, ViborTrenirovki.class);
        startActivity(intent);
        finish();
    }
}
