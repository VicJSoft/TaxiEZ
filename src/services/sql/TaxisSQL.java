package services.sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import models.Empleado;
import models.Taxi;
import models.Taxista;
import resources.Statics;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxisSQL {

    private Connection connection;
    private boolean key;
    private String query;
    private ResultSet rs;
    private PreparedStatement ps;
    private ActionEvent event;



    public TaxisSQL() {

        this.connection = Statics.getConnections();

    }


    /**
     * La insersión puede ser correcta o no, ya que el usuario puede cancelarla, pero también puede haber
     * error SQL que va al Logger.
     * @param taxi
     * El taxi a insertar, este puede tener o no un numero de telefono que ya exista en la bse de datos.
     * @return
     * Sí el registro tiene su campo de visibilidad como falso, entonces se activa, y se actualiza a los datos dados por el usuario.
     * Sí el registro tienen su campo visibilidad como verdadero
     * True si se insertó correctamente.
     * False si no se insertó.
     *
     */
    public boolean insertar(Taxi taxi)  {

        try{


            if(existeVisible(taxi)==2){

                query = "UPDATE unidad SET marca = ?,modelo =?,placa = ?,idTaxista = ?,visible = 1 WHERE unidad.idUnidad = ?";

            }else
            query = "INSERT INTO unidad (marca, modelo,placa, idTaxista,idUnidad) " +
                    "VALUES ( ?, ?, ?, ?,?)";

            ps = connection.prepareStatement(query);
            ps.setString(1, taxi.getMarca());
            ps.setString(2, taxi.getModelo());
            ps.setString(3, taxi.getPlaca());
            ps.setInt(4, taxi.getTaxista().getIdTaxista());
            ps.setInt(5, taxi.getIdUnidad());

            ps.executeUpdate();
            taxi.setIdUnidad(taxi.getIdUnidad());
            return true;


        }
        catch (SQLException sqlE){
            sqlE.printStackTrace();
            //ventana error v:
        }

        return false;

    }
    public boolean actualizar(Taxi taxi)  {
        try {
            query = "UPDATE unidad SET marca = ?,modelo =?,placa = ?,idTaxista = ? WHERE unidad.idUnidad = ?";
            ps = connection.prepareStatement(query);


            ps.setString(1, taxi.getMarca());
            ps.setString(2, taxi.getModelo());
            ps.setString(3, taxi.getPlaca());
            ps.setInt(4, taxi.getTaxista().getIdTaxista());
            ps.setInt(5, taxi.getIdUnidad());


            ps.executeUpdate();
            return true;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Buscará ese registro para saber si existe en la base de datos.
     *
     * @param taxi
     * Registro a buscar.
     * @return
     * 2 si existe pero está como visible = 0
     * 1 si existe.
     * 0 si no.
     * @throws SQLException
     */
    public int existeVisible(Taxi taxi) throws SQLException {
        int existe = 0;

        query = "SELECT * FROM unidad WHERE idUnidad = ? ";

        ps = connection.prepareStatement(query);
        ps.setInt(1, taxi.getIdUnidad());

        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()){
            existe = 1;//existe visible = 1.
            if(!resultSet.getBoolean("visible"))
                existe =2;//existe visible = 0.
        }


        return existe;

    }
    public int existe(int idTaxi) throws SQLException {
        int existe = 0;

        query = "SELECT * FROM unidad WHERE idUnidad = ?";

        ps = connection.prepareStatement(query);
        ps.setInt(1, idTaxi);

        //0 no existe, mayor qué 0 existe
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()){
            existe = 1;
            if(!resultSet.getBoolean("visible"))
                existe =2;
        }


        return existe;

    }
    public boolean eliminar(Taxi taxi){
        //eliminar todos los que tengan ese telefono
        query= "UPDATE unidad SET visible = 0 WHERE unidad.idUnidad = ?";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, taxi.getIdUnidad());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return
     * Lista de los {@link Taxi} existentes actualmente en la base de datos.
     */
    public ObservableList<Taxi> getTaxis()
    {
        ObservableList<Taxi> taxis =  FXCollections.observableArrayList();
        query="SELECT * FROM unidad JOIN taxista on unidad.idTaxista = taxista.idTaxista  WHERE taxista.visible = 1 and unidad.visible = 1";
       // query="SELECT * FROM taxista A WHERE A.idTaxista NOT IN (SELECT unidad.idTaxista FROM unidad)";
        try
        {
            ps = connection.prepareStatement(query);
            rs=ps.executeQuery();
            while(rs.next())
            {
                taxis.add( crearTaxista(rs) );
            }

            ps.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(ClienteSQL.class.getName()).log(Level.SEVERE, "Error al extraer clientes.", ex);
        }

        return taxis;
    }

    /**
     * TODO //Nombre está mal xd, corregir.
     * @param rs tupla {@link Taxi}.
     * La tupla obtenia es:
     * @return Instancia de Cliente a partir de la tupla contenida en rs.
     * idUnidad	marca	modelo	placa	idTaxista	visible
     * idTaxista nombre telefono fechNac observaciones idDireccion visible
     * @throws SQLException
     */
    private Taxi crearTaxista(ResultSet rs) throws SQLException {

        Taxista taxista =
                new Taxista(rs.getInt("idTaxista"),
                        rs.getDate("fechNac")==null?null:rs.getDate("fechNac").toLocalDate(),
                        rs.getString("telefono"),
                        rs.getString("nombre"),
                        rs.getString("observaciones"),
                        null);

        Taxi taxi = new Taxi(
                rs.getInt("idUnidad"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getString("placa"),
                taxista
        );

        return taxi;
    }

    /**
     * Consigue el empleado según el idIndicado.
     * @param unidad
     * Id del empleado a buscarr.
     * @return
     * Instancia con los datos que contiene la base de datos.
     * Null si no existe ese indice.
     * @throws SQLException
     */
    public Taxi get(int unidad) throws SQLException {
        query = "SELECT * FROM unidad JOIN taxista on unidad.idTaxista = taxista.idTaxista  WHERE taxista.visible = 1 and unidad.visible = 1 AND unidad.idUnidad = ?";

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1,unidad);

        ResultSet resultSet = ps.executeQuery();

        if(resultSet.first()){
            return crearTaxista(resultSet);
        }
        return null;
    }

}
