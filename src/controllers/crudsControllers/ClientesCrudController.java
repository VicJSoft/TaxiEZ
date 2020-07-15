package controllers.crudsControllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Cliente;
import models.Direccion;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.StringLengthValidator;
import javafx.scene.control.*;
import services.sql.DireccionSQL;
import sun.util.logging.PlatformLogger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientesCrudController extends SetAddRegistroListener implements Initializable, IValidateCRUD {
    @FXML
    private StackPane root;

    @FXML
    private AnchorPane anchorP;

    @FXML
    private JFXTextField textField_telefono;

    @FXML
    private Label lbl_tittle;

    @FXML
    private JFXTextField textField_nombre;

    @FXML
    private JFXTextField textField_calle;

    @FXML
    private JFXTextField textField_colonia;

    @FXML
    private JFXTextField textField_numExt;

    @FXML
    private JFXTextField textField_numInt;

    @FXML
    private JFXTextField textField_observ;

    @FXML
    private Button button_Aceptar;

    @FXML
    private Button button_Cancelar;

    private int idDireccion = 0;
    private int idCliente = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldValidations();
        button_Aceptar.setOnKeyReleased((event)->{
            if(event.getCode() == KeyCode.ENTER)
                button_Aceptar.fire();
        });
    }
    @FXML
    void btn_Agregar_Click(ActionEvent event) {

        if(validarCampos() ){
            if(enviarRegistro(((Stage)button_Aceptar.getScene().getWindow()))) //si fue posible subir la info se cerrará la ventana
                ((Stage)button_Aceptar.getScene().getWindow()).close();
        }

    }

    @FXML
    void btn_Cancelar_Click(ActionEvent event) {
        //cerrar ventana
        ((Stage)button_Aceptar.getScene().getWindow()).close();
    }



    @Override
    public void extraerRegistro(Registro registro) {
        //extrae la instancia.
        //cuando es null entonces es para registro nuevo, no una edición-
        if(registro !=null){
            Cliente clienteExtraido = (Cliente)registro;
            Direccion direccionCliente = clienteExtraido.getDireccion();

            this.idDireccion = direccionCliente.getIdDireccion();
            this.idCliente = clienteExtraido.getIdCliente();

            textField_calle.setText(direccionCliente.getCalle());
            textField_colonia.setText(direccionCliente.getColonia());
            textField_numExt.setText(direccionCliente.getNumExt());
            textField_numInt.setText(direccionCliente.getNumInt());

            textField_nombre.setText(clienteExtraido.getNombre());
            textField_telefono.setText(clienteExtraido.getNumero());
            textField_observ.setText(clienteExtraido.getObservaciones());
        }
    }

    @Override
    public Registro guardarCambiosRegistros(){
        //validar campos
        return getClienteVentana();//return new Cliente
    }

    /**
     * Mapea la ventana clientes, para generar un objeto, con los datos disponibles en la ventana.
     * Este método debe llamarse despues de validar los campos, para no tenereun campo nulo.
     * @return
     * Retorna objeto cliente, del resultado de la extracción de los datos de la ventana Clientes.
     */
    private Cliente getClienteVentana() {
        Direccion direccion =
                new Direccion(this.idDireccion, textField_calle.getText(), textField_colonia.getText(), textField_numInt.getText(), textField_numExt.getText());
        Cliente clienteVentana =
                new Cliente(this.idCliente,textField_telefono.getText(),true,textField_nombre.getText(), textField_observ.getText(),direccion);

        return clienteVentana;
    }

    @Override
    public ArrayList<IFXValidatableControl> listControlsRequired() {
        return null;
    }

    @Override
    public void setFieldValidations() {
        setLengthValidation();
        setRequiredValidation();
        setFocusedProperty();
    }

    private void setFocusedProperty() {

        textField_telefono.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
                if(!newValue)
                    textField_telefono.validate();
        });
        textField_nombre.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
                if(!newValue)
                    textField_nombre.validate();
        });
        textField_colonia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
                if(!newValue)
                    textField_colonia.validate();
        });

    }

    @Override
    public void setLengthValidation() {
        this.textField_nombre.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_calle.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_colonia.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_numInt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));
        this.textField_numExt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));
        this.textField_telefono.getValidators().add(new StringLengthValidator("Longuitud máxima de 10 carácteres.", 11));

        textField_nombre.textProperty().addListener((observable,  oldValue,  newValue)-> {
            textField_nombre.validate();
        });
        textField_calle.textProperty().addListener((observable,  oldValue,  newValue)-> {
                textField_calle.validate();
        });
        textField_colonia.textProperty().addListener((observable,  oldValue,  newValue)-> {
                textField_colonia.validate();
        });

        textField_numInt.textProperty().addListener((observable,  oldValue,  newValue)-> {
                textField_numInt.validate();
        });
        textField_numExt.textProperty().addListener((observable,  oldValue,  newValue)-> {
            textField_numExt.validate();
        });
        textField_telefono.textProperty().addListener((observable,  oldValue,  newValue)-> {
            textField_telefono.validate();
        });

    }


    @Override
    public void setRequiredValidation() {
        this.textField_telefono.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_calle.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_colonia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_nombre.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
    }

    @Override
    public boolean validarCampos() {

        ObservableList<Node> listaHijos = anchorP.getChildren();
        boolean validacioExitosa = true;

        for(Node node : listaHijos){
            if(node instanceof IFXValidatableControl){
                boolean validate = ((IFXValidatableControl) node).validate();
                validacioExitosa = validacioExitosa&&validate;
            }
        }


        return validacioExitosa;
    }
}
