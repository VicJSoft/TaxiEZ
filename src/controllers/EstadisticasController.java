 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.Cliente;
import models.Empleado;
import models.Taxi;
import models.interfaces.IAccion;
import models.interfaces.Registro;
import services.sql.ClienteSQL;
import services.sql.EmpleadoSQL;
import services.sql.ServicioRegularSQL;
import services.sql.TaxisSQL;

 /**
 * FXML Controller class
 *
 * @author vicen
 */
public class EstadisticasController implements Initializable, IAccion {

    @FXML
    public JFXDatePicker dp_fechaInicio;

    @FXML
    public JFXDatePicker dp_fechaFin;

    @FXML
    public JFXRadioButton rbRegular;

    @FXML
    public ToggleGroup tipoServicio;

    @FXML
    public JFXRadioButton rbProgramado;

    @FXML
    private LineChart<String, Integer> linechart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    

    @FXML
    private JFXComboBox<String> comboBox_tipoReporte;

    @FXML
    private JFXComboBox<Registro> comboBox_multiple;

    public static ObservableList<String> reportes=
            FXCollections.observableArrayList(
                    "Unidad","Cliente",
                    "Empleado"
            );


    String columnaCondicion = "";
    String tabla ="";
    int valorCondicion ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setAnimated(false);
        yAxis.setAnimated(true);
        linechart.setAnimated(true);

        comboBox_multiple.setCellFactory(callbackComboMultiple);

        //probar con un listcell anonimo.
        comboBox_multiple.setButtonCell(callbackComboMultiple.call(null));
        //unidad cliente empleado

        comboBox_tipoReporte.setItems(reportes);
       comboBox_tipoReporte.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
              //evaluarReporte(comboBox_tipoReporte.getSelectionModel().getSelectedItem());
               comboBox_multiple.getSelectionModel().clearSelection();;
               comboBox_multiple.setItems(null);
               int selectedIndex = comboBox_tipoReporte.getSelectionModel().getSelectedIndex();
               ObservableList<Registro> lista = FXCollections.observableArrayList();
               if(selectedIndex == 0){
                   columnaCondicion = "unidad.idUnidad";
                   ObservableList<Taxi> taxis = new TaxisSQL().getTaxis();
                   lista.addAll(taxis);

               }else if(selectedIndex == 1){
                   columnaCondicion = "servicio.idCliente";
                   ObservableList<Cliente> clientes = new ClienteSQL().getClientes();
                   lista.addAll(clientes);

               }else if(selectedIndex == 2){
                   columnaCondicion = "servicio.idEmpleado";
                   ObservableList<Empleado> empleados = new EmpleadoSQL().getEmpleados();
                   lista.addAll(empleados);
               }
               comboBox_multiple.setItems( lista );

           }
       });
       comboBox_multiple.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               Registro selectedItem = comboBox_multiple.getSelectionModel().getSelectedItem();
               if(selectedItem!=null){

                    if(selectedItem instanceof Taxi){

                        valorCondicion = ((Taxi) selectedItem).getIdUnidad();

                    }else if(selectedItem instanceof  Cliente){

                        valorCondicion = ((Cliente) selectedItem).getIdCliente();
                    }
                    else if(selectedItem instanceof  Empleado){
                        valorCondicion = ((Empleado) selectedItem).getIdEmpleado();
                    }

                }
           }
       });
      
       
        
        xAxis.setLabel("Fecha");
        yAxis.setLabel("Cantidad");
       // inicializarGraficaCero();
    }


    @FXML
    public void btnGenerar_OnActiion(ActionEvent event) {


        LocalDate valueInicio = dp_fechaInicio.getValue();
        LocalDate valueFin = dp_fechaFin.getValue();
        XYChart.Series series = new XYChart.Series();

        while(valueInicio.isBefore(valueFin)||valueInicio.isEqual(valueFin)){

            try {
                int conteo = new ServicioRegularSQL().getServiciosAplicadosSegunFiltro(columnaCondicion, valorCondicion, valueInicio);
                XYChart.Data punto = new XYChart.Data(valueInicio.toString(), conteo);
                punto.setNode(new HoveredThresholdNode((Integer) punto.getYValue()));

                series.getData().add(punto);

            } catch (SQLException e) {
                e.printStackTrace();
            }


            valueInicio = valueInicio.plusDays(1);
        }

        linechart.getData().clear();
        linechart.getData().add(series);


    }


    @FXML
    public void rbProgramado_OnAction(ActionEvent event) {
        tabla = "servicio.";
    }

    @FXML
    public void rbRegular_OnAction(ActionEvent event) {
        tabla = "servicio.";
    }

    Callback<ListView<Registro>, ListCell<Registro>> callbackComboMultiple = new Callback<ListView<Registro>, ListCell<Registro>>() {
        @Override
        public ListCell<Registro> call(ListView<Registro> param) {

            return new ListCell<Registro>(){
                @Override
                protected void updateItem(Registro item, boolean empty) {
                    super.updateItem(item, empty);

                    if(item instanceof Taxi){
                        setText(((Taxi) item).getIdUnidad() + " " + ((Taxi) item).getTaxista().getNombre());
                    }
                    else if(item instanceof Cliente){
                        setText(((Cliente) item).getNumero() + " " + ((Cliente) item).getNombre());
                    }
                    else if(item instanceof Empleado){
                        setText(((Empleado) item).getNombre());
                    }


                }
            };

        }
    };

    @Override
    public void accionPrimaria() {

    }

    @Override
    public void accionSecundaria() {

    }

    @Override
    public void accionTerciaria() {

    }



  class HoveredThresholdNode extends StackPane {
      
    HoveredThresholdNode(  int value) {
      setPrefSize(15, 15);

      final Label label = createDataThresholdLabel(  value);

      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().setAll(label);
          setCursor(Cursor.NONE);
          toFront();
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getChildren().clear();
          setCursor(Cursor.CROSSHAIR);
        }
      });
    }

    private Label createDataThresholdLabel( int value) {
      final Label label = new Label(value + "");
      label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
      label.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-background-radius:50; -fx-border-radius:50;");

      label.setTextFill(Color.DARKGRAY);
     
      label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
      return label;
    }
  }
 
}
