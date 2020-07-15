package controllers;

import com.jfoenix.controls.*;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import controllers.crudsControllers.EmpleadosCrudController;
import controllers.crudsControllers.TaxistasCrudController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Direccion;
import models.Empleado;
import models.Taxista;
import models.interfaces.AddRegistro;
import models.interfaces.IAccion;
import models.interfaces.Registro;
import services.sql.TaxistaSQL;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaxistasController implements Initializable, IAccion {
    @FXML
    private AnchorPane fondo_taxistas;

    @FXML
    private Label label_titulo_taxistas;

    @FXML
    private JFXTextField txt_buscar;

    @FXML
    private JFXTreeTableView<Taxista> table_taxistas;

    @FXML
    private TreeTableColumn<Taxista, String> column_nombre_taxistas;

    @FXML
    private TreeTableColumn<Taxista, String>  column_telefono_taxistas;

    @FXML
    private TreeTableColumn<Taxista, Direccion>  column_direccion_taxistas;

    @FXML
    private TreeTableColumn<Taxista, String>  column_observaciones_taxistas;

    @FXML
    private TreeTableColumn<Taxista, String> column_fechaNac_taxistas;

    @FXML
    private JFXButton button_agregarTaxista;

    @FXML
    private JFXButton button_eliminarTaxista;

    @FXML
    private JFXButton button_actualizarTaxista;

    ObservableList<Taxista> listaTaxistas = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listaTaxistas = new TaxistaSQL().getTaxistas();
        this.column_nombre_taxistas.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        this.column_telefono_taxistas.setCellValueFactory(new TreeItemPropertyValueFactory("telefono"));
        this.column_direccion_taxistas.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        this.column_observaciones_taxistas.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        this.column_fechaNac_taxistas.setCellValueFactory(new TreeItemPropertyValueFactory("fechaNac"));

        this.column_direccion_taxistas.setCellFactory(new Callback<TreeTableColumn<Taxista, Direccion>, TreeTableCell<Taxista, Direccion>>() {
            public TreeTableCell<Taxista, Direccion> call(TreeTableColumn<Taxista, Direccion> param) {

                TreeTableCell<Taxista, Direccion> cell = new TreeTableCell<Taxista, Direccion>() {
                    protected void updateItem(Direccion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setText("Calle: " + item.getCalle() + " Colonia: " + item.getColonia()+ " \nNum ext: " + item.getNumExt() + (item.getNumInt() == null ? "" : " Num int: " + item.getNumInt()) );
                            //this.setPrefHeight(35);

                        } else {
                            this.setText((String)null);
                        }

                    }

                };

                return cell;
            }
        });

        TreeItem<Taxista> empleadoTreeItem = new RecursiveTreeItem<>(listaTaxistas, (recursiveTreeObject) -> recursiveTreeObject.getChildren());
        this.table_taxistas.setRoot(empleadoTreeItem);
        this.table_taxistas.setShowRoot(false);

        table_taxistas.setRowFactory((param) -> {
            JFXTreeTableRow<Taxista> row = new JFXTreeTableRow<>();

            row.setOnMouseClicked(event->{

                //si un registro es seleccionado con 1 o 2 clic
                if(! row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2){
                    // Empleado clickedRow = row.getItem();
                    //      btnDelete_Cliente.disableProperty().set(false);
                    //    btnEdit_Cliente.disableProperty().set(false);
                    //  System.out.println(clickedRow.getNombre());
                    //abrirá la ventana para edición
                    button_actualizarTaxista.fire();

                }else
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {


                }


            });

            return row;
        });

    }

    @Override
    public void accionPrimaria() {
        this.button_agregarTaxista.fire();
    }

    @Override
    public void accionSecundaria() {
        this.button_eliminarTaxista.fire();
    }

    @Override
    public void accionTerciaria() {
        this.button_actualizarTaxista.fire();
    }
    @FXML
    void btnActualizarTaxista_OnAction(ActionEvent event) {
        abrirVentanaCrud(event, new AddRegistro(table_taxistas.getSelectionModel().getSelectedItem().getValue()) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {

                if(new TaxistaSQL().actualizar((Taxista) registro)){
                    table_taxistas.getSelectionModel().getSelectedItem().setValue((Taxista) registro);
                    return  true;
                }

                return false;
            }
        });
    }

    @FXML
    void btnAgregarTaxista_OnAction(ActionEvent event) {
        abrirVentanaCrud(event, new AddRegistro(null) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {
                if(new TaxistaSQL().insertar((Taxista) registro)) {

                    listaTaxistas.add((Taxista) registro);
                    table_taxistas.getSelectionModel().selectLast();
                    return true;
                }
                return false;
            }
        });
    }

    @FXML
    void btnEliminarTaxista_OnAction(ActionEvent event) {

        Taxista taxista = table_taxistas.getSelectionModel().getSelectedItem().getValue();
        if(new TaxistaSQL().eliminar(taxista)){
            listaTaxistas.remove(taxista);
        }
    }

    private void abrirVentanaCrud(ActionEvent event, AddRegistro addRegistro){
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/Cruds/TaxistasCRUD.fxml"));
            AnchorPane contenedor = controladorLoader.load();
            TaxistasCrudController taxistasCrudController = controladorLoader.getController();

            taxistasCrudController.setAddRegistroListener(addRegistro);

            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Taxistas");
            Scene scene = new Scene(contenedor);
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
                @Override
                public void run() {
                    FXRobot robot = FXRobotFactory.createRobot(scene);
                    robot.keyPress(KeyCode.TAB);
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(this.button_actualizarTaxista.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
