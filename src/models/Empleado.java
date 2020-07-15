package models;

import models.interfaces.Registro;

import java.time.LocalDate;

public class Empleado extends Persona<Empleado> implements Registro {

    private int idEmpleado;//primarykey
    private String telefono;
    private String contrasena;
    private LocalDate fechaNac;
    private boolean tipoEmpleado;

    public Empleado(String nombre, String observaciones, Direccion direccion, int idEmpleado, String telefono, String contrasena, LocalDate fechaNac, boolean tipoEmpleado) {
        super(nombre, observaciones, direccion);
        this.idEmpleado = idEmpleado;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.fechaNac = fechaNac;
        this.tipoEmpleado = tipoEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public boolean isTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(boolean tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }
}
