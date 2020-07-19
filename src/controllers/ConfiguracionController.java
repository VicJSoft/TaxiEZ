/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.interfaces.IValidateCRUD;
import persistencia.SharePreferencesDB;
import resources.Statics;

/**
 * FXML Controller class
 *
 * @author ESPINO
 */
public class ConfiguracionController implements Initializable, IValidateCRUD {


    @FXML
    private JFXTextField txt_IP;
    @FXML
    private JFXTextField txt_Puerto;
    @FXML
    private Button btn_Probar;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    @FXML
    private JFXTextField txt_Usuario;
    @FXML
    private JFXPasswordField txt_Contrasena;
    


    private boolean conexionSatisfactoria = false;
    private SharePreferencesDB configuracion;
    Connection connection;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Recupera información de conexion para la DB.
        this.configuracion = SharePreferencesDB.getConfiguracion();
        this.setRequiredValidation();
        
        this.txt_IP.setText(configuracion.getIp());
        this.txt_Puerto.setText(configuracion.getPuerto());
        this.txt_Usuario.setText(configuracion.getUser());
        this.txt_Contrasena.setText(configuracion.getPass());
    }    

    @FXML
    private void btn_Cancelar_Click(ActionEvent event) {
        ( (Stage) ((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void btn_Aceptar_Click(ActionEvent event) {
        
        if(this.conexionSatisfactoria){
            
            //guardar configuración de la DB en el json
            SharePreferencesDB.setConfiguracion(this.getVentanaConfiguracion());
            Statics.setConnections(this.connection);
            ( (Stage) ((Node)event.getSource()).getScene().getWindow()).close();
        }
        
    }

    @FXML
    private void btn_Probar_Click(ActionEvent event) {
        if(validarCampos()){
            
            String path = "jdbc:mysql://";
            path = path + this.txt_IP.getText() + ":" + this.txt_Puerto.getText() + "/sitio_taxi";
            String user = this.txt_Usuario.getText();
            String pass = this.txt_Contrasena.getText();
 
            try {
               Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(path,user,pass);

               this.conexionSatisfactoria = true;
                ErrorController errorController =  new ErrorController("Conectado", "Conectado","¡Conexión a la base de datos exitosa!");
                errorController.show(btn_Probar.getScene().getWindow());


            } 
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) 
            {
                /*Servicios.crearVentanaError(this.btnAceptar.getScene().getWindow(),
                        "Error de conexión de Base de Datos",
                        "No fue posible conectar a la base de datos", 
                        ex.getMessage()
                );*/
                ex.getMessage(); 
                this.conexionSatisfactoria = false;
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                ErrorController errorController =  new ErrorController("Error de conexión", "Error de conexión","¡Conexión a la base de datos fallida!\n¡Inténtelo de nuevo!\n¡Llame al administrador!");
                errorController.show(btn_Probar.getScene().getWindow());
            }

        }
    }

    @Override
    public ArrayList<IFXValidatableControl> listControlsRequired() {
        return null;
    }

    @Override
    public void setFieldValidations() {
    }

    @Override
    public void setLengthValidation() {
    }

    @Override
    public void setRequiredValidation() {
        this.txt_IP.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        this.txt_Puerto.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        this.txt_Usuario.getValidators().add(new RequiredFieldValidator("Campo requerido."));
     //   this.txt_Contrasena.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        
        this.txt_Puerto.getValidators().add(new IntegerValidator("Ingrese solo números."));
        
        
    }

    @Override
    public boolean validarCampos() {
        boolean valido = true;
        
        this.txt_IP.validate();
        this.txt_Puerto.validate();
        this.txt_Usuario.validate();
        this.txt_Contrasena.validate();
        
        valido = valido &&  
                this.txt_IP.validate() &&
                this.txt_Puerto.validate() &&
                this.txt_Usuario.validate() &&
                this.txt_Contrasena.validate();
        
        return valido;
        
    }

    private SharePreferencesDB getVentanaConfiguracion() {

        return new SharePreferencesDB(this.txt_IP.getText(), this.txt_Puerto.getText(), 
                this.txt_Usuario.getText(), this.txt_Contrasena.getText()
        );

    }
    
}
