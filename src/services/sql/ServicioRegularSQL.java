package services.sql;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import models.*;
import resources.Statics;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioRegularSQL {


    private Connection connection;

    private boolean key;
    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private ActionEvent event;


    public ServicioRegularSQL() {
        this.connection = Statics.getConnections();
    }



    @Deprecated
    /**
     * Se reempleaza por {@link getServiciosRegularesPendientes2}
     */
    public ObservableList<ServicioRegular> getServiciosRegularesPendientes(){
        ObservableList<ServicioRegular> serviciosRegulares =  FXCollections.observableArrayList();

       query = "SELECT * FROM " +
               "servicio " +
               "JOIN cliente ON servicio.idCliente = cliente.idCliente " +
               "JOIN empleado  on servicio.idEmpleado = empleado.idEmpleado " +
               "JOIN direccion ON servicio.idDireccion = direccion.idDireccion " +
               "WHERE servicio.isCancelado = 0 and servicio.fechaAplicacion IS NULL " +
               "LIMIT 0, 250";
        try
        {
            ps = connection.prepareStatement(query);
            rs=ps.executeQuery();
            while(rs.next())
            {
                serviciosRegulares.add( crearServicioPendiente(rs) );
            }

            ps.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(ClienteSQL.class.getName()).log(Level.SEVERE, "Error al extraer clientes.", ex);
        }

        return serviciosRegulares;
    }

    public ObservableList<ServicioRegular> getServiciosRegularesPendientes2() throws SQLException {
        ObservableList<ServicioRegular> serviciosRegularesPendientes =  FXCollections.observableArrayList();

        query ="SELECT * FROM servicio  " +
                "WHERE servicio.isCancelado = 0 and servicio.fechaAplicacion IS NULL " +
                " LIMIT 0,300 ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){

            Cliente clienteDelServicio = new ClienteSQL().get(rs.getInt("idCliente"));
            Direccion direccionDelServicio = new DireccionSQL().get(rs.getInt("idDireccion"));
            Empleado empleadoRegistroServicio = new  EmpleadoSQL().get(rs.getInt("idEmpleado"));
          //  Taxi taxisDelServicio = new TaxisSQL().get(rs.getInt("idUnidad"));

            Persona datosServicio = new Persona(rs.getString("servicio.nombre"),rs.getString("servicio.observaciones"),direccionDelServicio);


            LocalDateTime localDateTimeAgregacion = rs.getTimestamp("servicio.fechaAgregacion").toLocalDateTime();
            LocalDateTime localDateTimeServicio = rs.getTimestamp("servicio.fechaServicio").toLocalDateTime();

            //en un servicio pendiente siempre será null
            LocalDateTime localDateTimeAplicacion =
                    rs.getTimestamp("servicio.fechaAplicacion") ==null?
                            null:rs.getTimestamp("servicio.fechaAplicacion").toLocalDateTime();

            ServicioRegular SRAplicado =
                    new ServicioRegular(datosServicio, rs.getInt("servicio.idServicio"),
                            localDateTimeAgregacion, localDateTimeServicio, localDateTimeAplicacion,
                            rs.getBoolean("servicio.isCancelado"), clienteDelServicio, empleadoRegistroServicio);

           // SRAplicado.setTaxi(taxisDelServicio);

            serviciosRegularesPendientes.add(SRAplicado);
        }

        return serviciosRegularesPendientes;

    }

    /**
     * Obtiene los servicios marcados como aplicados.
     * La instancia a cada ServicioRegular se crea dentro de esta funcioón (no hace llamado a otro método de esta clase).
     * @return
     * @throws SQLException
     */
    public ObservableList<ServicioRegular> getServiciosRegularesAplicadosyCancelados() throws SQLException {
        ObservableList<ServicioRegular> serviciosRegularesAplicados =  FXCollections.observableArrayList();

        query ="SELECT * FROM servicio_has_unidad " +
                " JOIN servicio ON servicio_has_unidad.idServicio = servicio.idServicio " +
                " JOIN unidad ON servicio_has_unidad.idUnidad = unidad.idUnidad " + //este join se puede quitar para que se llame al new TaxisSQL.get.
                " LIMIT 0,300 ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){

            Cliente clienteDelServicio = new ClienteSQL().get(rs.getInt("idCliente"));
            Direccion direccionDelServicio = new DireccionSQL().get(rs.getInt("idDireccion"));
            Empleado empleadoRegistroServicio = new  EmpleadoSQL().get(rs.getInt("idEmpleado"));
            Taxi taxisDelServicio = new TaxisSQL().get(rs.getInt("idUnidad"));

            Persona datosServicio = new Persona(rs.getString("servicio.nombre"),rs.getString("servicio.observaciones"),direccionDelServicio);


            LocalDateTime localDateTimeAgregacion = rs.getTimestamp("servicio.fechaAgregacion").toLocalDateTime();
            LocalDateTime localDateTimeServicio = rs.getTimestamp("servicio.fechaServicio").toLocalDateTime();

            LocalDateTime localDateTimeAplicacion =
                    rs.getTimestamp("servicio.fechaAplicacion") ==null?
                            null:rs.getTimestamp("servicio.fechaAplicacion").toLocalDateTime();

            ServicioRegular SRAplicado =
                    new ServicioRegular(datosServicio, rs.getInt("servicio.idServicio"),
                            localDateTimeAgregacion, localDateTimeServicio, localDateTimeAplicacion,
                            rs.getBoolean("servicio.isCancelado"), clienteDelServicio, empleadoRegistroServicio);

            SRAplicado.setTaxi(taxisDelServicio);

            serviciosRegularesAplicados.add(SRAplicado);
        }

        return serviciosRegularesAplicados;

    }


    /**
     * Inserta un @{@link ServicioRegular} "pendiente" es decir, con la fecha de aplicacion nula y el campo isCancelado como false.
     * Esta instancia es creada dentro del programa por lo tanto el idServicio no se toma en cuenta en la inserción (campo AutoIncrement) posteriormente
     * de la insersión se modifica el idServicio de la instancia.
     * Los miembros internos como Direccion,Cliente,Empleado ya deben tener ID (la instancia) y deben existir en la DB.
     * @param servicioRegular
     * Instancia a insertar en la base de datos.
     * @return
     * True si fue exitosa la inserción.
     * False si lo contrario.
     */
    public boolean insertarServicioRegular(ServicioRegular servicioRegular) {

        query = "INSERT INTO servicio " +
                "(nombre, observaciones, fechaAgregacion, fechaServicio, fechaAplicacion, isCancelado, idCliente, idEmpleado, idDireccion) " +
                "VALUES" +
                " (?,?,?,?, ?, ?, ?, ?, ?)";

        try {

            ps = connection.prepareStatement(query);
            ps.setString(1, servicioRegular.getNombre());
            ps.setString(2, servicioRegular.getObservaciones());
            ps.setTimestamp(3, Timestamp.valueOf( servicioRegular.getFechaAgregacion()));
            ps.setTimestamp(4, Timestamp.valueOf( servicioRegular.getFechaServicio()));
            ps.setTimestamp(5, null);//fecha aplicacion nula para servicio pendiente

            ps.setBoolean(6, false);
            ps.setInt(7,servicioRegular.getCliente().getIdCliente());
            ps.setInt(8,servicioRegular.getEmpleado().getIdEmpleado());
            ps.setInt(9,servicioRegular.getDireccion().getIdDireccion());


            ps.executeUpdate();

            servicioRegular.setIdServicio(Statics.getLastId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean cancelarServicioPendiente(int idServicioRegularPendiente) throws SQLException {

        query = ("UPDATE servicio SET isCancelado = 1 WHERE servicio.idServicio = ?");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,idServicioRegularPendiente);

        return preparedStatement.executeUpdate() == 1;
    }

    /**
     * idServicio nombre observaciones fechaAgregacion fechaServicio fechaAplicacion isCancelado idCliente idEmpleado idDireccion (La instancia )
     * idDireccion calle colonia numInt numExt
     * cliente.telefono
     * @param rs
     * @return
     */
    private ServicioRegular crearServicioPendiente(ResultSet rs) throws SQLException {

        Direccion direccion =
                new Direccion(rs.getInt("direccion.idDireccion"), rs.getString("direccion.calle"),rs.getString("direccion.colonia"), rs.getString("direccion.numInt"), rs.getString("direccion.numExt"));

        Persona datos = new Persona(rs.getString("servicio.nombre"),rs.getString("servicio.observaciones"),direccion);



        LocalDateTime localDateTimeAgregacion = rs.getTimestamp("servicio.fechaAgregacion").toLocalDateTime();
        LocalDateTime localDateTimeServicio = rs.getTimestamp("servicio.fechaServicio").toLocalDateTime();


        LocalDateTime localDateTimeAplicacion =
                rs.getTimestamp("fechaAplicacion") ==null?
                        null:rs.getTimestamp("fechaAplicacion").toLocalDateTime();


        Cliente cliente = new Cliente(rs.getInt("cliente.idCliente"),rs.getString("cliente.telefono"),rs.getBoolean("Visible"), rs.getString("cliente.nombre"),
                rs.getString("observaciones"), new DireccionSQL().get(rs.getInt("cliente.idDireccion")));

        Empleado empleado = new Empleado(
                rs.getString("empleado.nombre"),rs.getString("empleado.observaciones"),
                new  DireccionSQL().get(rs.getInt("empleado.idDireccion")),
                rs.getInt("empleado.idEmpleado"), rs.getString("empleado.telefono"), rs.getString("contrasena"),
                rs.getDate("fechaNac")==null?null:rs.getDate("fechaNac").toLocalDate(), rs.getBoolean("tipoEmpleado"));

        ServicioRegular servicioRegular =
            new ServicioRegular(
                    datos,
                    rs.getInt("servicio.idServicio"),localDateTimeAgregacion, localDateTimeServicio, localDateTimeAplicacion,
                    rs.getBoolean("servicio.isCancelado"),cliente,empleado);


        return servicioRegular;

    }/**
     * idServicio nombre observaciones fechaAgregacion fechaServicio fechaAplicacion isCancelado idCliente idEmpleado idDireccion (La instancia )
     * idDireccion calle colonia numInt numExt
     * cliente.telefono
     * @param rs
     * @return
     */
    private ServicioRegular crearServicioAplicado(ResultSet rs) throws SQLException {

        Direccion direccion =
                new Direccion(rs.getInt("direccion.idDireccion"), rs.getString("direccion.calle"),rs.getString("direccion.colonia"), rs.getString("direccion.numInt"), rs.getString("direccion.numExt"));

        Persona datos = new Persona(rs.getString("servicio.nombre"),rs.getString("servicio.observaciones"),direccion);



        LocalDateTime localDateTimeAgregacion = rs.getTimestamp("servicio.fechaAgregacion").toLocalDateTime();
        LocalDateTime localDateTimeServicio = rs.getTimestamp("servicio.fechaServicio").toLocalDateTime();


        LocalDateTime localDateTimeAplicacion =
                rs.getTimestamp("fechaAplicacion") ==null?
                        null:rs.getTimestamp("fechaAplicacion").toLocalDateTime();


        Cliente cliente = new Cliente(rs.getInt("cliente.idCliente"),rs.getString("cliente.telefono"),rs.getBoolean("Visible"), rs.getString("cliente.nombre"),
                rs.getString("observaciones"), new DireccionSQL().get(rs.getInt("cliente.idDireccion")));

        Empleado empleado = new Empleado(
                rs.getString("empleado.nombre"),rs.getString("empleado.observaciones"),
                new  DireccionSQL().get(rs.getInt("empleado.idDireccion")),
                rs.getInt("empleado.idEmpleado"), rs.getString("empleado.telefono"), rs.getString("contrasena"),
                rs.getDate("fechaNac")==null?null:rs.getDate("fechaNac").toLocalDate(), rs.getBoolean("tipoEmpleado"));

        ServicioRegular servicioRegular =
            new ServicioRegular(
                    datos,
                    rs.getInt("servicio.idServicio"),localDateTimeAgregacion, localDateTimeServicio, localDateTimeAplicacion,
                    rs.getBoolean("servicio.isCancelado"),cliente,empleado);


        return servicioRegular;

    }


    public boolean aplicarServicioRegular(ServicioRegular servicioRegularAplicado) throws SQLException {

        LocalDateTime horaAplicacion = LocalDateTime.now();

        query = "UPDATE servicio SET fechaAplicacion = ? WHERE idServicio = ? ";
        ps = connection.prepareStatement(query);
        ps.setTimestamp(1,Timestamp.valueOf(horaAplicacion) );
        ps.setInt(2, servicioRegularAplicado.getIdServicio());
        ps.executeUpdate();

        servicioRegularAplicado.setFechaAplicacion(horaAplicacion);

        query = "INSERT INTO servicio_has_unidad (idServicio,idUnidad) VALUES (?,?) ";
        ps = connection.prepareStatement(query);
        ps.setInt(1, servicioRegularAplicado.getIdServicio());
        ps.setInt(2, servicioRegularAplicado.getTaxi().getIdUnidad());
        ps.executeUpdate();



        return true;
    }


    /**
     * Obtiene los servicios marcados como aplicados.
     * La instancia a cada ServicioRegular se crea dentro de esta funcioón (no hace llamado a otro método de esta clase).
     * @return
     * @throws SQLException
     */
    public int getServiciosAplicadosSegunFiltro(@NotNull String columnaCondicion, @NotNull int valorCondicion, @NotNull LocalDate diaBusqueda) throws SQLException {

        ObservableList<ServicioRegular> serviciosRegularesAplicados =  FXCollections.observableArrayList();

        query ="SELECT COUNT(*)" +
                " FROM servicio_has_unidad " +
                " JOIN servicio ON servicio_has_unidad.idServicio = servicio.idServicio " +
                " JOIN unidad ON servicio_has_unidad.idUnidad = unidad.idUnidad " + //este join se puede quitar para que se llame al new TaxisSQL.get.
                " WHERE " + columnaCondicion + " = " + valorCondicion +
                " AND " + " servicio.fechaAplicacion BETWEEN ? AND ?";



        LocalDateTime inicioDay =LocalDateTime.of(diaBusqueda, LocalTime.of(0, 0, 0)) ;
        LocalDateTime finDay = LocalDateTime.of(diaBusqueda, LocalTime.of(23, 59, 59));


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setTimestamp(1, Timestamp.valueOf(inicioDay));
        preparedStatement.setTimestamp(2, Timestamp.valueOf(finDay));
        ResultSet rs = preparedStatement.executeQuery();

        int count = 0;

        if(rs.first()){
            count = rs.getInt(1);
        }

        return count;

    }


}
