package com.conductor.appconductor.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conductor.appconductor.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Mapa2Activity extends FragmentActivity implements OnMapReadyCallback, LocationListener {


    private GoogleMap mMap;
    double lat, lon;
    double lat_conductir, lon_conductor;
    String key_envio;
    DatabaseReference reference,reference2,reference3;
    Location location;
    LocationManager lm;
    private LocationManager locManager;
    private Location loc;
    Button btn_enviar_posicion;

    public FirebaseUser currentUser;
    String celular;
    private FirebaseAuth mAuth;
    String user_uID;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;

    String lati,loti,key;
    boolean isFABOpen = false;

    private static final String TAG = "Mapa2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa2);
        btn_enviar_posicion=(Button)findViewById(R.id.btnenviar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //  user_id="-MBCk207t_xaxJQGjvyF";
        user_uID = mAuth.getCurrentUser().getUid();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //  fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        lati=getIntent().getStringExtra("la");
        loti=getIntent().getStringExtra("lo");
        key=getIntent().getStringExtra("key");
        celular=getIntent().getStringExtra("cel");


        lat=Double.parseDouble(lati);
        lon=Double.parseDouble(loti);

        Log.e(TAG, lati + " "+  loti );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(Mapa2Activity.this, "llamar", Toast.LENGTH_SHORT).show();
                double la = -18.0288008;
                double lo = -70.2534481;
                Telefono(celular);
              //  Enviar(la, lo);
             //   ObtenerUbicacion();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TerminarEntrega();
            }
        });


        btn_enviar_posicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPosicion(lat_conductir,lon_conductor);
            }
        });
    }

    public  void  Hilo(){
        try {

            Thread.sleep(2000);

        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    public  class  time extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int i=0;i<4;i++)
            {
                Hilo();
            }
            return  true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ObtenerUbicacion();
         //   Toast.makeText(HilosActivity.this, "cada 3 segundo", Toast.LENGTH_SHORT).show();
        }
    }

    private  void TerminarEntrega(){


        // para el nodo empresa

        String id_empresa="2";
        reference3= FirebaseDatabase.getInstance().getReference("PedidoEmpresa").child(id_empresa);
     //   reference2= FirebaseDatabase.getInstance().getReference("Conductores").child(user_uID);

        reference3.child(key).child("estado").setValue("Entregado");

        reference2= FirebaseDatabase.getInstance().getReference("PedidosCliente").child(user_uID).child(key);
        reference2.removeValue();

        Toast.makeText(this, "Terminado", Toast.LENGTH_SHORT).show();
        //reference.child(key).child("longitud_conductor") .setValue(lon_conductor);

      //  Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();


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
       // Toast.makeText(this, "lat :"+String.valueOf(loc.getLatitude()) +" lo: "+String.valueOf(loc.getLongitude()), Toast.LENGTH_SHORT).show();
       // tvLatitud.setText(String.valueOf(loc.getLatitude()));
        //tvLongitud.setText(String.valueOf(loc.getLongitude()));


    }

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
         fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    //    fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
     //   fab3.animate().translationY(0);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Agregamos un marcador en Lima y movemos la cámara
     //   LatLng ubicacion = new LatLng(-12.0560155, -77.0617879);
     //   mMap.animateCamera(CameraUpdateFactory.newLatLng(ubicacion));
      //  mMap.setMinZoomPreference(6.0f);
       // mMap.setMaxZoomPreference(14.0f);
     //   LatLng cliente = new LatLng(lat, lon);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        LatLng aquitoy = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(aquitoy)
                .zoom(12)//esto es el zoom
                .bearing(30)//esto es la inclonacion
                .build();
        LatLng cliente = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)).anchor(0.1f,0.5f).position(cliente).title("Cliente"));
        //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mundo)).anchor(0.0f,1.0f).position(aquitoy));

        mMap.addMarker(new MarkerOptions().position(aquitoy).title("Yo"));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();

                LatLng cliente = new LatLng(lat, lon);
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                lat_conductir=latLng.latitude;
                lon_conductor=latLng.longitude;
                mMap.addMarker(markerOptions);
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user)).anchor(0.1f,0.5f).position(cliente).title("Cliente"));

            }
        });

    }

    private  void  Enviar(double la, double lon){
        mMap.clear();
        LatLng myposition = new LatLng(la, lon);
        mMap.addMarker(new MarkerOptions().position(myposition).title("Aquit we"));
    }

    private void enviarPosicion(double lat_conductir, double lon_conductor) {
        if (lat_conductir==0){
            Toast.makeText(this, "falta posocio", Toast.LENGTH_SHORT).show();
        }
        else if ( lon_conductor==0 ){
            Toast.makeText(this, "faltao posicion", Toast.LENGTH_SHORT).show();
        }
        else {
            //   String  user_id="-MBCk207t_xaxJQGjvyF";
            reference= FirebaseDatabase.getInstance().getReference("PedidosCliente").child(user_uID);
            reference2= FirebaseDatabase.getInstance().getReference("Conductores").child(user_uID);
            reference.child(key).child("latitud_conductor") .setValue(lat_conductir);
            reference.child(key).child("longitud_conductor") .setValue(lon_conductor);
            Toast.makeText(this, "Enviado", Toast.LENGTH_SHORT).show();


            reference2.child("lat_conductor").setValue(lat_conductir);
            reference2.child("lon_conductor").setValue(lon_conductor);

        }

    }


    private void Telefono(String numero) {
        try {
            if (TextUtils.isEmpty(numero)){
                Toast.makeText(this, "no existe numero de telefono", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+numero));
                startActivity(intent);
            }

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void WhataApps(String telefono) {
        try {

            if (TextUtils.isEmpty(telefono)){
                Toast.makeText(this, "no hay telefono", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent _intencion = new Intent("android.intent.action.MAIN");
                _intencion.setAction(Intent.ACTION_SEND);
                _intencion.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                _intencion.putExtra("jid", PhoneNumberUtils.stripSeparators("51"+telefono)+"@s.whatsapp.net");
                startActivity(_intencion);
            }

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
}
