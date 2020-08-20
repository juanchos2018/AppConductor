package com.conductor.appconductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.conductor.appconductor.Activitys.OlvideClaveActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {


    EditText et_correo,et_clave;
    Button btningresar;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
  //  private FirebaseUser user;
    public FirebaseUser currentUser;
    private DatabaseReference userDatabaseReference;
    DatabaseReference reference,reference2;
    TextView txtolvide;
    private LocationManager locManager;
    private Location loc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        cargarcontroles();



        mAuth = FirebaseAuth.getInstance();
     //   user = mAuth.getCurrentUser();
        currentUser = mAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("Conductores");
        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo=et_correo.getText().toString();
                String clave=et_clave.getText().toString();
                ingresar(correo,clave);
            }
        });


        txtolvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inrolvide();
            }
        });
    }


    private void ingresar(String correo, String clave) {
        progressDialog = new ProgressDialog(this);
        if (TextUtils.isEmpty(correo)){
            et_correo.setError("campo requerido");

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(clave)){
            et_clave.setError("campo requerido");
        }
        else{

            progressDialog.setMessage("Cargando...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            mAuth.signInWithEmailAndPassword(correo,clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        String userUID = mAuth.getCurrentUser().getUid();
                        String userDeiceToken = FirebaseInstanceId.getInstance().getToken();

                        userDatabaseReference.child(userUID).child("device_token").setValue(userDeiceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkVerifiedEmail();
                            }
                        });
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Verifique su Email", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }
            });

        }


    }
    private void checkVerifiedEmail() {
        currentUser = mAuth.getCurrentUser();
        boolean isVerified = false;
        if (currentUser != null) {
            isVerified = currentUser.isEmailVerified();
        }
        if (isVerified){

            final String UID = mAuth.getCurrentUser().getUid();
            userDatabaseReference.child(UID).child("verified").setValue("true");
            userDatabaseReference.child(UID).child("verificado").setValue("true");
            SharedPreferences preferences= getSharedPreferences("mitoken", Context.MODE_PRIVATE);
            String token=preferences.getString("token","no existe we");
           // reference = FirebaseDatabase.getInstance().getReference("TokensProfesores").child(idprofesor);
          //  reference.child("tokenprofesor").setValue(token);
            userDatabaseReference.child(UID).child("token").setValue(token);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            userDatabaseReference.child(UID).child("active_now").setValue("true");


            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Correo no verificado", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }


    private void inrolvide() {

        startActivity(new Intent(LoginActivity.this, OlvideClaveActivity.class));

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
        Toast.makeText(this, "lat :"+String.valueOf(loc.getLatitude()) +" lo: "+String.valueOf(loc.getLongitude()), Toast.LENGTH_SHORT).show();
        // tvLatitud.setText(String.valueOf(loc.getLatitude()));
        //tvLongitud.setText(String.valueOf(loc.getLongitude()));


    }

    public void GuardarTokenmensaje(final String idprofesor){
        SharedPreferences preferences= getSharedPreferences("mitoken", Context.MODE_PRIVATE);
        String token=preferences.getString("token","no existe we");
        reference = FirebaseDatabase.getInstance().getReference("TokensProfesores").child(idprofesor);
        reference.child("tokenprofesor").setValue(token);

    }
    private void cargarcontroles() {

        et_correo=(EditText)findViewById(R.id.inputEmail);
        et_clave=(EditText)findViewById(R.id.inputPassword);
        btningresar=(Button)findViewById(R.id.loginButton);
        txtolvide=(TextView)findViewById(R.id.linkForgotPassword);

    }

}
