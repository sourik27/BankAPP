package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnOpenMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnOpenMap = findViewById(R.id.btnOpenMap);
        locationManager = LocationManager.getInstance();

        btnOpenMap.setOnClickListener(v -> {
            if (!PermissionsManager.isLocationPermissionGranted(MapsActivity.this)) {
                PermissionsManager.requestLocationPermission(MapsActivity.this);
            } else {
                openMap();
            }
        });
    }

    private void openMap() {
        if (!locationManager.isLocationEnabled(this)) {
            locationManager.createLocationRequest(this);
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsManager.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openMap();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
    }

    private void setupMap() {
        LatLng initialLocation = new LatLng(-34.0, 151.0);
        mMap.addMarker(new MarkerOptions().position(initialLocation).title("Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12f));

        // 1. On Click Listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Clicked: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        // 2. On Long Click Listener
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("New Pin"));
                Toast.makeText(MapsActivity.this, "Marker added!", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. On Camera Idle Listener
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng centerCoordinates = mMap.getCameraPosition().target;
            }
        });
    }
}