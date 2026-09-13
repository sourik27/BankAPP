package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ServiceFragment extends Fragment {

    private Button btnFindAtms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFindAtms = view.findViewById(R.id.btnFindAtms);

        btnFindAtms.setOnClickListener(v -> openGoogleMapsNearbyAtms());
    }

    private void openGoogleMapsNearbyAtms() {
        // Search URI for nearby ATMs centered on current location
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=ATM");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Check if Google Maps app is installed
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Fallback: Open in web browser if Google Maps app is not installed
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=ATM"));
            startActivity(webIntent);
        }
    }
}