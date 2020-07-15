package controllers.crudsControllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Cliente;
import models.Direccion;
import models.Persona;
import models.ServicioRegular;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import resources.Statics;
import services.StringLengthValidator;
import services.sql.ClienteSQL;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServicioRegularCrudController extends SetAddRegistroListener implements Initializable,IValidateCRUD {


    @FXML
    private AnchorPane root;

    @FXML
    private Label lbl_tittle;

    @FXML
    public JFXTextField textField_buscarTelefono;

    @FXML
    private JFXTextField textField_telefono;

    @FXML
    private JFXTextField textField_nombre;

    @FXML
    private JFXTextField textField_calle;

    @FXML
    private JFXTextField textField_colonia;

    @FXML
    private JFXTextField textField_num_ext;

    @FXML
    private JFXTextField textField_numInt;

    @FXML
    private JFXTextField textField_observaciones;

    @FXML
    private JFXTimePicker timePicker_horaServicio;

    @FXML
    private JFXDatePicker datePicker_dia;

    @FXML
    private Button btn_aceptar;

    @FXML
    private Button btn_cancelar;
    private Cliente cliente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        timePicker_horaServicio.setValue(LocalTime.now());
        datePicker_dia.setValue(LocalDate.now());
        this.setFieldValidations();

    }

    @FXML
    void btnAceptar_OnAction(ActionEvent event) {
        if(validarCampos() )
            if(enviarRegistro(((Stage)btn_aceptar.getScene().getWindow())))
                ((Stage)btn_aceptar.getScene().getWindow()).close();

    }

    @FXML
    void btnCancelar_OnAction(ActionEvent event) {
        ((Stage)btn_aceptar.getScene().getWindow()).close();

    }



    @Override
    public ArrayList<IFXValidatableControl> listControlsRequired() {
        return null;
    }

    @Override
    public void setFieldValidations() {

        this.setLengthValidation();
        this.setRequiredValidation();
        this.setFocusedProperty();

        textField_buscarTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                extraerRegistro(new ClienteSQL().existe(newValue));
            } catch (SQLException e) {
                e.printStackTrace();
                //ventana error.
            }
        });
    }

    @Override
    public void setLengthValidation() {

        textField_telefono.getValidators().add(new StringLengthValidator("Máximo 11 carácteres.",11));
        textField_colonia.getValidators().add(new StringLengthValidator("Máximo 45 carácteres.",45));
        textField_calle.getValidators().add(new StringLengthValidator("Máximo 45 carácteres.",45));
        textField_num_ext.getValidators().add(new StringLengthValidator("Máximo 10 carácteres.",10));
        textField_numInt.getValidators().add(new StringLengthValidator("Máximo 10 carácteres.",10));


    }

    @Override
    public void setRequiredValidation() {
        textField_telefono.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        textField_colonia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        textField_calle.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        datePicker_dia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        timePicker_horaServicio.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
    }

    private void setFocusedProperty() {

        textField_nombre.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_telefono.validate();
        });

        textField_colonia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_colonia.validate();
        });
        textField_calle.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_calle.validate();
        });
        datePicker_dia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                datePicker_dia.validate();
        });
        timePicker_horaServicio.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                timePicker_horaServicio.validate();
        });
        textField_numInt.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_numInt.validate();
        });
        textField_num_ext.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_num_ext.validate();
        });



    }

    @Override
    public boolean validarCampos() {
        ObservableList<Node> listaHijos = root.getChildren();
        boolean validacioExitosa = true;


        for(Node node : listaHijos){
            if(node instanceof IFXValidatableControl){
                boolean validate = ((IFXValidatableControl) node).validate();
                validacioExitosa = validacioExitosa&&validate;
            }
        }



        return validacioExitosa;
    }

    /**
     * los servicios no son editables, este metodo nunca se usará para editar un servicio.
     * Pero sí se usará para la busqueda automatica  de los datos de cliente.
     * Si el numero de cliente existe se rellenarán los campos con ese cliente.
     * @param registro
     */
    @Override
    public void extraerRegistro(Registro registro) {

        if(registro!=null){
            Cliente cliente = (Cliente) registro;
            Direccion direccion = cliente.getDireccion();

            textField_telefono.setText(cliente.getNumero());
            textField_nombre.setText(cliente.getNombre());

            textField_calle.setText(direccion.getCalle());
            textField_colonia.setText(direccion.getColonia());
            textField_numInt.setText(direccion.getNumInt());
            textField_num_ext.setText(direccion.getNumExt());
        }else{

            //si no existe limpia todo.
            textField_nombre. clear();
            textField_calle.clear();
            textField_colonia.clear();
            textField_numInt.clear();
            textField_num_ext.clear();
        }

        //lo guardo para cuando se guarden los cambios, se tome este cliente para ese servicio, y si es null se creará uno nuevo con los datos de direccion
        this.cliente = (Cliente) registro;

    }


    @Override
    public Registro guardarCambiosRegistros() {

        Direccion direccion = null;

        if(cliente!=null){
            //cuando se buscó y encontró el numero de telefono automaticamente.
            try {
                direccion = cliente.getDireccion().clone();//una copia para asignarsela a la instancia de servicioRegular.
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }else{//cliente == null

            //si la dirección es id=0, se deberá insertar en la DB.
            direccion =
                    new Direccion(0, textField_calle.getText(), textField_colonia.getText(), textField_numInt.getText(), textField_num_ext.getText());
        }

        Persona datos = new Persona(textField_nombre.getText(),textField_observaciones.getText(),direccion);
        //si cliente == null, esa propiedad debe ser creada
        ServicioRegular servicioRegular =
                new ServicioRegular(datos,0,LocalDateTime.now(),LocalDateTime.of(datePicker_dia.getValue(),timePicker_horaServicio.getValue()),null,false,cliente, Statics.empleadoSesionActual);
        servicioRegular.setTelefonoAux(textField_telefono.getText());

        return servicioRegular;

    }

}
