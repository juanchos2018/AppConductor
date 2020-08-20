package com.conductor.appconductor.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.conductor.appconductor.Fragments.MapaFragment;
import com.conductor.appconductor.R;

public class MapaClienteActivity extends AppCompatActivity {

    String lati,loti,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cliente);

        lati=getIntent().getStringExtra("la");
        loti=getIntent().getStringExtra("lo");
        key=getIntent().getStringExtra("key");

        MapaFragment fr = new MapaFragment();
          Bundle args = new Bundle();
          args.putString("lati", lati);
          args.putString("long", loti);
          args.putString("key", key);
         fr.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.contenerdor,fr).commit();

    }
}
