package controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Empleado;
import persistencia.SharePreferences;
import resources.Statics;
import services.sql.EmpleadoSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane fondoAP;

    @FXML
    private JFXTextField txt_usuario;

    @FXML
    private JFXPasswordField txt_contrasena;

    @FXML
    private JFXCheckBox cb_recordar;

    @FXML
    private Button btn_login;


    @FXML
    private Button btn_config;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setValidatorsRequired();

            getCredeciales();


        try {
            Statics.createConnection();
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            //crear ventana error.
        }

    }


    @FXML
    void btnConfig_OnAction(ActionEvent event) {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/resources/imagenes/iconos/Taxi/taxi.png"));
        Parent parent = null;
        try {

            parent = FXMLLoader.load(getClass().getResource("/views/Configuracion.fxml"));
            stage.setTitle("Taxis");

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btn_login.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setValidatorsRequired(){
        txt_usuario.getValidators().add(new RequiredFieldValidator("Este campo no debe estar vacío..."));
        txt_usuario.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue)
                    txt_usuario.validate();
            }
        });
        txt_contrasena.getValidators().add(new RequiredFieldValidator("Este campo no debe estar vacío..."));
        txt_contrasena.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue)
                    txt_contrasena.validate();
            }
        });
    }

    @FXML
    private void txtUsuario_ReleasedKey(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            txt_contrasena.requestFocus();
        }
    }

    @FXML
    private void txtPassword_ReleasedKey(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            btn_login.fire();
            txt_usuario.requestFocus();
        }
    }

    @FXML
    void btnLogin_Click(ActionEvent event) throws IOException {

        Empleado empleado = null;
        try {
            empleado = new EmpleadoSQL().existe(txt_usuario.getText(), txt_contrasena.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(empleado==null){
            //ventana error de credenciales.
        }else{

            Statics.empleadoSesionActual = empleado;
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/resources/imagenes/iconos/Taxi/taxi.png"));
            Parent parent = FXMLLoader.load(getClass().getResource("/views/Principal.fxml"));
            stage.setTitle("Taxis");

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }



    }

    private void getCredeciales()
    {
        SharePreferences sharePreferences = SharePreferences.getCredenciales();
        if(sharePreferences.getRecordar())
        {
            cb_recordar.setSelected(true);
            txt_usuario.setText(sharePreferences.getUsuario());
        }
        else
        {
            cb_recordar.setSelected(false);
        }
    }
    private void setCredenciales()
    {
        SharePreferences sharePreferences = new SharePreferences(cb_recordar.isSelected(),txt_usuario.getText());
        SharePreferences.setCredenciales(sharePreferences);
    }


}
