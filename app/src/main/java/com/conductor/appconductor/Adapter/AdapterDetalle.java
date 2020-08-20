package com.conductor.appconductor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.conductor.appconductor.Clases.Detalle;
import com.conductor.appconductor.R;

import java.util.ArrayList;

public class AdapterDetalle extends RecyclerView.Adapter<AdapterDetalle.ViewHolderDatos>  {


    ArrayList<Detalle> listaDetalle;

    public AdapterDetalle(ArrayList<Detalle> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

Context context;


    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle,parent,false);
        //  vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){

            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.nombre.setText(listaDetalle.get(position).getNombre_producto());
            datgolder.cantidad.setText(String.valueOf( listaDetalle.get(position).getCantidad()));

            Glide.with(datgolder.img.getContext())
                    .load(listaDetalle.get(position).getRuta_foto())
                    .placeholder(R.drawable.default_profile_image)
                    .fitCenter()
                    .centerCrop()
                    .into(datgolder.img);



        }
    }

    @Override
    public int getItemCount() {
        return listaDetalle.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombre,cantidad;
        ImageView img;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            nombre=(TextView)itemView.findViewById(R.id.txtproducto);
            cantidad=(TextView)itemView.findViewById(R.id.txtcantidad);
            img=(ImageView)itemView.findViewById(R.id.imgproducto);
        }
    }
}
