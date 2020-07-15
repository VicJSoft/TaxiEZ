package services.sql;

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

public class ServicioProgramadoSQL {


    private Connection connection;

    private boolean key;
    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private ActionEvent event;


    public ServicioProgramadoSQL() {
        this.connection = Statics.getConnections();
    }

    @Deprecated
    /**
     * Borrar. Creo no lo necesito.
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


    /**
     * idServicioProgramado	nombre	observaciones	fechaAdicion	fechaInicio	fechaFinalizacion	fechaUltimoRegistro
     * lunes	martes	miercoles	jueves	viernes	sabado	domingo	idEmpleado	idCliente	idDireccion.
     *
     * Obtiene los ultimos 500 servicios programados ó 500 servicios programados activos.
     * @return
     * @throws SQLException
     */
    public ObservableList<ServiciosProgramado> getServiciosProgramados() throws SQLException {

        ObservableList<ServiciosProgramado> serviciosRegularesPendientes =  FXCollections.observableArrayList();

        query ="SELECT * FROM servicioprogramado  " +
                //"WHERE servicio.isCancelado = 0 and servicio.fechaAplicacion IS NULL " +
                " LIMIT 0,500 ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        while(rs.next()){

            Cliente clienteDelServicio = new ClienteSQL().get(rs.getInt("servicioprogramado.idCliente"));
            Direccion direccionDelServicio = new DireccionSQL().get(rs.getInt("servicioprogramado.idDireccion"));
            Empleado empleadoRegistroServicio = new  EmpleadoSQL().get(rs.getInt("servicioprogramado.idEmpleado"));
          //  Taxi taxisDelServicio = new TaxisSQL().get(rs.getInt("idUnidad"));

            Persona datosServicio = new Persona(rs.getString("servicioprogramado.nombre"),rs.getString("servicioprogramado.observaciones"),direccionDelServicio);


            LocalDateTime localDateTimeAdicion = rs.getTimestamp("servicioprogramado.fechaAdicion").toLocalDateTime();
            LocalDateTime localDateTimeInicio = rs.getTimestamp("servicioprogramado.fechaInicio").toLocalDateTime();

            //puede ser null.
            LocalDateTime localDateTimeFin =
                    rs.getTimestamp("servicioprogramado.fechaFinalizacion") ==null?
                            null:rs.getTimestamp("servicioprogramado.fechaFinalizacion").toLocalDateTime();

            //puede ser null.
            LocalDateTime localDateTimeUltimoRegistro =
                    rs.getTimestamp("servicioprogramado.fechaUltimoRegistro") ==null?
                            null:rs.getTimestamp("servicioprogramado.fechaUltimoRegistro").toLocalDateTime();

            ServiciosProgramado SRAplicado =
                    new ServiciosProgramado(
                            datosServicio, rs.getInt("servicioprogramado.idServicioProgramado"),
                            localDateTimeAdicion, localDateTimeInicio, localDateTimeFin,
                            false/*no hay campo isCancelado en programados, se puede extraer ese miembro a servicio regular, para que no se herede en el programado rs.getBoolean("servicioprogramado.isCancelado")*/
                            ,clienteDelServicio, empleadoRegistroServicio,localDateTimeUltimoRegistro,
                            rs.getBoolean("servicioprogramado.lunes"),rs.getBoolean("servicioprogramado.martes"),rs.getBoolean("servicioprogramado.miercoles"),
                            rs.getBoolean("servicioprogramado.jueves"),rs.getBoolean("servicioprogramado.viernes"),rs.getBoolean("servicioprogramado.sabado"),
                            rs.getBoolean("servicioprogramado.domingo"));

           // SRAplicado.setTaxi(taxisDelServicio);

            serviciosRegularesPendientes.add(SRAplicado);
        }

