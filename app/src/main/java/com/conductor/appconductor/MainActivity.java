package com.conductor.appconductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conductor.appconductor.Activitys.EntregasActivity;
import com.conductor.appconductor.Activitys.PerfilActivity;
import com.conductor.appconductor.Activitys.UbicacionActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;

    public FirebaseUser currentUser;

    CardView carpedido,carperfil;

    double latitud;
    double longitud;
    private LocationManager locManager;
    private Location loc;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
                final String  user_uID = mAuth.getCurrentUser().getUid();
            final String  correo = mAuth.getCurrentUser().getEmail();
            id=mAuth.getCurrentUser().getUid();
            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Conductores").child(user_uID);
            userDatabaseReference.keepSynced(true);
            userDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
//                        String name = dataSnapshot.child("nombre").getValue().toString();
  //                      String urlimageperfil = dataSnapshot.child("user_image").getValue().toString();
    //                    String tipousu = dataSnapshot.child("tipo").getValue().toString();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("error  ", databaseError.getMessage());
                }
            });

        }


        carpedido =(CardView)findViewById(R.id.cardPedidos);

        carperfil=(CardView)findViewById(R.id.cardPerfil);
        carpedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entregas();
            }
        });
        carperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irperfil();
            }
        });
       // btn2.setOnClickListener(new View.OnClickListener() {


    }

    private void irperfil() {
        startActivity(new Intent(MainActivity.this, PerfilActivity.class));
    }

    private void ubicacion() {

        startActivity(new Intent(MainActivity.this, UbicacionActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
        //checking logging, if not login redirect to Login ACTIVITY
        if (currentUser == null ){
            mAuth.signOut();
            logOutUser(); // Return to Login activity

        }
        if (currentUser != null ){
            userDatabaseReference.child("active_now").setValue("true");

        }
    }


    private void ObtenerUbicacion() {

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     //   Toast.makeText(this, "lat :"+String.valueOf(loc.getLatitude()) +" lo: "+String.valueOf(loc.getLongitude()), Toast.LENGTH_SHORT).show();
        // tvLatitud.setText(String.valueOf(loc.getLatitude()));
        //tvLongitud.setText(String.valueOf(loc.getLongitude()));
        latitud=loc.getLatitude();
        longitud=loc.getLongitude();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Conductores").child(id);
        userDatabaseReference.child("lat_conductor") .setValue(latitud);
        userDatabaseReference.child("lon_conductor") .setValue(longitud);


    }

    private void logOutUser() {
        //TODO : ESTO ES PARA QUE REDIRECCIONE AL LOGIN
        //   borrarshaoresferes();
        Intent loginIntent =  new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void entregas() {

        startActivity(new Intent(MainActivity.this, EntregasActivity.class));
        ObtenerUbicacion();

    }
}
