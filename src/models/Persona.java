package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Persona<S> extends RecursiveTreeObject<S> {

    private String nombre;
    private String observaciones;
    private Direccion direccion;


    public Persona(String nombre, String observaciones, Direccion direccion) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.direccion = direccion;
    }

    public Persona(Persona datos){
        this.nombre = datos.getNombre();
        this.observaciones = datos.getObservaciones();
        this.direccion = datos.getDireccion();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
