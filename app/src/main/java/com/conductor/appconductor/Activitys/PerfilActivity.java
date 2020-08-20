package com.conductor.appconductor.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.conductor.appconductor.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PerfilActivity extends AppCompatActivity {

    CardView careditar;
    ImageView img2;
    private FirebaseAuth mAuth;
   // private DatabaseReference userDatabaseReference;

    private DatabaseReference referenceprofesor;
    public FirebaseUser currentUser;
    String urlimageperfil;
    private StorageReference storageReference;
    private StorageReference thumb_image_ref;
    private static final int COD_FOTO = 20;
    private final int MIS_PERMISOS = 100;
    EditText etnombreprofesor,etapellidoprofeseor,etnumeroprofesor;
    Button btnregistrrar,btnabrigaleria;
    ImageView imgfoto;
    Uri uri;
    private StorageReference mStorageRef;
    private final int frames = 9;
    private int currentAnimationFrame = 0;
    private final int PICKER=1;
    private static final int COD_SELECCIONA = 10;
    Bitmap thumb_Bitmap = null;
    private ProgressDialog progressDialog;
    ImageView getImg2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        btnabrigaleria=(Button)findViewById(R.id.idbtnfotoperfil);
        btnregistrrar=(Button)findViewById(R.id.idbotonregistrardatos);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgfoto=(ImageView) findViewById(R.id.idimgfotoperfil);
        if(solicitaPermisosVersionesSuperiores()){
            btnabrigaleria.setEnabled(true);
        }else{
            btnabrigaleria.setEnabled(false);
        }


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String user_id = mAuth.getCurrentUser().getUid();
        referenceprofesor = FirebaseDatabase.getInstance().getReference().child("Conductores").child(user_id);
        referenceprofesor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String img_usuario = dataSnapshot.child("rutafoto_conductor").getValue().toString();
               // String nombre = dataSnapshot.child("nombre_usuario").getValue().toString();
               // String apellido = dataSnapshot.child("apellido_usuario").getValue().toString();

               // txtnombre.setText(nombre);
                //txtapellido.setText(apellido);

                if (img_usuario.equals("default_imagen")){
                    imgfoto.setImageResource(R.drawable.default_profile_image);

                }
                else{
                    Glide.with(getApplicationContext())
                            .load(img_usuario)
                            .placeholder(R.drawable.default_profile_image)
                            .fitCenter()
                            .centerCrop()
                            .error(R.drawable.default_profile_image)
                            .into(imgfoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnabrigaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);// 10
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //TODO : ESTO SELECCIONA DE LA GALERIA
            case COD_SELECCIONA:
                // Uri miPath=data.getData();

                if (data==null){
                    Toast.makeText(this, "No selecciono una imagen", Toast.LENGTH_SHORT).show();
                    return;
                }

                uri=data.getData();
                //  txtrutasubio.setText(data.getDataString());
                imgfoto.setImageURI(uri);

                try {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Subiendo..");
                    progressDialog.setProgress(0);
                    progressDialog.show();
                    progressDialog.setCancelable(false);

                    final StorageReference mountainsRef=mStorageRef.child("FotosConductor").child(uri.getLastPathSegment());
                    imgfoto.setDrawingCacheEnabled(true);
                    imgfoto.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imgfoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] path = baos.toByteArray();
                    final UploadTask uploadTask = mountainsRef.putBytes(path);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return mountainsRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                       referenceprofesor.child("rutafoto_conductor").setValue(downloadUri.toString());

                                    } else {
                                        Toast.makeText(PerfilActivity.this, "Error al subir", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(currentprogress);
                            if (currentprogress==100){
                                progressDialog.dismiss();
                                Toast.makeText(PerfilActivity.this, "subido", Toast.LENGTH_SHORT).show();
                                String mensaje="Foto subida";
                             //   dialogomensaje(mensaje);
                            }

                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(this, "No existe la foto", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getBaseContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getBaseContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getBaseContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

}
