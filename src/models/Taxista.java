package models;

import models.interfaces.Registro;

import java.time.LocalDate;

public class Taxista extends Persona<Taxista> implements Registro {

    private int idTaxista;
    private LocalDate fechaNac;
    private String telefono;

    public Taxista(int idTaxista, LocalDate fechaNac, String telefono,String nombre, String observaciones, Direccion direccion) {
        super(nombre, observaciones, direccion);
        this.idTaxista = idTaxista;
        this.fechaNac = fechaNac;
        this.telefono = telefono;
    }

    public int getIdTaxista() {
        return idTaxista;
    }

    public void setIdTaxista(int idTaxista) {
        this.idTaxista = idTaxista;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    @Override
    public String toString() {
        return idTaxista + " " + this.getNombre();
    }
}
