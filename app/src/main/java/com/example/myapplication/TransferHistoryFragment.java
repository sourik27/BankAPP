package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class TransferHistoryFragment extends Fragment {

    private TextView textAllData;
    private LinearLayout cardContainer;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_history, container, false);
        textAllData = view.findViewById(R.id.textAllData);
        cardContainer = view.findViewById(R.id.cardContainer);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        // 1. Get the logged-in user's email from the host Activity's Intent
        String currentUserEmail = null;
        if (getActivity() != null && getActivity().getIntent() != null) {
            currentUserEmail = getActivity().getIntent().getStringExtra("USER_EMAIL");
        }

        // 2. Filter transactions where "E-Mail" matches the logged-in user's email
        db.collection("transaction")
                .whereEqualTo("E-Mail", currentUserEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        cardContainer.removeAllViews();

                        if (!task.getResult().isEmpty()) {
                            textAllData.setVisibility(View.GONE);

                            float density = getResources().getDisplayMetrics().density;
                            int padding = (int) (16 * density);
                            int spacing = (int) (14 * density);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("E-Mail");
                                Double amount = document.getDouble("TransferAmount in €");

                                // Create Material CardView
                                MaterialCardView card = new MaterialCardView(requireContext());
                                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                cardParams.setMargins(0, 0, 0, spacing); // Space between cards
                                card.setLayoutParams(cardParams);
                                card.setCardBackgroundColor(Color.parseColor("#1E293B"));
                                card.setRadius(14 * density);
                                card.setCardElevation(4 * density);
                                card.setStrokeColor(Color.parseColor("#334155"));
                                card.setStrokeWidth((int) (1 * density));

                                // Create Inner Layout for Card Text
                                LinearLayout innerLayout = new LinearLayout(requireContext());
                                innerLayout.setOrientation(LinearLayout.VERTICAL);
                                innerLayout.setPadding(padding, padding, padding, padding);

                                // User Email Text
                                TextView txtEmail = new TextView(requireContext());
                                txtEmail.setText("User: " + (email != null ? email : "N/A"));
                                txtEmail.setTextColor(Color.parseColor("#F8FAFC"));
                                txtEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                txtEmail.setTypeface(null, Typeface.BOLD);

                                // Deducted Amount Text (Red with minus sign)
                                TextView txtAmount = new TextView(requireContext());
                                txtAmount.setText("Amount: -€" + (amount != null ? amount : 0.0));
                                txtAmount.setTextColor(Color.parseColor("#EF4444")); // Red color for deduction
                                txtAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                txtAmount.setTypeface(null, Typeface.BOLD);
                                txtAmount.setPadding(0, (int) (8 * density), 0, 0);

                                // Build Card Structure
                                innerLayout.addView(txtEmail);
                                innerLayout.addView(txtAmount);
                                card.addView(innerLayout);

                                // Add finished card to container
                                cardContainer.addView(card);
                            }
                        } else {
                            textAllData.setVisibility(View.VISIBLE);
                            textAllData.setText("No transaction history for this account.");
                        }

                    } else {
                        textAllData.setVisibility(View.VISIBLE);
                        textAllData.setText("Error loading transactions.");
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}