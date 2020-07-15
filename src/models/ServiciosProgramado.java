package models;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import resources.Statics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ServiciosProgramado extends Servicio<ServiciosProgramado> {

    private LocalDateTime fechaUltimaAplicacion;
    private boolean isLunes;
    private boolean isMartes;
    private boolean isMiercoles;
    private boolean isJueves;
    private boolean isViernes;
    private boolean isSabado;
    private boolean isDomingo;

    private String telefonoAux;

    public ServiciosProgramado(String nombre, String observaciones, Direccion direccion, int idServicio,
                               LocalDateTime fechaAgregacion, LocalDateTime fechaServicio, LocalDateTime fechaAplcacion,
                               boolean isCancelado, Cliente cliente, Empleado empleado, LocalDateTime fechaUltimaAplicacion,
                               boolean isLunes, boolean isMartes, boolean isMiercoles, boolean isJueves, boolean isViernes, boolean isSabado, boolean isDomingo) {
        super(nombre, observaciones, direccion, idServicio, fechaAgregacion, fechaServicio, fechaAplcacion, isCancelado, cliente, empleado);
        this.fechaUltimaAplicacion = fechaUltimaAplicacion;
        this.isLunes = isLunes;
        this.isMartes = isMartes;
        this.isMiercoles = isMiercoles;
        this.isJueves = isJueves;
        this.isViernes = isViernes;
        this.isSabado = isSabado;
        this.isDomingo = isDomingo;
    }

    public ServiciosProgramado(Persona datos, int idServicio,
                               LocalDateTime fechaAgregacion, LocalDateTime fechaServicio, LocalDateTime fechaAplcacion,
                               boolean isCancelado, Cliente cliente, Empleado empleado, LocalDateTime fechaUltimaAplicacion,
                               boolean isLunes, boolean isMartes, boolean isMiercoles, boolean isJueves, boolean isViernes, boolean isSabado, boolean isDomingo) {
        super(datos, idServicio, fechaAgregacion, fechaServicio, fechaAplcacion, isCancelado, cliente, empleado);
        this.fechaUltimaAplicacion = fechaUltimaAplicacion;
        this.isLunes = isLunes;
        this.isMartes = isMartes;
        this.isMiercoles = isMiercoles;
        this.isJueves = isJueves;
        this.isViernes = isViernes;
        this.isSabado = isSabado;
        this.isDomingo = isDomingo;
    }

    public void setFechaInicio(LocalDateTime fechaInicio){
        setFechaServicio(fechaInicio);
    }
    public void setFechaFin(LocalDateTime fechaFin){
        setFechaAplicacion(fechaFin);
    }

    public LocalDateTime getFechaInicio(){
        return getFechaServicio();
    }
    public LocalDateTime getFechaFin( ){
        return getFechaAplicacion();
    }


    public LocalDateTime getFechaUltimaAplicacion() {
        return fechaUltimaAplicacion;
    }

    public void setFechaUltimaAplicacion(LocalDateTime fechaUltimaAplicacion) {
        this.fechaUltimaAplicacion = fechaUltimaAplicacion;
    }





    public boolean isLunes() {
        return isLunes;
    }

    public void setLunes(boolean lunes) {
        isLunes = lunes;
    }

    public boolean isMartes() {
        return isMartes;
    }

    public void setMartes(boolean martes) {
        isMartes = martes;
    }

    public boolean isMiercoles() {
        return isMiercoles;
    }

    public void setMiercoles(boolean miercoles) {
        isMiercoles = miercoles;
    }

    public boolean isJueves() {
        return isJueves;
    }

    public void setJueves(boolean jueves) {
        isJueves = jueves;
    }

    public boolean isViernes() {
        return isViernes;
    }

    public void setViernes(boolean viernes) {
        isViernes = viernes;
    }

    public boolean isSabado() {
        return isSabado;
    }

    public void setSabado(boolean sabado) {
        isSabado = sabado;
    }

    public boolean isDomingo() {
        return isDomingo;
    }

    public void setDomingo(boolean domingo) {
        isDomingo = domingo;
    }

    public String getTelefonoAux() {
        return telefonoAux;
    }

    public void setTelefonoAux(String telefonoAux) {
        this.telefonoAux = telefonoAux;
    }

    private ArrayList<Label> getDiasSeleccionados(){
        ArrayList<Label> listaDias = new ArrayList<>();

        listaDias.add(new Label("Lu"));
        listaDias.add(new Label("Ma"));
        listaDias.add(new Label("Mi"));
        listaDias.add(new Label("Ju"));
        listaDias.add(new Label("Vi"));
        listaDias.add(new Label("Sa"));
        listaDias.add(new Label("Do"));

        if(isLunes)
            listaDias.get(0).setTextFill(Paint.valueOf("5AB444"));
        if(isMartes)
            listaDias.get(1).setTextFill(Paint.valueOf("5AB444"));
        if(isMiercoles)
            listaDias.get(2).setTextFill(Paint.valueOf("5AB444"));
        if(isJueves)
            listaDias.get(3).setTextFill(Paint.valueOf("5AB444"));
        if(isViernes)
            listaDias.get(4).setTextFill(Paint.valueOf("5AB444"));
        if(isSabado)
            listaDias.get(5).setTextFill(Paint.valueOf("5AB444"));
        if(isDomingo)
            listaDias.get(6).setTextFill(Paint.valueOf("5AB444"));

        return listaDias;

    }


    public HBox getDiasSeleccion() {

        HBox diasSeleccion = new HBox();

       diasSeleccion.getChildren().addAll(getDiasSeleccionados());
       diasSeleccion.setAlignment(Pos.CENTER);
       diasSeleccion.setSpacing(2);

        return diasSeleccion;
    }


    public ServicioRegular generarServicioRegular(){
        ServicioRegular servicioRegular =
                new ServicioRegular(this.getNombre(),this.getObservaciones(),this.getDireccion(),0,
                        LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now(),
                        false,this.getCliente(), Statics.empleadoSesionActual);

        return  servicioRegular;
    }

}
