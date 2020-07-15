/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.ServicioRegular;
import models.Taxi;
import models.Taxista;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.sql.TaxisSQL;
import services.sql.TaxistaSQL;


/**
 * FXML Controller class
 *
 * @author VAPESIN
 */
public class AsignarUnidadController extends SetAddRegistroListener implements Initializable {

    @FXML
    private AnchorPane ap_tittleBar;
    @FXML
    private ImageView iv_icon;
    @FXML
    private Button btn_cerrar;
    @FXML
    private Label lbl_tittleBar;
    @FXML
    private JFXComboBox<Taxi> cb_unidad;
    @FXML
    private JFXTextArea textField_notas;

    @FXML
    private Button btnAceptar;
    private ServicioRegular servicioRegular;


    Callback<ListView<Taxi>, ListCell<Taxi>> callbackComboTaxi = new Callback<ListView<Taxi>, ListCell<Taxi>>() {

        @Override
        public ListCell<Taxi> call(ListView<Taxi> param) {

            ListCell<Taxi> listCell = new ListCell<Taxi>(){
                @Override
                protected void updateItem(Taxi item, boolean empty) {
                    super.updateItem(item, empty);

                    if(item!=null){
                        this.setText(item.getIdUnidad() +" " + item.getTaxista().getNombre());
                    }else{
                        //setText(null);
                        setGraphic(null);
                    }
                }
            };

            return listCell;
        }
    };

    Callback<ListView<Taxista>, ListCell<Taxista>> callbackComboTaxista = new Callback<ListView<Taxista>, ListCell<Taxista>>() {

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cb_unidad.setCellFactory(callbackComboTaxi);
        cb_unidad.setItems(new TaxisSQL().getTaxis());
        cb_unidad.setButtonCell(callbackComboTaxi.call(null));
        //cb_unidad.getSelectionModel().select(0);
     //   Taxi titem = cb_unidad.getSelectionModel().getSelectedItem();

        // cb_unidad.setButtonCell(callbackCombo.call(null));
        /*JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(cb_unidad.getItems());
        autoCompletePopup.getStyleClass().add("combo-box-popup");
        autoCompletePopup.getStyleClass().add("jfx-combo-box");
         //autoCompletePopup.getStyleClass().add();
        autoCompletePopup.getStyleClass().add("-fx-border-color: #000000;");
        
        //SelectionHandler sets the value of the comboBox
        autoCompletePopup.setSelectionHandler(event -> {
            cb_unidad.setValue(event.getObject());
        });
        TextField editor = cb_unidad.getEditor();
        editor.textProperty().addListener(observable -> {
            //The filter method uses the Predicate to filter the Suggestions defined above
            //I choose to use the contains method while ignoring cases
            autoCompletePopup.filter(item -> item.toLowerCase().contains(editor.getText().toLowerCase()));
            //Hide the autocomplete popup if the filtered suggestions is empty or when the box's original popup is open
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || cb_unidad.showingProperty().get()) {
                autoCompletePopup.hide();
            } 
            else {
                autoCompletePopup.show(editor);
                
            }
        });*/
    }    


    @FXML
    private void btnAceptar_OnAction(ActionEvent event) {
        
        if(!cb_unidad.getSelectionModel().isEmpty()){
            if(enviarRegistro( ((Stage)((Node)event.getSource()).getScene().getWindow()) )){
                ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
            }
        
            
        }
        
    }


    @Override
    public void extraerRegistro(Registro registro) {


        this.servicioRegular = (ServicioRegular) registro;
        this.textField_notas.setText(servicioRegular.getObservaciones());

    }

    @Override
    public Registro guardarCambiosRegistros() {
        //Taxista selectedItem = comboBox_taxista.getSelectionModel().getSelectedItem();
        Taxi titem = cb_unidad.getSelectionModel().getSelectedItem();
        this.servicioRegular.setTaxi(titem);
        this.servicioRegular.setObservaciones(textField_notas.getText());

        return this.servicioRegular;

    }
}
