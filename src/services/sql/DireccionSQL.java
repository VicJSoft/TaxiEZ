package services.sql;

import models.Direccion;
import resources.Statics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DireccionSQL {


    private final Connection connection;

    public DireccionSQL() {
        this. connection = Statics.getConnections();
    }

    /**
     * Inserta una nueva direccion.
     * Esta dirección debe tener idDireccion = 0, si es diferente de 0 el id quedará reemplazado como si fuera un nuevo registro Dirección.
     * @param direccion
     * @return
     */
    public boolean insertarDireccion(Direccion direccion)  {
   //     String query = "INSERT INTO `direccion` (`idDireccion`, `calle`, `colonia`, `numInt`, `numExt`) VALUES (?, ?,?,?,?); ";
        String query = "INSERT INTO `direccion` ( `calle`, `colonia`, `numInt`, `numExt`) VALUES (?,?,?,?); ";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
           // preparedStatement.setInt(1,direccion.getIdDireccion());
            preparedStatement.setString(1,direccion.getCalle());
            preparedStatement.setString(2,direccion.getColonia());
            preparedStatement.setString(3,direccion.getNumInt());
            preparedStatement.setString(4,direccion.getNumExt());

            int i = preparedStatement.executeUpdate();
            direccion.setIdDireccion(Statics.getLastId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean actualizar(Direccion direccion)  {
        String query = "UPDATE direccion SET calle = ?,colonia = ?, numInt = ?,numExt = ? WHERE direccion.idDireccion = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,direccion.getCalle());
            preparedStatement.setString(2,direccion.getColonia());
            preparedStatement.setString(3,direccion.getNumInt());
            preparedStatement.setString(4,direccion.getNumExt());
            preparedStatement.setInt(5,direccion.getIdDireccion());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     * Retorna instancia de {@link Direccion}.
     * @param idDireccion
     * el indice a buscar para crear la instancia.
     * @return
     * Si el indice no existe retorna NULL.
     */
    public Direccion get(int idDireccion){
        String query = "SELECT * FROM direccion WHERE direccion.idDireccion = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,idDireccion);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.first()){
                return crearDireccion(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;

    }

    private Direccion crearDireccion(ResultSet rs) throws SQLException {

        Direccion direccion = new Direccion(rs.getInt("idDireccion"),
                rs.getString("calle"),rs.getString("colonia"),
                rs.getString("numInt"),rs.getString("numExt"));

        return direccion;
    }

}
