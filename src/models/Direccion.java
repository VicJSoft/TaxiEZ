package models;

import models.interfaces.Registro;

public class Direccion {

    private int idDireccion;
    private String calle;
    private String colonia;
    private String numInt;
    private String numExt;

    public Direccion(int idDireccion, String calle, String colonia, String numInt, String numExt) {
        this.idDireccion = idDireccion;
        this.calle = calle;
        this.colonia = colonia;
        this.numInt = numInt;
        this.numExt = numExt;
    }

    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getNumInt() {
        return numInt;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    /**
     *
     * @return Una instancia nueva con los mismo valores que esta.
     * @throws CloneNotSupportedException
     */
    @Override
    public Direccion clone() throws CloneNotSupportedException {

        Direccion direcionClonada =
                new Direccion(this.idDireccion, this.calle, this.colonia, this.numInt,this.numExt);
        return direcionClonada;
    }
}