        return serviciosRegularesPendientes;

    }

    /**
     * TODO implementar correctamente para servicio programado.
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
     * Inserta un @{@link ServiciosProgramado}  con la fecha de aplicacion y finalización nula y el campo isCancelado como false.
     * Cuando se inserte el idServicioProgramado de la instancia será reemplazado por el AutoIncrement que le asignó la base de datos.
     * Así que el ID con el que venga debe ser 0, y no asignarle un ID antes.
     * Los miembros internos como Direccion,Cliente,Empleado ya deben tener ID (la instancia) y deben existir en la DB.
     * @param servicioProgramado
     * Instancia a insertar en la base de datos.
     * @return
     * True si fue exitosa la inserción.
     * False si lo contrario.
     */
    public boolean insertarServicioProgramado(ServiciosProgramado servicioProgramado) {

        query = "INSERT INTO servicioprogramado " +
               // "(nombre, observaciones, fechaAgregacion, fechaServicio, fechaAplicacion, isCancelado, idCliente, idEmpleado, idDireccion) " +
                "VALUES" +
                //idServicioProgramado, fechaFin y fechaUltimoReg = NULL -> columnas 1,6,7
                " (NULL,?,?,?,?,  NULL,NULL,?,?,?,  ?,?,?,?,?  ,?,?)";

        try {

            ps = connection.prepareStatement(query);
            ps.setString(1,servicioProgramado.getNombre());
            ps.setString(2,servicioProgramado.getObservaciones());
            ps.setTimestamp(3,Timestamp.valueOf(servicioProgramado.getFechaAgregacion()));
            ps.setTimestamp(4,Timestamp.valueOf(servicioProgramado.getFechaServicio()));//fecha inicio

            ps.setBoolean(5,servicioProgramado.isLunes());
            ps.setBoolean(6,servicioProgramado.isMartes());
            ps.setBoolean(7,servicioProgramado.isMiercoles());
            ps.setBoolean(8,servicioProgramado.isJueves());
            ps.setBoolean(9,servicioProgramado.isViernes());
            ps.setBoolean(10,servicioProgramado.isSabado());
            ps.setBoolean(11,servicioProgramado.isDomingo());

            ps.setInt(12,servicioProgramado.getEmpleado().getIdEmpleado());
            ps.setInt(13,servicioProgramado.getCliente().getIdCliente());
            ps.setInt(14,servicioProgramado.getDireccion().getIdDireccion());

            ps.executeUpdate();

            servicioProgramado.setIdServicio(Statics.getLastId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Indica que este servicio programado ya no generará más servicios (el cliente ya no necesita el servicio periodico).
     * @param serviciosProgramado
     * El id del servicio.
     * @return
     * @throws SQLException
     */
    public boolean terminarProgramacionServicio(ServiciosProgramado serviciosProgramado) throws SQLException {

        Timestamp timestampAhora = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.now()));

        query = ("UPDATE servicioprogramado SET fechaFinalizacion = ? WHERE servicioprogramado.idServicioProgramado = ?");
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setTimestamp(1,timestampAhora);
        preparedStatement.setInt(2,serviciosProgramado.getIdServicio());

        serviciosProgramado.setFechaFin(timestampAhora.toLocalDateTime());


        return preparedStatement.executeUpdate()==1;
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


    /***
     * Aplica un servicio programado para la fecha actual del sistema.
     *
     * @param serviciosProgramado
     * El servicio programado a aplicar el dia actual.
     * Esta instancia ya debe contener un {@link Taxi} Asignado.
     * @return
     * Retorna un servicio regular el cual fue creado y aplicado dentro de este metodo.
     * @throws SQLException
     */
    public boolean aplicarServicioProgramado(ServiciosProgramado serviciosProgramado,ServicioRegular servicioRegularGeneradoAplicado) throws SQLException {

        Timestamp timestampActual = Timestamp.valueOf(servicioRegularGeneradoAplicado.getFechaAplicacion());

        query = "UPDATE servicioprogramado SET fechaUltimoRegistro = ? WHERE idServicioProgramado = ? ";
        ps = connection.prepareStatement(query);
        ps.setTimestamp(1, timestampActual);
        ps.setInt(2, serviciosProgramado.getIdServicio());
        ps.executeUpdate();


        serviciosProgramado.setFechaUltimaAplicacion(timestampActual.toLocalDateTime());

        query = "INSERT INTO servicioprogramado_has_servicio (idServicio,idServicioProgramado) VALUES (?,?) ";
        ps = connection.prepareStatement(query);
        ps.setInt(1, servicioRegularGeneradoAplicado.getIdServicio());
        ps.setInt(2, serviciosProgramado.getIdServicio());

        return ps.executeUpdate() == 1 ;

    }

}
