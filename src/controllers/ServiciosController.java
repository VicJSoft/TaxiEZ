package controllers;

import com.jfoenix.controls.*;
import com.sun.javafx.robot.FXRobot;
import com.sun.javafx.robot.FXRobotFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import models.interfaces.AddRegistro;
import models.interfaces.IAccion;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import services.sql.ClienteSQL;
import services.sql.ServicioProgramadoSQL;
import services.sql.ServicioRegularSQL;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ServiciosController implements Initializable, IAccion {

    @FXML
    private AnchorPane anchorPaneRoot;

    @FXML
    private JFXTabPane tabPaneServicios;

    @FXML
    private Tab tabServicios;

    @FXML
    private JFXTextField textField_buscarServicios;

    @FXML
    private JFXTreeTableView<ServicioRegular> tablaServicio;

    @FXML
    private TreeTableColumn<ServicioRegular, LocalDateTime> cmServicios_fechaAdd;

    @FXML
    public TreeTableColumn<ServicioRegular,LocalDateTime> cmServicios_fechaServicio;

    @FXML
    private TreeTableColumn<ServicioRegular, LocalDateTime>  cmServicios_FechaAplic;

    @FXML
    private TreeTableColumn<ServicioRegular, Cliente> cmServicios_telefono;

    @FXML
    private TreeTableColumn<ServicioRegular, String> cmServicios_nombre;

    @FXML
    private TreeTableColumn<ServicioRegular, Direccion> cmServicios_direccion;

    @FXML
    private TreeTableColumn<ServicioRegular, String> cmServicios_observaciones;

    @FXML
    private TreeTableColumn<ServicioRegular, Taxi> cmServicios_unidad;

    @FXML
    private TreeTableColumn<ServicioRegular,Empleado> cmServicios_modulador;

    @FXML
    private JFXTextField textField_buscarPendiente;

    @FXML
    private JFXTextField textField_servicioRapido;

    @FXML
    private JFXTextField textField_cantidad;

    @FXML
    private JFXButton btnAddServicio;

    @FXML
    private JFXButton btnCancelServicio;

    @FXML
    private JFXButton btnAplicarServicio;

    @FXML
    private Tab tabServiciosPendientes;

    @FXML
    private JFXTreeTableView<ServicioRegular> tablaServicioPend;

    @FXML
    private TreeTableColumn<ServicioRegular, LocalDateTime> cmServiciosPend_fechaAdd;

    @FXML
    public TreeTableColumn<ServicioRegular, LocalDateTime> cmServiciosPend_fechaServicio;

    @FXML
    private TreeTableColumn<ServicioRegular, Cliente> cmServiciosPend_telefono;

    @FXML
    private TreeTableColumn<ServicioRegular, String> cmServiciosPend_nombre;

    @FXML
    private TreeTableColumn<ServicioRegular, Direccion> cmServiciosPend_direccion;

    @FXML
    private TreeTableColumn<ServicioRegular, String> cmServiciosPend_notas;

    @FXML
    private TreeTableColumn<ServicioRegular, Empleado> cmServiciosPend_modulador;

    @FXML
    private Tab tabServiciosProgramados;

    @FXML
    private JFXTreeTableView<ServiciosProgramado> tablaServicioProgr;

    @FXML
    public TreeTableColumn<ServiciosProgramado, LocalDateTime> cmServiciosProg_FechaAdicion;

    @FXML
    private TreeTableColumn<ServiciosProgramado, LocalDateTime> cmServiciosProg_FechaInicio;

    @FXML
    private TreeTableColumn<ServiciosProgramado, LocalDateTime> cmServiciosProg_FechaUltimoDiaAplicacion;

    @FXML
    private TreeTableColumn<ServiciosProgramado, LocalDateTime> cmServiciosProg_FechaFin;

    @FXML
    private TreeTableColumn<ServiciosProgramado, Cliente> cmServiciosProg_telefono;

    @FXML
    private TreeTableColumn<ServiciosProgramado, String> cmServiciosProg_nombre;

    @FXML
    private TreeTableColumn<ServiciosProgramado, Direccion> cmServiciosProg_dirección;

    @FXML
    private TreeTableColumn<ServiciosProgramado, String> cmServiciosProg_notas;

    @FXML
    private TreeTableColumn<ServiciosProgramado, Empleado> cmServiciosProg_modulador;

    @FXML
    private TreeTableColumn<ServiciosProgramado, String> cmServiciosProg_diasServicio;

    @FXML
    private JFXTextField textField_buscarProgramado;

    @FXML
    private JFXButton btnAddProgramado;

    @FXML
    private JFXButton btnTerminarProgramacion;

    @FXML
    private JFXButton btnAplicarServicioProgramado;

    ObservableList<ServicioRegular> listaServicioRegularesPendientes;
    ObservableList<ServicioRegular> listaServicioRegularesAplicados;
    ObservableList<ServiciosProgramado> listaServicioProgramado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        /*----------------------------------------------------Aplicados ---------------------------------------------------------------*/

        cmServicios_fechaAdd.setCellValueFactory(new TreeItemPropertyValueFactory("fechaAgregacion"));
        cmServicios_fechaServicio.setCellValueFactory(new TreeItemPropertyValueFactory("fechaServicio"));
        cmServicios_FechaAplic.setCellValueFactory(new TreeItemPropertyValueFactory("fechaAplicacion"));
        cmServicios_telefono.setCellValueFactory(new TreeItemPropertyValueFactory("cliente"));//solo numero de telefono
        cmServicios_nombre.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        cmServicios_direccion.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        cmServicios_observaciones.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        cmServicios_unidad.setCellValueFactory(new TreeItemPropertyValueFactory("taxi"));
        cmServicios_modulador.setCellValueFactory(new TreeItemPropertyValueFactory("empleado"));

        cmServicios_fechaAdd.setCellFactory(callbackDateTime);
        cmServicios_fechaServicio.setCellFactory(callbackDateTime);
        cmServicios_FechaAplic.setCellFactory(callbackDateTime);

        cmServicios_telefono.setCellFactory(new Callback<TreeTableColumn<ServicioRegular, Cliente>, TreeTableCell<ServicioRegular, Cliente>>() {
            @Override
            public TreeTableCell<ServicioRegular, Cliente> call(TreeTableColumn<ServicioRegular, Cliente> param) {


                TreeTableCell<ServicioRegular, Cliente> cell = new TreeTableCell<ServicioRegular, Cliente>(){
                    @Override
                    protected void updateItem(Cliente item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getNumero());
                        }
                    }
                };
                return cell;

            }
        });

        cmServicios_direccion.setCellFactory(callbackDireccion);


        cmServicios_unidad.setCellFactory(new Callback<TreeTableColumn<ServicioRegular, Taxi>, TreeTableCell<ServicioRegular, Taxi>>() {
            @Override
            public TreeTableCell<ServicioRegular, Taxi> call(TreeTableColumn<ServicioRegular, Taxi> param) {
                TreeTableCell<ServicioRegular, Taxi> cell = new TreeTableCell<ServicioRegular, Taxi>(){

                    @Override
                    protected void updateItem(Taxi item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getIdUnidad() + " " + item.getTaxista().getNombre());
                        }
                    }

                };
                return cell;
            }
        });

        cmServicios_modulador.setCellFactory(new Callback<TreeTableColumn<ServicioRegular, Empleado>, TreeTableCell<ServicioRegular, Empleado>>() {
            @Override
            public TreeTableCell<ServicioRegular, Empleado> call(TreeTableColumn<ServicioRegular, Empleado> param) {

                TreeTableCell<ServicioRegular, Empleado> cell = new TreeTableCell<ServicioRegular, Empleado>(){

                    @Override
                    protected void updateItem(Empleado item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getIdEmpleado() + " " + item.getNombre());
                        }
                    }

                };
                return cell;

            }
        });

        try {
            this.listaServicioRegularesAplicados = new ServicioRegularSQL().getServiciosRegularesAplicadosyCancelados();
            TreeItem<ServicioRegular> serivicioRegularAplicadosRecursiveTreeItem =
                    new RecursiveTreeItem<>(listaServicioRegularesAplicados, (recursiveTreeObject) -> recursiveTreeObject.getChildren());

            this.tablaServicio.setRoot(serivicioRegularAplicadosRecursiveTreeItem);
            this.tablaServicio.setShowRoot(false);

        } catch (SQLException e) {
            e.printStackTrace();
            //ventana error
        }

        /*----------------------------------------------------FIN APLICADOS ---------------------------------------------------------------*/

        /*---------------------------------------------------- PENDIENTES ---------------------------------------------------------------*/
        this.cmServiciosPend_fechaAdd.setCellValueFactory(new TreeItemPropertyValueFactory("fechaAgregacion"));
        this.cmServiciosPend_fechaServicio.setCellValueFactory(new TreeItemPropertyValueFactory("fechaServicio"));
        this.cmServiciosPend_telefono.setCellValueFactory(new TreeItemPropertyValueFactory("cliente"));
        this.cmServiciosPend_nombre.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        this.cmServiciosPend_direccion.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        this.cmServiciosPend_notas.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        this.cmServiciosPend_modulador.setCellValueFactory(new TreeItemPropertyValueFactory("empleado"));

        this.cmServiciosPend_fechaAdd.setCellFactory(callbackDateTime);
        this.cmServiciosPend_fechaServicio.setCellFactory(callbackDateTime);
        this.cmServiciosPend_direccion.setCellFactory(callbackDireccion);
        this.cmServiciosPend_telefono.setCellFactory(new Callback<TreeTableColumn<ServicioRegular, Cliente>, TreeTableCell<ServicioRegular, Cliente>>() {
            @Override
            public TreeTableCell<ServicioRegular, Cliente> call(TreeTableColumn<ServicioRegular, Cliente> param) {


                TreeTableCell<ServicioRegular,Cliente> cell = new TreeTableCell<ServicioRegular,Cliente>(){
                    @Override
                    protected void updateItem(Cliente item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getNumero());
                        }
                    }
                };
                return cell;

            }
        });

        this.cmServiciosPend_modulador.setCellFactory((param -> {

            TreeTableCell<ServicioRegular,Empleado> cell = new TreeTableCell<ServicioRegular,Empleado>(){
                @Override
                protected void updateItem(Empleado item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item!=null){
                        setText(item.getIdEmpleado() + " " + item.getNombre());
                    }
                }
            };
            return cell;

        }));


        try {
            listaServicioRegularesPendientes = new ServicioRegularSQL().getServiciosRegularesPendientes2();


            TreeItem<ServicioRegular> serivicioRegularPendienteRecursiveTreeItem =
                    new RecursiveTreeItem<>(listaServicioRegularesPendientes, (recursiveTreeObject) -> recursiveTreeObject.getChildren());

            this.tablaServicioPend.setRoot(serivicioRegularPendienteRecursiveTreeItem);
            this.tablaServicioPend.setShowRoot(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }

/*----------------------------------------------------FIN PENDIENTES ---------------------------------------------------------------*/

/*-----------------------------------------------------PROGRAMADOS-------------------------------------------------------------------*/

        this.cmServiciosProg_FechaAdicion.setCellValueFactory(new TreeItemPropertyValueFactory("fechaAgregacion"));
        this.cmServiciosProg_FechaInicio.setCellValueFactory(new TreeItemPropertyValueFactory("fechaInicio"));
        this.cmServiciosProg_FechaUltimoDiaAplicacion.setCellValueFactory(new TreeItemPropertyValueFactory("fechaUltimaAplicacion"));
        this.cmServiciosProg_FechaFin.setCellValueFactory(new TreeItemPropertyValueFactory("fechaFin"));

        this.cmServiciosProg_telefono.setCellValueFactory(new TreeItemPropertyValueFactory("cliente"));
        this.cmServiciosProg_nombre.setCellValueFactory(new TreeItemPropertyValueFactory("nombre"));
        this.cmServiciosProg_dirección.setCellValueFactory(new TreeItemPropertyValueFactory("direccion"));
        this.cmServiciosProg_notas.setCellValueFactory(new TreeItemPropertyValueFactory("observaciones"));
        this.cmServiciosProg_modulador.setCellValueFactory(new TreeItemPropertyValueFactory("empleado"));
        this.cmServiciosProg_diasServicio.setCellValueFactory(new TreeItemPropertyValueFactory("DiasSeleccion"));

        this.cmServiciosProg_FechaAdicion.setCellFactory(callbackDateTimeProgramado);
        this.cmServiciosProg_FechaInicio.setCellFactory(callbackDateTimeProgramado);
        //Fecha es diferente, ya que si la fecha es nula solo debe aparecer en blanco.
        this.cmServiciosProg_FechaFin.setCellFactory(new Callback<TreeTableColumn<ServiciosProgramado, LocalDateTime>, TreeTableCell<ServiciosProgramado, LocalDateTime>>() {
            @Override
            public TreeTableCell<ServiciosProgramado, LocalDateTime> call(TreeTableColumn<ServiciosProgramado, LocalDateTime> param) {

                TreeTableCell<ServiciosProgramado, LocalDateTime> cell = new TreeTableCell<ServiciosProgramado,LocalDateTime>(){
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);//empty hace referencia a row no usado. o sin valo.

                        if(empty)
                            return;
                        if(item!= null && !empty){
                            setText(item.toString().replace('T','\n'));
                        }/*else{
                            //cuando no aparece fechar o es nulla siempre será la columna de fecha aplicación
                            //por lo tanto se mostrará el mensaje:
                            if(!empty)
                                setText("Servicio cancelado.");
                        }*/

                    }
                };
                return cell;

            }
        });
        this.cmServiciosProg_FechaUltimoDiaAplicacion.setCellFactory(new Callback<TreeTableColumn<ServiciosProgramado, LocalDateTime>, TreeTableCell<ServiciosProgramado, LocalDateTime>>() {
            @Override
            public TreeTableCell<ServiciosProgramado, LocalDateTime> call(TreeTableColumn<ServiciosProgramado, LocalDateTime> param) {

                TreeTableCell<ServiciosProgramado, LocalDateTime> cell = new TreeTableCell<ServiciosProgramado,LocalDateTime>(){
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);//empty hace referencia a row no usado. o sin valo.

                        if(empty)
                            return;
                        if(item!= null && !empty){
                            setText(item.toString().replace('T','\n'));
                        }else if(item == null){
                            setText("No se ha aplicado ningún servicio.");
                        }


                    }
                };
                return cell;

            }
        });
        this.cmServiciosProg_dirección.setCellFactory(callbackDireccionProgramado);


        this.cmServiciosProg_modulador.setCellFactory(new Callback<TreeTableColumn<ServiciosProgramado, Empleado>, TreeTableCell<ServiciosProgramado, Empleado>>() {
            @Override
            public TreeTableCell<ServiciosProgramado, Empleado> call(TreeTableColumn<ServiciosProgramado, Empleado> param) {

                TreeTableCell<ServiciosProgramado, Empleado> cell = new TreeTableCell<ServiciosProgramado, Empleado>(){

                    @Override
                    protected void updateItem(Empleado item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getIdEmpleado() + " " + item.getNombre());
                        }
                    }

                };
                return cell;

            }
        });

        this.cmServiciosProg_telefono.setCellFactory(new Callback<TreeTableColumn<ServiciosProgramado, Cliente>, TreeTableCell<ServiciosProgramado, Cliente>>() {
            @Override
            public TreeTableCell<ServiciosProgramado, Cliente> call(TreeTableColumn<ServiciosProgramado, Cliente> param) {


                TreeTableCell<ServiciosProgramado,Cliente> cell = new TreeTableCell<ServiciosProgramado,Cliente>(){
                    @Override
                    protected void updateItem(Cliente item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item!=null){
                            setText(item.getNumero());
                        }
                    }
                };
                return cell;

            }
        });


        try {
            listaServicioProgramado = new ServicioProgramadoSQL().getServiciosProgramados();


            TreeItem<ServiciosProgramado> serviciosProgramadoRecursiveTreeItem =
                    new RecursiveTreeItem<>(listaServicioProgramado, (recursiveTreeObject) -> recursiveTreeObject.getChildren());

            this.tablaServicioProgr.setRoot(serviciosProgramadoRecursiveTreeItem);
            this.tablaServicioProgr.setShowRoot(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }


/*-----------------------------------------------------FIN PROGRAMADOS-------------------------------------------------------------------*/



/*-----------------------------------------------------SORT POLICIES-------------------------------------------------------------------*/
        tablaServicioPend.setSortPolicy(new Callback<TreeTableView<ServicioRegular>, Boolean>()
        {
            @Override
            public Boolean call(TreeTableView<ServicioRegular> param)
            {
                Comparator<TreeItem<ServicioRegular>> comparator = new Comparator<TreeItem<ServicioRegular>>()
                {
                    @Override
                    public int compare(TreeItem<ServicioRegular> o1, TreeItem<ServicioRegular> o2)
                    {
                        ServicioRegular s1 = o1.getValue();
                        ServicioRegular s2 = o2.getValue();

                        if(s1.getFechaAgregacion().isBefore(s2.getFechaAgregacion()))// la fecha 1 es menor que la fecha 2
                        {
                            return 1;
                        }
                        else if(s1.getFechaAgregacion().isEqual(s2.getFechaAgregacion()))//iguales
                        {
                            return 0;
                        }
                        else// fecha 1 es mayor que fecha 2
                        {
                            return -1;
                        }
                    }
                };

                ObservableList<TreeItem<ServicioRegular>> children = tablaServicioPend.getRoot().getChildren();
                FXCollections.sort(children,comparator);
                return true;
            }
        });

        tablaServicio.setSortPolicy( new Callback<TreeTableView<ServicioRegular>, Boolean>()
        {
            @Override
            public Boolean call(TreeTableView<ServicioRegular> param)
            {
                Comparator<TreeItem<ServicioRegular>> comparator = new Comparator<TreeItem<ServicioRegular>>()
                {
                    @Override
                    public int compare(TreeItem<ServicioRegular> o1, TreeItem<ServicioRegular> o2)
                    {
                        ServicioRegular s1 = o1.getValue();
                        ServicioRegular s2 = o2.getValue();

                        if(s1.getFechaAgregacion().isBefore(s2.getFechaAgregacion()))// la fecha 1 es menor que la fecha 2
                        {
                            return 1;
                        }
                        else if(s1.getFechaAgregacion().isEqual(s2.getFechaAgregacion()))//iguales
                        {
                            return 0;
                        }
                        else// fecha 1 es mayor que fecha 2
                        {
                            return -1;
                        }
                    }
                };

                ObservableList<TreeItem<ServicioRegular>> children = tablaServicio.getRoot().getChildren();
                FXCollections.sort(children,comparator);
                return true;
            }
        });

        tablaServicioProgr.setSortPolicy(new Callback<TreeTableView<ServiciosProgramado>, Boolean>() {
            @Override
            public Boolean call(TreeTableView<ServiciosProgramado> param) {
                Comparator<TreeItem<ServiciosProgramado>> comparator = new Comparator<TreeItem<ServiciosProgramado>>()
                {
                    @Override
                    public int compare(TreeItem<ServiciosProgramado> o1, TreeItem<ServiciosProgramado> o2)
                    {
                        ServiciosProgramado s1 = o1.getValue();
                        ServiciosProgramado s2 = o2.getValue();

                        if(s1.getFechaAgregacion().isBefore(s2.getFechaAgregacion()))// la fecha 1 es menor que la fecha 2
                        {
                            return 1;
                        }
                        else if(s1.getFechaAgregacion().isEqual(s2.getFechaAgregacion()))//iguales
                        {
                            return 0;
                        }
                        else// fecha 1 es mayor que fecha 2
                        {
                            return -1;
                        }
                    }
                };

                ObservableList<TreeItem<ServiciosProgramado>> children = tablaServicioProgr.getRoot().getChildren();
                FXCollections.sort(children,comparator);
                return true;

            }
        });

        tablaServicioProgr.setRowFactory(new Callback<TreeTableView<ServiciosProgramado>, TreeTableRow<ServiciosProgramado>>() {
            @Override
            public TreeTableRow<ServiciosProgramado> call(TreeTableView<ServiciosProgramado> param) {

                return new TreeTableRow<ServiciosProgramado>(){
                    @Override
                    protected void updateItem(ServiciosProgramado item, boolean empty) {
                        if(!empty && item!=null)super.updateItem(item, empty);
                    }
                };
            }
        });

        /*----------------------------------------------------- FIN SORT POLICIES-------------------------------------------------------------------*/

        /*-----------------------------------------------------   TEXT PROPERTY CHANGE -------------------------------------------------------------------*/

        textField_buscarServicios.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                tablaServicio.setPredicate(new Predicate<TreeItem<ServicioRegular>>() {
                    @Override
                    public boolean test(TreeItem<ServicioRegular> servicioRegularTreeItem) {

                        Boolean flag  = servicioRegularTreeItem.getValue().getCliente().getNumero().contains(newValue);

                        return flag;
                    }
                });

            }
        });


        textField_buscarPendiente.textProperty().addListener((observable, oldValue, newValue) -> tablaServicioPend.setPredicate(new Predicate<TreeItem<ServicioRegular>>() {
            @Override
            public boolean test(TreeItem<ServicioRegular> servicioRegularTreeItem) {

                Boolean flag  = servicioRegularTreeItem.getValue().getCliente().getNumero().contains(newValue);

                return flag;
            }
        }));
        textField_buscarProgramado.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tablaServicioProgr.setPredicate(new Predicate<TreeItem<ServiciosProgramado>>() {
                    @Override
                    public boolean test(TreeItem<ServiciosProgramado> serviciosProgramadoTreeItem) {
                        Boolean flag  = serviciosProgramadoTreeItem.getValue().getCliente().getNumero().contains(newValue);

                        return flag;
                    }
                });
            }
        });

        textField_buscarServicios.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ESCAPE){
                textField_buscarServicios.clear();
            }

        });
        textField_buscarPendiente.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ESCAPE){
                textField_buscarPendiente.clear();
            }

        });
        textField_buscarProgramado.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ESCAPE){
                textField_buscarProgramado.clear();
            }

        });

        textField_servicioRapido.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ENTER){
                textField_cantidad.requestFocus();
            }

        });
        textField_cantidad.setOnKeyPressed(event -> {

            if(event.getCode() == KeyCode.ENTER){

                int cantidad = 1;
                if(!textField_cantidad.getText().isEmpty()) {
                    cantidad = Integer.parseInt(textField_cantidad.getText());
                }

                    try {
                        Cliente existe = new ClienteSQL().existe(textField_servicioRapido.getText());
                        if(existe == null){
                            btnAddServicio.fire();//si no existe añadir manualmente.
                        }else{

                            while(cantidad>0){
                                ServicioRegular servicioRegularRapido = new ServicioRegular(existe);

                                if(new ServicioRegularSQL().insertarServicioRegular(servicioRegularRapido)){
                                    listaServicioRegularesPendientes.add(servicioRegularRapido);
                                    tablaServicioPend.sort();
                                    tablaServicioPend.refresh();

                                }
                                cantidad--;
                            }


                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                textField_cantidad.clear();
                    textField_servicioRapido.clear();

            }

        });




        /*----------------------------------------------------- FIN TEXT PROPERTY CHANGE -------------------------------------------------------------------*/




    }



    //TODO Refactorizar callbacks a un metodo esttico con generics.

    Callback<TreeTableColumn<ServiciosProgramado, Direccion>, TreeTableCell<ServiciosProgramado, Direccion>> callbackDireccionProgramado = new Callback<TreeTableColumn<ServiciosProgramado, Direccion>, TreeTableCell<ServiciosProgramado, Direccion>>() {
        @Override
        public TreeTableCell<ServiciosProgramado, Direccion> call(TreeTableColumn<ServiciosProgramado, Direccion> param) {

            TreeTableCell<ServiciosProgramado, Direccion> cell = new TreeTableCell<ServiciosProgramado, Direccion>() {
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
    };

    Callback<TreeTableColumn<ServiciosProgramado, LocalDateTime>, TreeTableCell<ServiciosProgramado, LocalDateTime>> callbackDateTimeProgramado = new Callback<TreeTableColumn<ServiciosProgramado, LocalDateTime>, TreeTableCell<ServiciosProgramado, LocalDateTime>>() {
        @Override
        public TreeTableCell<ServiciosProgramado, LocalDateTime> call(TreeTableColumn<ServiciosProgramado, LocalDateTime> param) {
            TreeTableCell<ServiciosProgramado, LocalDateTime> cell = new TreeTableCell<ServiciosProgramado,LocalDateTime>(){
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);//empty hace referencia a row no usado. o sin valo.

                    if(item!= null && !empty){
                        setText(item.toString().replace('T','\n'));
                    }else{
                        //cuando no aparece fechar o es nulla siempre será la columna de fecha aplicación
                        //por lo tanto se mostrará el mensaje:
                        if(!empty)
                            setText("Servicio cancelado.");
                    }

                }
            };
            return cell;
        }
    };

    Callback<TreeTableColumn<ServicioRegular, Direccion>, TreeTableCell<ServicioRegular, Direccion>> callbackDireccion = new Callback<TreeTableColumn<ServicioRegular, Direccion>, TreeTableCell<ServicioRegular, Direccion>>() {
        @Override
        public TreeTableCell<ServicioRegular, Direccion> call(TreeTableColumn<ServicioRegular, Direccion> param) {

            TreeTableCell<ServicioRegular, Direccion> cell = new TreeTableCell<ServicioRegular, Direccion>() {
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
    };

    Callback<TreeTableColumn<ServicioRegular, LocalDateTime>, TreeTableCell<ServicioRegular, LocalDateTime>> callbackDateTime = new Callback<TreeTableColumn<ServicioRegular, LocalDateTime>, TreeTableCell<ServicioRegular, LocalDateTime>>() {
        @Override
        public TreeTableCell<ServicioRegular, LocalDateTime> call(TreeTableColumn<ServicioRegular, LocalDateTime> param) {
            TreeTableCell<ServicioRegular, LocalDateTime> cell = new TreeTableCell<ServicioRegular,LocalDateTime>(){
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);//empty hace referencia a row no usado. o sin valo.

                    if(item!= null && !empty){
                        setText(item.toString().replace('T','\n'));
                    }else{
                        //cuando no aparece fechar o es nulla siempre será la columna de fecha aplicación
                        //por lo tanto se mostrará el mensaje:
                        if(!empty)
                            setText("Servicio cancelado.");
                    }

                }
            };
            return cell;
        }
    };


    Callback<TreeTableView<ServicioRegular>, Boolean> callbackSortDate = new Callback<TreeTableView<ServicioRegular>, Boolean>()
    {
        @Override
        public Boolean call(TreeTableView<ServicioRegular> param)
        {
            Comparator<TreeItem<ServicioRegular>> comparator = new Comparator<TreeItem<ServicioRegular>>()
            {
                @Override
                public int compare(TreeItem<ServicioRegular> o1, TreeItem<ServicioRegular> o2)
                {
                    ServicioRegular s1 = o1.getValue();
                    ServicioRegular s2 = o2.getValue();

                    if(s1.getFechaAgregacion().isBefore(s2.getFechaAgregacion()))// la fecha 1 es menor que la fecha 2
                    {
                        return 1;
                    }
                    else if(s1.getFechaAgregacion().isEqual(s2.getFechaAgregacion()))//iguales
                    {
                        return 0;
                    }
                    else// fecha 1 es mayor que fecha 2
                    {
                        return -1;
                    }
                }
            };
            return true;
        }
    };

    @Override
    public void accionPrimaria() {
        if(tabPaneServicios.getSelectionModel().getSelectedItem() == tabServiciosProgramados){
            btnAddProgramado.fire();
        }else{
            btnAddServicio.fire();
        }
    }

    @Override
    public void accionSecundaria() {
        if(tabPaneServicios.getSelectionModel().getSelectedItem() == tabServiciosProgramados){
            btnTerminarProgramacion.fire();
        }else{
            btnCancelServicio.fire();
        }
    }

    @Override
    public void accionTerciaria() {
        if(tabPaneServicios.getSelectionModel().getSelectedItem() == tabServiciosProgramados){
            btnAplicarServicioProgramado.fire();
        }else{
            btnAplicarServicio.fire();
        }
    }

    @FXML
    void btnAddServicioNormal_OnAction(ActionEvent event) {

        abrirVentanaCrud(event, new AddRegistro() {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {

                ServicioRegular servicioRegular= (ServicioRegular) registro;
                Direccion direccion = servicioRegular.getDireccion();
                Cliente cliente = servicioRegular.getCliente();
                //si el id es 0 entonces esa direccón es nueva, por lo tanto se introduce primero a la DB.
               /* if(direccion.getIdDireccion()==0){
                    if(!new DireccionSQL().insertarDireccion(direccion)){
                        return false;
                    }
                }*/
                //si el ciente es igual a null significa que cuando se busco el numero para añadir los datos de dirección autamaticamente.
                //Entonces se debe crear un nuevo cliente con los mismos datos de dirección que contiene el servicio
                if(cliente == null){

                    cliente =
                            new Cliente(0,servicioRegular.getTelefonoAux(),true,servicioRegular.getNombre(),servicioRegular.getObservaciones(),direccion);

                    new ClienteSQL().insertar(cliente, ((Stage)((Node)event.getSource()).getScene().getWindow()));
                    servicioRegular.setCliente(cliente);
                }


                if(new ServicioRegularSQL().insertarServicioRegular((ServicioRegular) registro)){
                    listaServicioRegularesPendientes.add((ServicioRegular) registro);
                    tablaServicioPend.sort();
                    return true;
                }
                return false;
            }
        },"/views/Cruds/ServicioRegularCRUD.fxml");

    }

    @FXML
    void btnAddServicioProgramado_OnAction(ActionEvent event) {

        abrirVentanaCrud(event, new AddRegistro() {
            @Override
            public boolean addRegistro(Registro registro, Stage stage) {

                ServiciosProgramado serviciosProgramado = (ServiciosProgramado) registro;
                Direccion direccion = serviciosProgramado.getDireccion();
                Cliente cliente = serviciosProgramado.getCliente();

                if(cliente == null){
                    cliente =
                            new Cliente(0,serviciosProgramado.getTelefonoAux(),true,serviciosProgramado.getNombre(),serviciosProgramado.getObservaciones(),direccion);

                    new ClienteSQL().insertar(cliente, ((Stage)((Node)event.getSource()).getScene().getWindow()));
                    serviciosProgramado.setCliente(cliente);
                }

                if(new ServicioProgramadoSQL().insertarServicioProgramado((ServiciosProgramado) registro)){

                    listaServicioProgramado.add((ServiciosProgramado) registro);
                    tablaServicioProgr.refresh();
                    tablaServicioProgr.sort();

                    //tablaServicioProgr.refresh();
                    return true;

                }

                return false;

            }
        }, "/views/Cruds/ServicioProgramadoCRUD.fxml");


        System.out.println("Add servicio programado .");
    }

    @FXML
    void btnAplicarServicioNormal_OnAction(ActionEvent event) {

        //si no hay selección, a pastar.
        if(tablaServicioPend.getSelectionModel().isEmpty()){
            return;
        }

        try {
            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/AsignarUnidad.fxml"));
            AnchorPane contenedorAsignarUnidad = controladorLoader.load();
            AsignarUnidadController asignarUnidadController = controladorLoader.getController();

         //   listaServicioRegularesPendientes.remove(tablaServicioPend.getSelectionModel().getSelectedItem().getValue());
           // if(true)
             //   return;

            asignarUnidadController.setAddRegistroListener(new AddRegistro(tablaServicioPend.getSelectionModel().getSelectedItem().getValue()) {
                @Override
                public boolean addRegistro(Registro registro, Stage stage) {

                    //regresa la instancia que mandamos pero con ID Direccion = true;
                    ServicioRegular SRAplicado = (ServicioRegular) registro;
                    ServicioRegular servicioRegularPendientePorAplicar = tablaServicioPend.getSelectionModel().getSelectedItem().getValue();
                    //   ServicioRegular servicioRegularPostModificiacion = tablaServicioPend.getSelectionModel().getSelectedItem().getValue();
                    try {
                        if(new ServicioRegularSQL().aplicarServicioRegular(SRAplicado)){

                            TreeItem<ServicioRegular> selectedItem = tablaServicioPend.getSelectionModel().getSelectedItem();

                            listaServicioRegularesPendientes.remove(servicioRegularPendientePorAplicar);
                            tablaServicioPend.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedItem);
                            tablaServicioPend.refresh();

                            listaServicioRegularesAplicados.add(servicioRegularPendientePorAplicar);
                            //tablaServicioPend.getSelectionModel().getSelectedItem().setValue(servicioRegularPostModificiacion);
                            tablaServicio.sort();
                            return true;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    return false;
                }

            });
            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Asignar unidad");
            Scene scene = new Scene(contenedorAsignarUnidad);
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
                @Override
                public void run() {
                    FXRobot robot = FXRobotFactory.createRobot(scene);
                    robot.keyPress(KeyCode.TAB);
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(this.textField_servicioRapido.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Aplicar servicio normal.");
    }

    @FXML
    void btnAplicarServicioProgramado_OnAction(ActionEvent event) {
        //si no hay selección, a pastar.
        if(tablaServicioProgr.getSelectionModel().isEmpty()){
            return;
        }
        if(tablaServicioProgr.getSelectionModel().getSelectedItem().getValue().getFechaFin()!=null){

            //acabó la programación de este servicio. No puede generar ningún servicio regular a partir de este servicio programado.
            return;
        }


        try {
            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource("/views/AsignarUnidad.fxml"));
            AnchorPane contenedorAsignarUnidad = controladorLoader.load();
            AsignarUnidadController asignarUnidadController = controladorLoader.getController();

            //   listaServicioRegularesPendientes.remove(tablaServicioPend.getSelectionModel().getSelectedItem().getValue());
            // if(true)
            //   return;
            TreeItem<ServiciosProgramado> serviciosProgramadoTreeItem = tablaServicioProgr.getSelectionModel().getSelectedItem();
            ServiciosProgramado serviciosProgramadoSelected = tablaServicioProgr.getSelectionModel().getSelectedItem().getValue();
            ServicioRegular servicioRegularGenerado = serviciosProgramadoSelected.generarServicioRegular();


            new ServicioRegularSQL().insertarServicioRegular(servicioRegularGenerado);//lo inserta en servicios regulares pendientes.
            //y se manda para que se apliqué el servicio pidiendo unidad para asignar.

            asignarUnidadController.setAddRegistroListener(new AddRegistro(servicioRegularGenerado) {
                @Override
                public boolean addRegistro(Registro registro, Stage stage) {

                    //llega la misma instancia que mandé, pero con un taxi asignado, así que ese lo aplico.
                    try {
                        TreeItem<ServiciosProgramado> serviciosProgramadoTreeItem = tablaServicioProgr.getSelectionModel().getSelectedItem();
                        new ServicioRegularSQL().aplicarServicioRegular((ServicioRegular) registro);
                        listaServicioRegularesAplicados.add((ServicioRegular) registro);
                        tablaServicio.sort();

                        //ya que se aplicó el servicio regular, se debe enlazar por medio de "aplicarServicioProgramado.
                        ServiciosProgramado serviciosProgramadoSelected = tablaServicioProgr.getSelectionModel().getSelectedItem().getValue();
                        new ServicioProgramadoSQL().aplicarServicioProgramado(serviciosProgramadoSelected, (ServicioRegular) registro);

                        serviciosProgramadoTreeItem.setValue(null);
                        serviciosProgramadoTreeItem.setValue(serviciosProgramadoSelected);

                       // tablaServicioProgr.refresh();



                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    return false;
                }

            });
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Asignar unidad");
            Scene scene = new Scene(contenedorAsignarUnidad);
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
                @Override
                public void run() {
                    FXRobot robot = FXRobotFactory.createRobot(scene);
                    robot.keyPress(KeyCode.TAB);
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(this.textField_servicioRapido.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();


        }
        catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Aplicar servicio programado.");
    }

    @FXML
    void btnCancelServicioNormal_OnAction(ActionEvent event) {

        try {
            ServicioRegular servicioRegularSeleccionado = tablaServicioPend.getSelectionModel().getSelectedItem().getValue();
            if(servicioRegularSeleccionado!=null){
                if(new ServicioRegularSQL().cancelarServicioPendiente(servicioRegularSeleccionado.getIdServicio())){
                    listaServicioRegularesPendientes.remove(servicioRegularSeleccionado);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Cancel servicio normal.");
    }


    @FXML
    void btnFinalizarServicioProgramado_OnAction(ActionEvent event) {

        if(!tablaServicioProgr.getSelectionModel().isEmpty()) {

            ServiciosProgramado serviciosProgramado = tablaServicioProgr.getSelectionModel().getSelectedItem().getValue();

            try {
                if(serviciosProgramado.getFechaFin()==null)
                    if(new ServicioProgramadoSQL().terminarProgramacionServicio(serviciosProgramado)){

                        tablaServicioProgr.getSelectionModel().getSelectedItem().setValue(null);
                        tablaServicioProgr.getSelectionModel().getSelectedItem().setValue(serviciosProgramado);
                      // if(listaServicioProgramado.size()<30)
                            //tablaServicioProgr.refresh();

                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("FInalizar servicio programado.");
    }


    private void abrirVentanaCrud(ActionEvent event, AddRegistro addRegistro,String resourceCRUDView){
        try {

            FXMLLoader controladorLoader = new FXMLLoader(getClass().getResource(resourceCRUDView));
            AnchorPane contenedorServicioRegularCRUD = controladorLoader.load();
            SetAddRegistroListener servicioRegularCrudController = controladorLoader.getController();

            servicioRegularCrudController.setAddRegistroListener(addRegistro);

            Stage primaryStage = new Stage();
            // Parent root = FXMLLoader.load(getClass().getResource("/views/Cruds/taxisCRUD.fxml"));
            primaryStage.setTitle("Servicios");
            Scene scene = new Scene(contenedorServicioRegularCRUD);
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), new Runnable() {
                @Override
                public void run() {
                    FXRobot robot = FXRobotFactory.createRobot(scene);
                    robot.keyPress(KeyCode.TAB);
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.initOwner(this.textField_servicioRapido.getScene().getWindow());
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
