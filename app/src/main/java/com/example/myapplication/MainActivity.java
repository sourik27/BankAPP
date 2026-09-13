package com.example.myapplication;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    TextView message;
    EditText FirstName,password;
    Button btnLogin,btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        message = findViewById(R.id.txtmessage);
        message.setVisibility(GONE);
        FirstName = findViewById(R.id.FirstName);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(view ->{
            String user = FirstName.getText().toString().trim();
            String pass = password.getText().toString().trim();

            login(user,pass);

        });

        btnRegister.setOnClickListener(view ->{
            Intent register = new Intent(MainActivity.this, Register.class);
            startActivity(register);
        });
    }

    private void login(String user, String pass) {
        firestore.collection("users")
                .whereEqualTo("FirstName", user)
                .whereEqualTo("Password", pass)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        // Check whether a matching user was found
                        if (!task.getResult().isEmpty()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("LOGIN", document.getId() + " => " + document.getData());

                                // Extract the email stored during registration
                                String userEmail = document.getString("E-Mail");

                                // Login successful
                                Intent toHomePage = new Intent(MainActivity.this, Home.class);
                                toHomePage.putExtra("USER_EMAIL", userEmail);

                                startActivity(toHomePage);
                                finish();

                                break;
                            }

                        } else {
                            // No matching username/password
                            Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Firestore error
                        Log.d("LOGIN", "Error getting documents: ",
                                task.getException());

                        Toast.makeText(
                                MainActivity.this,
                                "Login error. Please try again.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}
