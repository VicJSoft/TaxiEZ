package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.interfaces.IAccion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    private MenuItem miConfiguracion;

    @FXML
    private MenuItem miCerrarSesion;

    @FXML
    private MenuItem miCerrar;

    @FXML
    private MenuItem miServicios;

    @FXML
    private MenuItem miClientes;

    @FXML
    private MenuItem miEmpleados;

    @FXML
    private MenuItem miTaxisTaxista;

    @FXML
    private MenuItem miEstadisticas;

    @FXML
    private MenuItem miAccionPrimaria;

    @FXML
    private MenuItem miAccionSecundaria;

    @FXML
    private MenuItem miAccionTerciaria;

    @FXML
    private MenuItem miAccionServicioRapido;

    @FXML
    private MenuItem miAcercaDe;

    @FXML
    private AnchorPane apContenedorSecundario;


    private IAccion controladorActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //refactorizar, solo copié y pegué

        cargarPantalla("Servicios");

    }
    @FXML
    void cerrarAcelerator_OnAction(ActionEvent event)
    {
       System.exit(0);
    }

    @FXML
    void cerrarSesionAcelerator_OnAction(ActionEvent event)
    {
        try {
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/resources/imagenes/iconos/Taxi/taxi.png"));
            Parent parent = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            stage.setTitle("Taxis");
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
            ((Stage)((Node)apContenedorSecundario).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void miConfiguracion_OnAction(ActionEvent event)
    {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/resources/imagenes/iconos/Taxi/taxi.png"));
        Parent parent = null;
        try {

            parent = FXMLLoader.load(getClass().getResource("/views/Configuracion.fxml"));
            stage.setTitle("Taxis");

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(apContenedorSecundario.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void serviciosAcelerator_OnAction(ActionEvent event) {
        cargarPantalla("Servicios");
    }

    @FXML
    void clientesAcelerator_OnAction(ActionEvent event) {
        cargarPantalla("Clientes");
    }

    @FXML
    void empleadosAcelerator_OnAction(ActionEvent event) {
        cargarPantalla("Empleados");
    }

    @FXML
    void estadisticasAcelerator_OnAction(ActionEvent event) {
        cargarPantalla("Estadisticas");

    }

    @FXML
    void taxisAcelerator_OnAction(ActionEvent event) {
        cargarPantalla("Taxis");

    }

    @FXML
    void taxistasAcelerator_OnAction(ActionEvent event) {

        cargarPantalla("Taxistas");
    }


    @FXML
    void accionPrimaria_OnAction(ActionEvent event) {
        System.out.println("Prim");
        controladorActual.accionPrimaria();
    }

    @FXML
    void accionSecundaria_OnAction(ActionEvent event) {
        System.out.println("Sec");
        controladorActual.accionSecundaria();
    }

    @FXML
    void accionTerciaria_OnAction(ActionEvent event) {
        System.out.println("Ter");
        controladorActual.accionTerciaria();
    }

    @FXML
    void accionServicioRapido_OnAction(ActionEvent event) {
        System.out.println("Serv rapido");

    }



    private void cargarPantalla(String pantallaName){
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/"+pantallaName+".fxml"));
            AnchorPane contenedor = controladorLoader.load();
            controladorActual = controladorLoader.getController();

            apContenedorSecundario.getChildren().clear();
            apContenedorSecundario.getChildren().addAll(contenedor.getChildren());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
