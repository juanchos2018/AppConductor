package com.conductor.appconductor.Clases;

public class Cambio {

    String compra;
    String venta;

    public  Cambio(){

    }

    public Cambio(String compra, String venta) {
        this.compra = compra;
        this.venta = venta;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    public String getVenta() {
        return venta;
    }

    public void setVenta(String venta) {
        this.venta = venta;
    }
}
