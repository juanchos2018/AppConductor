package com.conductor.appconductor.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.conductor.appconductor.R;
import com.google.android.gms.maps.SupportMapFragment;

public class Mapa1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }
}
