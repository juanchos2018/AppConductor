package com.conductor.appconductor.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.conductor.appconductor.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    Button btn_enviar_posicion;
    GoogleMap gMap;
    LocationManager lm;
    Location location;

    TextView tvlati,tvlongi;
    double lat,lon;
    double lat_conductir,lon_conductor;
    String key_envio;
    DatabaseReference reference;

    private FirebaseAuth mAuth;
     String user_uID;
    private DatabaseReference userDatabaseReference;

    public FirebaseUser currentUser;
    public MapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_mapa, container, false);
        btn_enviar_posicion=(Button)vista.findViewById(R.id.btnenviar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //  user_id="-MBCk207t_xaxJQGjvyF";
       user_uID = mAuth.getCurrentUser().getUid();

        Bundle data = this.getArguments();
        if(data != null){
            String la = data.getString("lati");
            String lo = data.getString("long");
            key_envio = data.getString("key");
            lat=Double.parseDouble(la);
            lon=Double.parseDouble(lo);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btn_enviar_posicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPosicion(lat_conductir,lon_conductor);
            }
        });
        return  vista;
    }

    private void enviarPosicion(double lat_conductir, double lon_conductor) {
        if (lat_conductir==0){
            Toast.makeText(getContext(), "Falta posicion", Toast.LENGTH_SHORT).show();
        }
        else if ( lon_conductor==0 ){
            Toast.makeText(getContext(), "Falta posicion", Toast.LENGTH_SHORT).show();
        }
        else {
         //   String  user_id="-MBCk207t_xaxJQGjvyF";
            reference= FirebaseDatabase.getInstance().getReference("PedidosCliente").child(user_uID);
            reference.child(key_envio).child("latitud_conductor") .setValue(lat_conductir);
            reference.child(key_envio).child("longitud_conductor") .setValue(lon_conductor);
            Toast.makeText(getContext(), "Enviado", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Localizacion", location.getLatitude() + " y " + location.getLongitude());
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
//LOCATION_SERVICE
        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();
               // MarkerOptions cliente = new MarkerOptions();
                LatLng cliente = new LatLng(lat, lon);
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);


                gMap.clear();
                Toast.makeText(getContext(), String.valueOf(latLng.latitude+ "-" +latLng.longitude), Toast.LENGTH_SHORT).show();

                gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                lat_conductir=latLng.latitude;
                lon_conductor=latLng.longitude;
                gMap.addMarker(markerOptions);
                gMap.addMarker(new MarkerOptions().position(cliente).title("Cliente"));
                //lat=latLng.latitude;
                //lon=latLng.longitude;
             //   double la=latLng.latitude;
               // double lo=latLng.longitude;



                //  txtlatitud1.setText(String.valueOf(la));
                //txtlngitud1.setText(String.valueOf(lo));
            }
        });
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
       // lat=location.getLongitude();
        //lon=location.getLatitude();
        LatLng aquitoy = new LatLng(latitude, longitude);
        LatLng cliente = new LatLng(lat, lon);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(aquitoy)
                .zoom(14)//esto es el zoom
                .bearing(30)//esto es la inclonacion
                .build();


        gMap.addMarker(new MarkerOptions().position(aquitoy).title("Aqui estoy wey"));
        gMap.addMarker(new MarkerOptions().position(cliente).title("Cliente"));
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

}
