package com.conductor.appconductor.Clases;

public class MisEntregas {


    String paquete;
    double latitud;
    double longitud;
    String nombre_cliente;

    double latitud_conductor;
    double longitud_conductor;
    String telefono;

    public  MisEntregas(){

    }
    public MisEntregas(String paquete, double latitud, double longitud, String nombre_cliente,double latitud_conductor,double longitud_conductor) {
        this.paquete = paquete;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre_cliente = nombre_cliente;
        this.latitud_conductor=latitud_conductor;
        this.longitud_conductor=longitud_conductor;

    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public double getLatitud_conductor() {
        return latitud_conductor;
    }

    public void setLatitud_conductor(double latitud_conductor) {
        this.latitud_conductor = latitud_conductor;
    }

    public double getLongitud_conductor() {
        return longitud_conductor;
    }

    public void setLongitud_conductor(double longitud_conductor) {
        this.longitud_conductor = longitud_conductor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
