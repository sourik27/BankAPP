package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Transfer extends AppCompatActivity {

    EditText transAmount;
    Button TransferBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transfer);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        transAmount = findViewById(R.id.transAmount);
        TransferBtn = findViewById(R.id.TransferBtn);

        double totalAmount = getIntent().getDoubleExtra("TotalAmount", 0.0);
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        TransferBtn.setOnClickListener(view -> {
            String inputValue = transAmount.getText().toString().trim();

            // Validate that input is not empty and contains only numbers (with an optional decimal point)
            if (inputValue.isEmpty() || !inputValue.matches("^\\d*\\.?\\d+$")) {
                Toast.makeText(Transfer.this, "Use numbers to transfer money", Toast.LENGTH_SHORT).show();
                return;
            }

            double newTransferAmount;
            try {
                newTransferAmount = Double.parseDouble(inputValue);
            } catch (NumberFormatException e) {
                Toast.makeText(Transfer.this, "Use numbers to transfer money", Toast.LENGTH_SHORT).show();
                return;
            }

            double result = totalAmount - newTransferAmount;

            Map<String, Object> transaction = new HashMap<>();
            transaction.put("TransferAmount in €", newTransferAmount);
            transaction.put("E-Mail", userEmail);

            // Write to database first before displaying success or navigating away
            db.collection("transaction")
                    .add(transaction)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Transferred successfully"); 
                        Toast.makeText(Transfer.this, "Transferred successfully", Toast.LENGTH_SHORT).show();

                        Intent changingTotalAmount = new Intent(Transfer.this, Home.class);
                        changingTotalAmount.putExtra("RESULT", result);
                        changingTotalAmount.putExtra("USER_EMAIL", userEmail);
                        startActivity(changingTotalAmount);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error while transferring", e);
                        Toast.makeText(Transfer.this, "Error while transferring money", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}