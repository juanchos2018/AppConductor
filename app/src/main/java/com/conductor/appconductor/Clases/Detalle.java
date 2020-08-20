package com.conductor.appconductor.Clases;

public class Detalle {

    String id_cliente;
    int cantidad;
    String nombre_producto;
    int precio;
    String ruta_foto;

    public  Detalle(){

    }

    public Detalle(String id_cliente, int cantidad, String nombre_producto, int precio, String ruta_foto) {
        this.id_cliente = id_cliente;
        this.cantidad = cantidad;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.ruta_foto = ruta_foto;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }
}
