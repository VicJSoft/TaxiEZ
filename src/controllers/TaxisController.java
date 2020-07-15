package controllers;
import com.jfoenix.controls.*;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import controllers.crudsControllers.EmpleadosCrudController;
import controllers.crudsControllers.TaxisCrudController;
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
import models.Taxi;
import models.Taxista;
import models.interfaces.Registro;
import models.interfaces.AddRegistro;
import models.interfaces.IAccion;
import services.sql.TaxisSQL;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class TaxisController implements Initializable,IAccion {
    @FXML
    private Label label_taxis;

    @FXML
    private JFXTextField txt_buscarTaxis;

    @FXML
    private JFXButton button_agregarTaxi;

    @FXML
    private JFXButton button_eliminarTaxi;

    @FXML
    private JFXButton button_actualizarTaxi;

    @FXML
    private JFXTreeTableView<Taxi> table_taxis;

    @FXML
    private TreeTableColumn<Taxi, String> column_unidad;

    @FXML
    private TreeTableColumn<Taxi, String> column_marca;

    @FXML
    private TreeTableColumn<Taxi, String> column_modelo;

    @FXML
    private TreeTableColumn<Taxi, String> column_placa;

    @FXML
    private TreeTableColumn<Taxi, Taxista> column_taxista;

    ObservableList<Taxi> listaTaxis = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.column_marca.setCellValueFactory(new TreeItemPropertyValueFactory("marca"));
        this.column_unidad.setCellValueFactory(new TreeItemPropertyValueFactory("idUnidad"));
        this.column_placa.setCellValueFactory(new TreeItemPropertyValueFactory("placa"));
        this.column_modelo.setCellValueFactory(new TreeItemPropertyValueFactory("modelo"));
        this.column_taxista.setCellValueFactory(new TreeItemPropertyValueFactory("taxista"));
        this.column_taxista.setCellFactory(new Callback<TreeTableColumn<Taxi, Taxista>, TreeTableCell<Taxi, Taxista>>() {
            @Override
            public TreeTableCell<Taxi, Taxista> call(TreeTableColumn<Taxi, Taxista> param) {
                TreeTableCell<Taxi,Taxista> cell = new TreeTableCell<Taxi,Taxista>(){
                    @Override
                    protected void updateItem(Taxista item, boolean empty) {
                        super.updateItem(item, empty);

                        if(item!=null){
                            this.setText(item.getIdTaxista() + " "+item.getNombre());
                        }

                    }
                };

                return cell;
            }
        });

        listaTaxis = new TaxisSQL().getTaxis();
        TreeItem<Taxi> clienteRecursiveTreeItem = new RecursiveTreeItem<>(listaTaxis, (recursiveTreeObject) -> recursiveTreeObject.getChildren());
        this.table_taxis.setRoot(clienteRecursiveTreeItem);
        this.table_taxis.setShowRoot(false);

        table_taxis.setRowFactory((param) -> {
            JFXTreeTableRow<Taxi> row = new JFXTreeTableRow<>();

            row.setOnMouseClicked(event->{

                //si un registro es seleccionado con 1 o 2 clic
                if(! row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2){
                    // Empleado clickedRow = row.getItem();
                    //      btnDelete_Cliente.disableProperty().set(false);
                    //    btnEdit_Cliente.disableProperty().set(false);
                    //  System.out.println(clickedRow.getNombre());
                    //abrirá la ventana para edición
                    button_actualizarTaxi.fire();

                }else
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {


                }

            });

            return row;
        });

    }
    @Override
    public void accionPrimaria() {
        this.button_agregarTaxi.fire();
    }

    @Override
    public void accionSecundaria() {
        this.button_eliminarTaxi.fire();
    }

    @Override
    public void accionTerciaria() {
        this.button_actualizarTaxi.fire();
    }


    @FXML
    void btnAgregarTaxi_OnAction(ActionEvent event) throws IOException {
        //listaTaxis.add(new Taxi(1,"mar","model","placaaaa",new Taxista(2, LocalDate.now(),"telef","nomb","obser",null)));

        abrirVentanaCrud(event, new AddRegistro(null) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {

                if(new TaxisSQL().insertar((Taxi) registro)){
                    listaTaxis.add((Taxi) registro);
                    //table_taxis.getSelectionModel().selectLast();
                    return true;
                }
                return false;


            }
        });


    }
    @FXML
    void btnActualizarTaxi_OnAction(ActionEvent event) {

        abrirVentanaCrud(event, new AddRegistro(table_taxis.getSelectionModel().getSelectedItem().getValue()) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {
                if(new TaxisSQL().actualizar((Taxi) registro)) {
                    table_taxis.getSelectionModel().getSelectedItem().setValue((Taxi) registro);
                    return true;
                }
                return false;
            }
        }).textField_unidad.setDisable(true);
    }

    @FXML
    void btnEliminarTaxi_OnAction(ActionEvent event) {

        Taxi empleado = table_taxis.getSelectionModel().getSelectedItem().getValue();

        if(new TaxisSQL().eliminar(empleado)){
            listaTaxis.remove(empleado);
        }
    }


    private TaxisCrudController abrirVentanaCrud(ActionEvent event, AddRegistro addRegistro){

        TaxisCrudController taxisCrudController = null;
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/Cruds/TaxisCRUD.fxml"));
            AnchorPane contenedor = controladorLoader.load();
            taxisCrudController = controladorLoader.getController();

            taxisCrudController.setAddRegistroListener(addRegistro);

            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Taxis");
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
            primaryStage.initOwner(this.button_actualizarTaxi.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  taxisCrudController;
    }


}
