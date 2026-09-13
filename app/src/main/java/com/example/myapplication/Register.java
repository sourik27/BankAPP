package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText FirstName, LastName, EmailAddress, Password;
    private Button btnRegister2;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        firestore = FirebaseFirestore.getInstance();


        btnRegister2 = findViewById(R.id.btnRegister2);
        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        EmailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);

        btnRegister2.setOnClickListener(view -> {
            String firstName = FirstName.getText().toString().trim();
            String lastName = LastName.getText().toString().trim();
            String emailAddress = EmailAddress.getText().toString().trim();
            String password = Password.getText().toString().trim();


            if (firstName.isEmpty()) {
                FirstName.setError("Enter your First Name");
            } else if (lastName.isEmpty()) {
                LastName.setError("Enter your Last Name");
            } else if (emailAddress.isEmpty()) {
                EmailAddress.setError("Enter your E-Mail address");
            } else if (password.isEmpty()) {
                Password.setError("Enter a Password");
            } else {


                Map<String, Object> users = new HashMap<>();
                users.put("FirstName", firstName);
                users.put("LastName", lastName);
                users.put("E-Mail", emailAddress);
                users.put("Password", password);

                firestore.collection("users")
                        .add(users)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(Register.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();


                                Intent registerPage2 = new Intent(Register.this, LastPageRegister.class);
                                registerPage2.putExtra("FirstName", firstName);
                                registerPage2.putExtra("LastName", lastName);
                                registerPage2.putExtra("EmailAddress", emailAddress);
                                startActivity(registerPage2);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error saving data: ", e);
                                Toast.makeText(Register.this, "Database Write Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}