package services.sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import models.Direccion;
import models.Empleado;
import resources.Statics;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpleadoSQL {

    private Connection connection;

    private boolean key;
    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private ActionEvent event;

    public EmpleadoSQL() {

        connection = Statics.getConnections();


    }


    /**
     * La insersión puede ser correcta o no, ya que el usuario puede cancelarla, pero también puede haber
     * error SQL que va al Logger.
     * @param empleado
     * El empleado a insertar, este puede tener o no un numero de telefono que ya exista en la bse de datos.
     * @return
     * True si se insertó correctamente.
     * False si no se insertó.
     *
     */
    public boolean insertar(Empleado empleado)  {

        if(!new DireccionSQL().insertarDireccion(empleado.getDireccion())){
            return false;
        }
        try{
            query = "INSERT INTO empleado (nombre, fechaNac, telefono, tipoEmpleado, observaciones, contrasena, idDireccion) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(query);
            ps.setString(1, empleado.getNombre());
            ps.setDate(2, Date.valueOf( empleado.getFechaNac()));
            ps.setString(3, empleado.getTelefono());
            ps.setBoolean(4,empleado.isTipoEmpleado());
            ps.setString(5,empleado.getObservaciones());
            ps.setString(6,empleado.getContrasena());
            ps.setInt(7,empleado.getDireccion().getIdDireccion());

            ps.executeUpdate();
            empleado.setIdEmpleado(Statics.getLastId());
                return true;


        }
        catch (SQLException sqlE){
            sqlE.printStackTrace();
            //ventana error v:
        }

        return false;

    }
    public boolean actualizar(Empleado empleado)  {
        //si existe entonces se "borrará" (ocultará) el registro(s) que contenga a ese numero de telefono.
        if(!new DireccionSQL().actualizar(empleado.getDireccion())){
            return false;
        }
        try {
            query = "UPDATE empleado SET nombre = ?,fechaNac =?,telefono = ?,tipoEmpleado = ?,observaciones = ?,  contrasena = ? WHERE empleado.idEmpleado = ?";
            ps = connection.prepareStatement(query);


            ps.setString(1, empleado.getNombre());
            ps.setDate(2, Date.valueOf(empleado.getFechaNac()));
            ps.setString(3, empleado.getTelefono());
            ps.setBoolean(4, empleado.isTipoEmpleado());
            ps.setString(5, empleado.getObservaciones());
            ps.setString(6, empleado.getContrasena());
            ps.setInt(7,empleado.getIdEmpleado());

            ps.executeUpdate();
            return true;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Buscará ese registro para saber si existe en la bse de datos.
     *
     * @param empleado
     * Registro a buscar.
     * @return
     * True si existe.
     * False si no.
     * @throws SQLException
     */
    public boolean existe(Empleado empleado) throws SQLException {
        boolean existe = false;

        query = "SELECT * FROM empleado WHERE idEmpleado = ? AND visible = 1";

        ps = connection.prepareStatement(query);
        ps.setInt(1, empleado.getIdEmpleado());

        //0 no existe, mayor qué 0 existe
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()){
            existe = true;

        }


        return existe;

    }
    /**
     * Buscará ese registro para saber si existe en la bse de datos.
     *
     * @param usuario
     * Nombre del empleado.
     * @param contrasena
     * Contraseña del empleado
     * @return
     * Instancia si existe
     * False si no.
     * @throws SQLException
     */
    public Empleado existe(String usuario,String contrasena) throws SQLException {
        boolean existe = false;

        query = "SELECT * FROM empleado JOIN direccion on empleado.idDireccion = direccion.idDireccion WHERE nombre = ? AND contrasena = ? AND visible = 1";

        ps = connection.prepareStatement(query);
        ps.setString(1,usuario);
        ps.setString(2,contrasena);

        //0 no existe, mayor qué 0 existe
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()){
            return crearEmpleado(resultSet);

        }

        return null;
    }



    public boolean eliminar(Empleado empleado){
        //eliminar todos los que tengan ese telefono
        query= "UPDATE empleado SET visible = 0 WHERE empleado.idEmpleado = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, empleado.getIdEmpleado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return
     * Lista de los clientes existentes actualmente en la base de datos.
     */
    public ObservableList<Empleado> getEmpleados()
    {
        ObservableList<Empleado> empleados =  FXCollections.observableArrayList();
        query="SELECT * FROM empleado JOIN direccion on empleado.idDireccion = direccion.idDireccion WHERE visible = 1";
        Direccion direccion;
        try
        {
            ps = connection.prepareStatement(query);
            rs=ps.executeQuery();
            while(rs.next())
            {
                empleados.add( crearEmpleado(rs) );
            }

            ps.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(ClienteSQL.class.getName()).log(Level.SEVERE, "Error al extraer clientes.", ex);
        }

        return empleados;
    }

    /**
     * @param rs tupla Cliente.
     * La tupla obtenia es:
     * idEmpleado nombre fechaNac telefono tipoEmpleado observaciones contrasena idDireccion visible
     * idDireccion calle colonia numInt numExt.
     * @return Instancia de Cliente a partir de la tupla contenida en rs.
     * @throws SQLException
     */
    private Empleado crearEmpleado(ResultSet rs) throws SQLException {
        //Cliente cliente = new Cliente(rs.getString(0), rs.getBoolean(3), rs.getString(1), rs.getString(2), );

        Direccion direcion  = new Direccion(
                rs.getInt(10),
                rs.getString(11),
                rs.getString(12),
                rs.getString(13),
                rs.getString(14)
        );

        Empleado empleado = new Empleado(
                rs.getString(2),
                rs.getString(6),
                direcion,
                rs.getInt(1),
                rs.getString(4),
                rs.getString(7),
                rs.getDate(3) ==null?null:rs.getDate(3).toLocalDate(),
                rs.getBoolean(5)
        );

        return empleado;
    }


    /**
     * Consigue el empleado según el idIndicado.
     * @param idEmpleado
     * Id del empleado a buscarr.
     * @return
     * Instancia con los datos que contiene la base de datos.
     * Null si no existe ese indice.
     * @throws SQLException
     */
    public Empleado get(int idEmpleado) throws SQLException {
        query = "SELECT * FROM empleado JOIN direccion ON empleado.idDireccion = direccion.idDireccion WHERE idEmpleado = ? AND visible = 1";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,idEmpleado);

        ResultSet resultSet = ps.executeQuery();

        if(resultSet.first()){
            return crearEmpleado(resultSet);
        }
        return null;
    }


}
