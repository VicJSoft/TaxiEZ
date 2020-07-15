package controllers;

import com.jfoenix.controls.*;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import controllers.crudsControllers.ClientesCrudController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Cliente;
import models.Direccion;
import models.interfaces.AddRegistro;
import models.interfaces.IAccion;
import models.interfaces.Registro;
import services.sql.ClienteSQL;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientesController implements Initializable,IAccion {

    @FXML
    private AnchorPane fondo_clientes;

    @FXML
    private JFXTextField txtF_buscar;

    @FXML
    private JFXTreeTableView<Cliente> table_view_clientes;

    @FXML
    private TreeTableColumn<Cliente, String> column_nombre;

    @FXML
    private TreeTableColumn<Cliente,Direccion> column_direccion;

    @FXML
    private TreeTableColumn<Cliente,Direccion>  column_telefono;

    @FXML
    private TreeTableColumn<Cliente,Direccion>  column_observaciones;

    @FXML
    private Label label_clientes;

    @FXML
    private JFXButton button_agregarCliente;

    @FXML
    private JFXButton button_eliminarCliente;

    @FXML
    private JFXButton button_actualizarCliente;

    ObservableList<Cliente> listaServicios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.column_nombre.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        this.column_telefono.setCellValueFactory(new TreeItemPropertyValueFactory("numero"));
        this.column_direccion.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        this.column_observaciones.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        listaServicios = new ClienteSQL().getClientes();

        TreeItem<Cliente> clienteRecursiveTreeItem = new RecursiveTreeItem<>(listaServicios, (recursiveTreeObject) -> recursiveTreeObject.getChildren());
        this.table_view_clientes.setRoot(clienteRecursiveTreeItem);
        this.table_view_clientes.setShowRoot(false);
        this.column_direccion.setCellFactory(new Callback<TreeTableColumn<Cliente, Direccion>, TreeTableCell<Cliente, Direccion>>() {
            public TreeTableCell<Cliente, Direccion> call(TreeTableColumn<Cliente, Direccion> param) {

                TreeTableCell<Cliente, Direccion> cell = new TreeTableCell<Cliente, Direccion>() {
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

        //definir una fila de fabrica.
        table_view_clientes.setRowFactory((param) -> {
            // TableRow<Empleados> row = new TableRow<>();
            JFXTreeTableRow<Cliente> row = new JFXTreeTableRow<>();

          //  row.setPrefHeight(row.getTreeItem().getGraphic().);

            row.setOnMouseClicked(event->{

                //si un registro es seleccionado con 1 o 2 clic
                if(! row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2){
                    Cliente clickedRow = row.getItem();
              //      btnDelete_Cliente.disableProperty().set(false);
                //    btnEdit_Cliente.disableProperty().set(false);
                  //  System.out.println(clickedRow.getNombre());
                    //abrirá la ventana para edición
                    button_actualizarCliente.fire();

                }else
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {

                    ////  Empleados clickedRow = row.getItem();
                  //  btnDelete_Cliente.disableProperty().set(false);
                   // btnEdit_Cliente.disableProperty().set(false);
                   // //System.out.println(clickedRow.getNombre());
                }


            });

            return row;
        });
    }

    @Override
    public void accionPrimaria() {
        this.button_agregarCliente.fire();
    }

    @Override
    public void accionSecundaria() {
        this.button_eliminarCliente.fire();
    }

    @Override
    public void accionTerciaria() {
        this.button_actualizarCliente.fire();
    }

    @FXML
    void btnActualizarCliente_OnAction(ActionEvent event) {

        abrirVentanaCrud(event, new AddRegistro(table_view_clientes.getSelectionModel().getSelectedItem().getValue()) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {
                if(new ClienteSQL().actualizar((Cliente) registro)){
                    table_view_clientes.getSelectionModel().getSelectedItem().setValue((Cliente)registro);
                    return true;
                }
                return false;
            }
        });
    }

    @FXML
    void btnAddCliente_OnAction(ActionEvent event) {
        abrirVentanaCrud(event, new AddRegistro(null) {
            @Override
            public boolean addRegistro(Registro registro,Stage stage) {
                if(new ClienteSQL().insertar((Cliente) registro, stage)) {
                    listaServicios.add((Cliente) registro);
                    table_view_clientes.getSelectionModel().selectLast();
                    return true;
                }
                return false;

            }
        });
    }

    @FXML
    void btnDeleteCliente_OnAction(ActionEvent event) {
        Cliente clienteSelected = table_view_clientes.getSelectionModel().getSelectedItem().getValue();
        if(new ClienteSQL().eliminar(clienteSelected)){
            listaServicios.remove(clienteSelected);
        }
    }

    private void abrirVentanaCrud(ActionEvent event, AddRegistro addRegistro){
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/Cruds/ClientesCRUD.fxml"));
            StackPane contenedorCRUDClientes = controladorLoader.load();
            ClientesCrudController clientesCrudController = controladorLoader.getController();

            clientesCrudController.setAddRegistroListener(addRegistro);

            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Taxis añadir");
            Scene scene = new Scene(contenedorCRUDClientes);
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
                @Override
                public void run() {
                    FXRobot robot = FXRobotFactory.createRobot(scene);
                    robot.keyPress(KeyCode.TAB);
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(this.button_actualizarCliente.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

