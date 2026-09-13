package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private static final String ARG_AMOUNT = "arg_amount";
    private static final String ARG_EMAIL = "arg_email";

    public static HomeFragment newInstance(double amount, String email) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_AMOUNT, amount);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText totalAmount = view.findViewById(R.id.totalAmount);
        TextView dateText = view.findViewById(R.id.dateText);
        ImageButton transferBtn = view.findViewById(R.id.transferBtn);

        double amount = getArguments() != null ? getArguments().getDouble(ARG_AMOUNT, 100_000.00) : 100_000.00;
        String userEmail = getArguments() != null ? getArguments().getString(ARG_EMAIL) : null;

        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        dateText.setText(date);
        totalAmount.setText(String.valueOf(amount));

        transferBtn.setOnClickListener(v -> {
            String input = totalAmount.getText().toString().trim();
            double parsedAmount;
            try {
                parsedAmount = input.isEmpty() ? 0.00 : Double.parseDouble(input);
            } catch (NumberFormatException e) {
                parsedAmount = 0.00;
            }

            Intent transfer = new Intent(getActivity(), Transfer.class);
            transfer.putExtra("TotalAmount", parsedAmount);
            transfer.putExtra("USER_EMAIL", userEmail);
            startActivity(transfer);
        });
    }
}