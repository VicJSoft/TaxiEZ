package models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import models.interfaces.Registro;

public class Taxi extends RecursiveTreeObject<Taxi> implements Registro {

    private int idUnidad;//primaryKey
    private String marca;
    private String modelo;
    private String placa;
    private Taxista taxista;//actuará como foreign key :

    public Taxi(int idUnidad, String marca, String modelo, String placa, Taxista taxista) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.taxista = taxista;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Taxista getTaxista() {
        return taxista;
    }

    public void setTaxista(Taxista taxista) {
        this.taxista = taxista;
    }
/*
    @Override
    public String toString() {
        return idUnidad + " " + taxista.getNombre();
    }*/
}
