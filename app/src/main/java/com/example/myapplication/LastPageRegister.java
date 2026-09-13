package com.example.myapplication;

import static androidx.appcompat.widget.AppCompatDrawableManager.get;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.util.LocalePreferences;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LastPageRegister extends AppCompatActivity {

    FirebaseFirestore firestone;
    TextView textBox;

    Button GoToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_last_page_register);

        firestone = FirebaseFirestore.getInstance();

        textBox = findViewById(R.id.textbox);
        GoToHomeBtn = findViewById(R.id.GoToHomeBtn);

        Random random = new Random();

        int num = random.nextInt(90000000) + 10000000;

        String iban = "DE" +  num;


        String firstName = getIntent().getStringExtra("FirstName");
        String lastName = getIntent().getStringExtra("LastName");
        String emailAddress = getIntent().getStringExtra("EmailAddress");

        textBox.setText("Hello " + firstName + " " + lastName + "," + "\n" + "This is your IBAN number: \n" + iban);


        GoToHomeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LastPageRegister.this, Home.class);
            intent.putExtra("USER_EMAIL", emailAddress);
            startActivity(intent);
            finish();
        });

    }
}