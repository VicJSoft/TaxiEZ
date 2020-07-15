package controllers.crudsControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Direccion;
import models.Taxista;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.StringLengthValidator;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaxistasCrudController extends SetAddRegistroListener implements Initializable, IValidateCRUD {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lbl_tittle;

    @FXML
    private JFXTextField textField_nombre;

    @FXML
    private JFXTextField textField_telefono;

    @FXML
    private JFXDatePicker datePicker_nacimiento;

    @FXML
    private JFXTextField textField_calle;

    @FXML
    private JFXTextField textField_colonia;

    @FXML
    private JFXTextField textField_numExt;

    @FXML
    private JFXTextField textField_numInt;

    @FXML
    private JFXTextField textField_observaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFieldValidations();
    }

    @FXML
    void btn_Agregar_Click(ActionEvent event) {
        if(validarCampos() )
            if(enviarRegistro(((Stage)textField_nombre.getScene().getWindow())))
                ((Stage)textField_nombre.getScene().getWindow()).close();

    }

    @FXML
    void btn_Cancelar_Click(ActionEvent event) {
        ((Stage)textField_nombre.getScene().getWindow()).close();
    }

    int idTaxista = 0;
    int idDireccion = 0;
    @Override
    public void extraerRegistro(Registro registro) {

        if(registro!=null){
            Taxista taxista = (Taxista) registro;
            Direccion direccion = taxista.getDireccion();
            this.idTaxista = taxista.getIdTaxista();
            this.idDireccion = taxista.getDireccion().getIdDireccion();
            textField_telefono.setText(taxista.getTelefono());
            textField_nombre.setText(taxista.getNombre());

            datePicker_nacimiento.setValue(taxista.getFechaNac());
            textField_calle.setText(direccion.getCalle());
            textField_colonia.setText(direccion.getColonia());
            textField_numExt.setText(direccion.getNumExt());
            textField_numInt.setText(direccion.getNumInt());

            textField_observaciones.setText(taxista.getObservaciones());
        }

    }

    @Override
    public Registro guardarCambiosRegistros() {

        Direccion direccion =
                new Direccion(this.idDireccion, textField_calle.getText(), textField_colonia.getText(), textField_numInt.getText(), textField_numExt.getText());
        Taxista taxista =
                new Taxista(this.idTaxista, datePicker_nacimiento.getValue(), textField_telefono.getText(), textField_nombre.getText(), textField_observaciones.getText(), direccion);


        return taxista;
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
        // TODO falta implementar un textchage para validar mientras escribe.
    }

    @Override
    public void setLengthValidation() {
        this.textField_calle.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_telefono.getValidators().add(new StringLengthValidator("Longuitud máxima de 15 carácteres.", 15));
        this.textField_colonia.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_nombre.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_numInt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));
        this.textField_numExt.getValidators().add(new StringLengthValidator("Longuitud máxima de 8 carácteres.", 8));
    }

    @Override
    public void setRequiredValidation() {
        this.textField_calle.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_colonia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        this.textField_nombre.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));

    }

    private void setFocusedProperty() {

        textField_nombre.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_nombre.validate();
        });

        textField_calle.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_calle.validate();
        });
        textField_colonia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_colonia.validate();
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
