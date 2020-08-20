package com.conductor.appconductor.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.conductor.appconductor.Adapter.AdapterDetalle;
import com.conductor.appconductor.Clases.Detalle;
import com.conductor.appconductor.Clases.MisEntregas;
import com.conductor.appconductor.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EntregasActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    private DatabaseReference referenceentregas;
    private DatabaseReference referencedetalle;
    String user_id;


        private FirebaseAuth mAuth;
        private DatabaseReference userDatabaseReference;

        public FirebaseUser currentUser;

    android.app.AlertDialog.Builder builder1;
    AlertDialog alert;


    ArrayList<Detalle> listaSalones;
    AdapterDetalle adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);


        listaSalones=new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
      //  user_id="-MBCk207t_xaxJQGjvyF";
        final String  user_uID = mAuth.getCurrentUser().getUid();
        referenceentregas= FirebaseDatabase.getInstance().getReference("PedidosCliente").child(user_uID);
        recyclerView=findViewById(R.id.recyclerentregas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<MisEntregas> recyclerOptions = new FirebaseRecyclerOptions.Builder<MisEntregas>()
                .setQuery(referenceentregas, MisEntregas.class).build();
        FirebaseRecyclerAdapter<MisEntregas,Items> adapter =new FirebaseRecyclerAdapter<MisEntregas, Items>(recyclerOptions) {

            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull MisEntregas tutores) {
                final String key = getRef(i).getKey();
                referenceentregas.child(key).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            final String nombre=dataSnapshot.child("nombre_cliente").getValue().toString();
                            String idcli=dataSnapshot.child("id_cliente").getValue().toString();
                                    //id_clietnte
                            final String fecha=dataSnapshot.child("fecha").getValue().toString();
                            final String latitud=dataSnapshot.child("latitud").getValue().toString();
                            final String longitud=dataSnapshot.child("longitud").getValue().toString();
                            final String celular_cliente =dataSnapshot.child("celular_cliente").getValue().toString();
                          //  final String key=dataSnapshot.child("key_envio").getValue().toString();

                            items.id_clietnte=idcli;
                            items.txtcliente.setText(nombre);
                            items.txtfecha.setText(fecha);

                            items.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                 //  Toast.makeText(EntregasActivity.this, "para Ver Ubicacion", Toast.LENGTH_SHORT).show();
                                 //Intent intent =new Intent(EntregasActivity.this,MapaClienteActivity.class);
                                    Intent intent =new Intent(EntregasActivity.this,Mapa2Activity.class);
                                 Bundle bundle=new Bundle();
                                 bundle.putString("la",latitud);
                                 bundle.putString("lo",longitud);
                                 bundle.putString("key",key);
                                 bundle.putString("cel",celular_cliente);
                                 intent.putExtras(bundle);
                                  startActivity(intent);


                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(EntregasActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entregas, parent, false);
                return new Items(vista);

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public void abrirdetalle() {



    }

    public void dialogomensaje(String id_cliente){


            Toast.makeText(this, id_cliente, Toast.LENGTH_SHORT).show();
            builder1 = new AlertDialog.Builder(EntregasActivity.this);
            Button btcerrrar;
            TextView tvestado;
            builder1.setTitle("detalle");
            View v = LayoutInflater.from(EntregasActivity.this).inflate(R.layout.dialogo_detalle, null);
            final RecyclerView recyclerView2;
            recyclerView2=(RecyclerView)v.findViewById(R.id.recyclerDetalle);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            referencedetalle= FirebaseDatabase.getInstance().getReference("PedidosClienteDetalle").child(id_cliente);


            Query q=referencedetalle;
            q.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaSalones.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Detalle artist = postSnapshot.getValue(Detalle.class);

                        listaSalones.add(artist);
                    }

                    adapter = new AdapterDetalle(listaSalones);
                    recyclerView2.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            builder1.setView(v);


            alert  = builder1.create();
         //   alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            alert.show();




    }

    public   class Items extends RecyclerView.ViewHolder{
        TextView txtfecha,txtcliente;
        ImageView imgfoto;
        String id_clietnte;
        final Toolbar toolbarCard = (Toolbar)itemView.findViewById(R.id.idtolbar1);
        public Items(@NonNull View itemView) {
            super(itemView);
            txtcliente=(TextView)itemView.findViewById(R.id.lblExpenseCancel);
            txtfecha=(TextView)itemView.findViewById(R.id.txtfecha);
          //  txtcliente=(TextView)itemView.findViewById(R.id.txtestado);
            toolbarCard.inflateMenu(R.menu.menu_pedido);
            toolbarCard.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.idveProductos:
                        //    Toast.makeText(toolbarCard.getContext(), "Para ver Detalle", Toast.LENGTH_SHORT).show();

                            if (id_clietnte=="" || id_clietnte ==null){
                                id_clietnte="1";
                            }
                            dialogomensaje(id_clietnte);
                            
                            break;
                    }
                    return true;
                }
            });


        }


    }
}
