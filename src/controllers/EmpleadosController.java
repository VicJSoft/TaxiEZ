package controllers;

import com.jfoenix.controls.*;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import controllers.crudsControllers.EmpleadosCrudController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import models.interfaces.AddRegistro;
import models.interfaces.IAccion;
import models.interfaces.Registro;
import services.sql.EmpleadoSQL;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class EmpleadosController  implements Initializable, IAccion {
    @FXML
    private AnchorPane fondo_empleados;

    @FXML
    private JFXTextField txt_buscar;

    @FXML
    private JFXTreeTableView<Empleado> table_empleados;

    @FXML
    private TreeTableColumn<Empleado,String> column_nombre;

    @FXML
    private TreeTableColumn<Empleado,String> column_telefono;

    @FXML
    private TreeTableColumn<Empleado, Direccion> column_direccion;

    @FXML
    private TreeTableColumn<Empleado,String> column_observaciones;

    @FXML
    private TreeTableColumn<Empleado,String> column_fechaNac;

    @FXML
    private Label label_empleados;

    @FXML
    private JFXButton button_agregarEmpleado;

    @FXML
    private JFXButton button_eliminarEmpleado;

    @FXML
    private JFXButton button_actualizarEmpleado;

    ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.column_nombre.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        this.column_telefono.setCellValueFactory(new TreeItemPropertyValueFactory("telefono"));
        this.column_observaciones.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        this.column_direccion.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        this.column_fechaNac.setCellValueFactory(new TreeItemPropertyValueFactory("fechaNac"));

        listaEmpleados = new EmpleadoSQL().getEmpleados();

        this.column_direccion.setCellFactory(new Callback<TreeTableColumn<Empleado, Direccion>, TreeTableCell<Empleado, Direccion>>() {
            public TreeTableCell<Empleado, Direccion> call(TreeTableColumn<Empleado, Direccion> param) {

                TreeTableCell<Empleado, Direccion> cell = new TreeTableCell<Empleado, Direccion>() {
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

        TreeItem<Empleado> empleadoRecursiveTreeItem = new RecursiveTreeItem<>(listaEmpleados, (recursiveTreeObject) -> recursiveTreeObject.getChildren());
        this.table_empleados.setRoot(empleadoRecursiveTreeItem);
        this.table_empleados.setShowRoot(false);

        table_empleados.setRowFactory((param) -> {
            JFXTreeTableRow<Empleado> row = new JFXTreeTableRow<>();

            row.setOnMouseClicked(event->{

                //si un registro es seleccionado con 1 o 2 clic
                if(! row.isEmpty() && event.getButton()== MouseButton.PRIMARY && event.getClickCount() == 2){
                   // Empleado clickedRow = row.getItem();
                    //      btnDelete_Cliente.disableProperty().set(false);
                    //    btnEdit_Cliente.disableProperty().set(false);
                    //  System.out.println(clickedRow.getNombre());
                    //abrirá la ventana para edición
                    button_actualizarEmpleado.fire();

                }else
                if (! row.isEmpty() && event.getButton()==MouseButton.PRIMARY && event.getClickCount() == 1) {


                }


            });

            return row;
        });

    }

    @Override
    public void accionPrimaria() {
        this.button_agregarEmpleado.fire();
    }

    @Override
    public void accionSecundaria() {
        this.button_eliminarEmpleado.fire();
    }

    @Override
    public void accionTerciaria() {
        this.button_actualizarEmpleado.fire();
    }



    @FXML
    void btnAddEmpleado_OnAction(ActionEvent event) {

        abrirVentanaCrud(event, new AddRegistro(null) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {
                if(new EmpleadoSQL().insertar((Empleado) registro)){
                    listaEmpleados.add((Empleado) registro);
                    table_empleados.getSelectionModel().selectLast();
                    return true;
                }
                return false;
            }
        });
    }

    @FXML
    void btnEliminarEmpleado_OnAction(ActionEvent event) {

        Empleado empleado = table_empleados.getSelectionModel().getSelectedItem().getValue();

        if(new EmpleadoSQL().eliminar(empleado)){
            listaEmpleados.remove(empleado);
        }

    }


    @FXML
    void btnActualizarEmpleado_OnAction(ActionEvent event) {
        abrirVentanaCrud(event, new AddRegistro(table_empleados.getSelectionModel().getSelectedItem().getValue()) {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {
                if(new EmpleadoSQL().actualizar((Empleado) registro)){
                    table_empleados.getSelectionModel().getSelectedItem().setValue((Empleado) registro);
                    return true;
                }
                return false;
            }
        });
    }

    private void abrirVentanaCrud(ActionEvent event, AddRegistro addRegistro){
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/Cruds/EmpleadosCRUD.fxml"));
            AnchorPane contenedor = controladorLoader.load();
            EmpleadosCrudController empleadosCrudController = controladorLoader.getController();

            empleadosCrudController.setAddRegistroListener(addRegistro);

            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Empleados");
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
            primaryStage.initOwner(this.button_actualizarEmpleado.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
