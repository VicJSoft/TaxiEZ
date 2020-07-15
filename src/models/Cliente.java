package models;

import models.interfaces.Registro;

public class Cliente extends Persona<Cliente> implements Registro {

    private int idCliente;
    private String numero;//primaryKey
    private boolean visible;


    public Cliente(int idCliente,String numero, boolean visible,String nombre, String observaciones, Direccion direccion) {
        super(nombre, observaciones, direccion);
        this.idCliente = idCliente;
        this.numero = numero;
        this.visible = visible;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
