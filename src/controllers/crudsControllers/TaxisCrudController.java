package controllers.crudsControllers;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Taxi;
import models.Taxista;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.StringLengthValidator;
import services.sql.TaxisSQL;
import services.sql.TaxistaSQL;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaxisCrudController extends SetAddRegistroListener implements Initializable, IValidateCRUD {
    @FXML
    private AnchorPane root;

    @FXML
    private Label lbl_tittle;

    @FXML
    public JFXTextField textField_unidad;

    @FXML
    private JFXTextField textField_marca;

    @FXML
    private JFXTextField textField_placa;

    @FXML
    private JFXTextField textField_modelo;

    @FXML
    private JFXComboBox<Taxista> comboBox_taxista;

    @FXML
    private Button btn_Agregar;

    @FXML
    private Button btn_Cancelar;

    ObservableList<Taxista> listaTaxistas ;

    private int idTaxista;
    private int idDireccion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_Agregar.setOnKeyReleased((event)->{
            if(event.getCode() == KeyCode.ENTER)
                btn_Agregar.fire();
        });

        listaTaxistas = new TaxistaSQL().getTaxistasLibres();

        comboBox_taxista.setItems(listaTaxistas);
        comboBox_taxista.setCellFactory(callbackCombo);
        //hacer esto o  hacer override a el To String();, con JFoenix hay una doble etiqueta, así que o se quita el prompt o se hace el toString del model.
       // comboBox_taxista.setButtonCell(callbackCombo.call(null));

    }


    Callback<ListView<Taxista>, ListCell<Taxista>> callbackCombo = new Callback<ListView<Taxista>, ListCell<Taxista>>() {

        @Override
        public ListCell<Taxista> call(ListView<Taxista> param) {

            ListCell<Taxista> listCell = new ListCell<Taxista>(){
                @Override
                protected void updateItem(Taxista item, boolean empty) {
                    super.updateItem(item, empty);

                    if(item!=null){
                        this.setText(item.getIdTaxista() + " " + item.getNombre());
                    }else{
                        //setText(null);
                        setGraphic(null);
                    }
                }
            };

            return listCell;
        }
    };


    @FXML
    void btn_Agregar_Click(ActionEvent event) {
            if(validarCampos() )
                if(enviarRegistro(((Stage)btn_Agregar.getScene().getWindow())))
                    ((Stage)comboBox_taxista.getScene().getWindow()).close();

    }

    @FXML
    void btn_Cancelar_Click(ActionEvent event) {
        ((Stage)this.comboBox_taxista.getScene().getWindow()).close();

    }


    @Override
    public void extraerRegistro(Registro registro) {
        if(registro!=null){
            Taxi taxi = (Taxi) registro;
            textField_marca.setText(taxi.getMarca());
            textField_modelo.setText(taxi.getModelo());
            textField_unidad.setText(taxi.getIdUnidad()+"");
            textField_placa.setText(taxi.getPlaca());
            //si el
            for(Taxista taxista :listaTaxistas){
                if(taxista.getIdTaxista() == taxi.getTaxista().getIdTaxista()){
                    comboBox_taxista.getSelectionModel().select(taxista);
                }
            }
        }
    }

    @Override
    public Registro guardarCambiosRegistros() {
        return getTaxiVentana();
    }

    private Taxi getTaxiVentana() {
        Taxi taxi = new Taxi(
                Integer.parseInt(textField_unidad.getText()), textField_marca.getText(), textField_modelo.getText(),
                textField_placa.getText(),
                comboBox_taxista.getSelectionModel().getSelectedItem()
        );

        return taxi;
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
    }

    @Override
    public void setLengthValidation() {
        this.textField_unidad.getValidators().add(new StringLengthValidator("Longuitud máxima de 11 digítos.", 11));
        this.textField_marca.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
        this.textField_placa.getValidators().add(new StringLengthValidator("Longuitud máxima de 10 carácteres.", 10));
        this.textField_modelo.getValidators().add(new StringLengthValidator("Longuitud máxima de 45 carácteres.", 45));
    }

    @Override
    public void setRequiredValidation() {
        this.textField_unidad.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        this.textField_marca.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        this.textField_placa.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        //this.textField_modelo.getValidators().add(new RequiredFieldValidator("Campo requerido."));
        this.comboBox_taxista.getValidators().add(new RequiredFieldValidator("Campo requerido."));

    }

    private void setFocusedProperty() {

        textField_unidad.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_unidad.validate();
        });

        textField_marca.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_marca.validate();
        });
        textField_modelo.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_modelo.validate();
        });
        textField_placa.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_placa.validate();
        });
        comboBox_taxista.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                comboBox_taxista.validate();
        });



    }

    @Override
    public boolean validarCampos() {
        ObservableList<Node> listaHijos = root.getChildren();
        boolean validacioExitosa = true;

        try {
            int existe = new TaxisSQL().existe(Integer.parseInt(textField_unidad.getText()));
            if(existe ==1){
                //ventana error, diciendo que ya existe esa unidad.
                validacioExitosa = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //ventana error
        }


        for(Node node : listaHijos){
            if(node instanceof IFXValidatableControl){
                boolean validate = ((IFXValidatableControl) node).validate();
                validacioExitosa = validacioExitosa&&validate;
            }
        }


        return validacioExitosa;
    }
}
