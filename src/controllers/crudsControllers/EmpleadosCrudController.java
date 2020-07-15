package controllers.crudsControllers;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Direccion;
import models.Empleado;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.StringLengthValidator;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmpleadosCrudController extends SetAddRegistroListener implements Initializable,IValidateCRUD {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lbl_tittle;

    @FXML
    private JFXTextField textField_nombre;

    @FXML
    private JFXTextField textField_telefono;

    @FXML
    private JFXPasswordField textField_password;

    @FXML
    private JFXDatePicker datePicker_nacimiento;

    @FXML
    private JFXComboBox<String> comboBox_tipo_empleado;

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
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    private int idEmpleado = 0;
    private int idDireccion = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldValidations();
        btnAceptar.setOnKeyReleased((event)->{
            if(event.getCode() == KeyCode.ENTER)
                btnAceptar.fire();
        });

        ObservableList<String> listaTiposEmpleado = FXCollections.observableArrayList();
        listaTiposEmpleado.addAll("Estándar","Administrador");

        comboBox_tipo_empleado.setItems(listaTiposEmpleado);

    }

    @FXML
    void btn_Agregar_Click(ActionEvent event) {
        if(validarCampos() )
            if(enviarRegistro(((Stage)btnAceptar.getScene().getWindow())))
                ((Stage)textField_observ.getScene().getWindow()).close();

    }

    @FXML
    void btn_Cancelar_Click(ActionEvent event) {
        ((Stage)textField_observ.getScene().getWindow()).close();
    }

    @Override
    public void extraerRegistro(Registro registro) {
        if(registro!=null){
            Empleado empleado = (Empleado) registro;
            Direccion direccion = empleado.getDireccion();

            this.idEmpleado = empleado.getIdEmpleado();
            this.idDireccion = empleado.getDireccion().getIdDireccion();

            textField_telefono.setText(empleado.getTelefono());
            textField_nombre.setText(empleado.getNombre());
            textField_password.setText(empleado.getContrasena());
            datePicker_nacimiento.setValue(empleado.getFechaNac());
            comboBox_tipo_empleado.getSelectionModel().select(empleado.isTipoEmpleado()?1:0);

            textField_calle.setText(direccion.getCalle());
            textField_colonia.setText(direccion.getColonia());
            textField_numExt.setText(direccion.getNumExt());
            textField_numInt.setText(direccion.getNumInt());

            textField_observ.setText(empleado.getObservaciones());
        }
    }

    @Override
    public Registro guardarCambiosRegistros() {

        //validarCampos
        return getEmpleadoVentana();

    }

    private Empleado getEmpleadoVentana() {
        Direccion direccion =  new Direccion(this.idDireccion, textField_calle.getText(), textField_colonia.getText(), textField_numInt.getText(), textField_numExt.getText());
        Empleado empleado = new Empleado(
                textField_nombre.getText(),
                textField_observ.getText(),
                direccion,
                this.idEmpleado,
                textField_telefono.getText(),
                textField_password.getText(),
                datePicker_nacimiento.getValue(),
                comboBox_tipo_empleado.getSelectionModel().getSelectedIndex() == 1);
        return empleado;
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

    @Override
    public void setLengthValidation() {
        this.textField_nombre.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_telefono.getValidators().add(new StringLengthValidator("Longuitud máxima de 11 carácteres.", 11));
        this.textField_password.getValidators().add(new StringLengthValidator("Longuitud máxima de 15 carácteres.", 15));
        this.textField_calle.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_colonia.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_numInt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));
        this.textField_numExt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));

    }

    @Override
    public void setRequiredValidation() {
        this.textField_calle.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_colonia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_password.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.comboBox_tipo_empleado.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_nombre.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));

    }
    private void setFocusedProperty() {

        textField_calle.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_calle.validate();
        });
        textField_colonia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_colonia.validate();
        });
        textField_password.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_password.validate();
        });
        comboBox_tipo_empleado.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                comboBox_tipo_empleado.validate();
        });
        textField_nombre.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_nombre.validate();
        });
        textField_numExt.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_numExt.validate();
        });
        textField_numInt.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_numInt.validate();
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
}
