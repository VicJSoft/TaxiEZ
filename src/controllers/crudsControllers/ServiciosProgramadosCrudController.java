package controllers.crudsControllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.*;
import models.interfaces.IValidateCRUD;
import models.interfaces.Registro;
import models.interfaces.SetAddRegistroListener;
import resources.Statics;
import services.StringLengthValidator;
import services.sql.ClienteSQL;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServiciosProgramadosCrudController extends SetAddRegistroListener implements Initializable, IValidateCRUD {

    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField textField_telefono_buscar;

    @FXML
    private Label lbl_tittle;

    @FXML
    private JFXTextField textField_telefono;

    @FXML
    private JFXTextField textField_nombre;

    @FXML
    private JFXTextField textField_calle;

    @FXML
    private JFXTextField textField_colonia;

    @FXML
    private JFXTextField textField_num_ext;

    @FXML
    private JFXTextField textField_numInt;

    @FXML
    private JFXTextField textField_notas;

    @FXML
    private JFXRadioButton rb_personalizado;

    @FXML
    private ToggleGroup toggletipoProgramado;

    @FXML
    private JFXRadioButton rb_diario;

    @FXML
    private JFXCheckBox cb_lunes;

    @FXML
    private JFXCheckBox cb_martes;

    @FXML
    private JFXCheckBox cb_miercoles;

    @FXML
    private JFXCheckBox cb_jueves;

    @FXML
    private JFXCheckBox cb_viernes;

    @FXML
    private JFXCheckBox cb_sabado;

    @FXML
    private JFXCheckBox cb_domingo;

    @FXML
    private JFXTimePicker timePicker_horaServicio;

    @FXML
    private JFXDatePicker datePicker_dia;

    @FXML
    private JFXTextField txt_destino;

    @FXML
    private Button btn_aceptar;

    @FXML
    private Button btn_cancelar;

    @FXML
    private Label lbl_errorConfigTipoServicio;

    LocalDate ldLunesProximo;
    LocalDate ldMartesProximo;
    LocalDate ldMiercolesProximo;
    LocalDate ldJuevesProximo;
    LocalDate ldViernesProximo;
    LocalDate ldSabadoProximo;
    LocalDate ldDomingoProximo;
    LocalDate ldHoy;

    private ArrayList<JFXCheckBox> listaCheckBox = new ArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setFieldValidations();
        setClickedEventCheckBox();


        this.rb_diario.setOnAction((ActionEvent event) -> {
            //toogleCheckBoxDias();
            setChecksDays(true);

            datePicker_dia.setValue(LocalDate.now());
            timePicker_horaServicio.setValue(LocalTime.now());
        });

        this.rb_personalizado.setOnAction(event -> {

            datePicker_dia.setValue(getDiaMasCercanoInicio());

        });

    }

    private LocalDate getDiaMasCercanoInicio() {

        ldHoy = LocalDate.now();

        //alguno de estos tiene el mismo dia que hoy.
        ldLunesProximo = cb_lunes.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)):null;
        ldMartesProximo = cb_martes.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)):null;
        ldMiercolesProximo = cb_miercoles.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY)):null;
        ldJuevesProximo = cb_jueves.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY)):null;
        ldViernesProximo = cb_viernes.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)):null;
        ldSabadoProximo = cb_sabado.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)):null;
        ldDomingoProximo = cb_domingo.isSelected() ?ldHoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)):null;

        LocalDate diaMasCercano = ldLunesProximo;
        //ALgunos pueden ser nulos, entonces:
        if(ldMartesProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldMartesProximo)||diaMasCercano.isEqual(ldMartesProximo)?ldMartesProximo:diaMasCercano;
        if(ldMiercolesProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldMiercolesProximo)||diaMasCercano.isEqual(ldMiercolesProximo)?ldMiercolesProximo:diaMasCercano;
        if(ldJuevesProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldJuevesProximo)||diaMasCercano.isEqual(ldJuevesProximo)?ldJuevesProximo:diaMasCercano;
        if(ldViernesProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldViernesProximo)||diaMasCercano.isEqual(ldViernesProximo)?ldViernesProximo:diaMasCercano;
        if(ldSabadoProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldSabadoProximo)||diaMasCercano.isEqual(ldSabadoProximo)?ldSabadoProximo:diaMasCercano;
        if(ldDomingoProximo!=null)
            diaMasCercano = diaMasCercano.isBefore(ldDomingoProximo)||diaMasCercano.isEqual(ldDomingoProximo)?ldDomingoProximo:diaMasCercano;


        return diaMasCercano;
    }


    @FXML
    void DetectFocusable_OnMouse(MouseEvent event) {

    }

    @FXML
    void btnAceptar_OnAction(ActionEvent event) {
        if(validarCampos()){
            if(enviarRegistro(((Stage)btn_aceptar.getScene().getWindow())))
                    ((Stage)btn_aceptar.getScene().getWindow()).close();
        }
    }

    @FXML
    void btnCancelar_OnAction(ActionEvent event) {
        ((Stage)btn_aceptar.getScene().getWindow()).close();
    }

    @FXML
    void txtBuscarTelefono_OnKeyRealased(KeyEvent event) {

    }

    @Override
    public void extraerRegistro(Registro registro) {

        //no sé usará para "edidión" no existe, solo para busqueda automatica

    }

    private Cliente cliente;
    @Override
    public Registro guardarCambiosRegistros() {
        Direccion direccion = null;

        if(cliente!=null){
            //cuando se buscó y encontró el numero de telefono automaticamente.
            try {
                direccion = cliente.getDireccion().clone();//una copia para asignarsela a la instancia de servicioRegular.
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }else{//cliente == null

            //si la dirección es id=0, se deberá insertar en la DB.
            direccion =
                    new Direccion(0, textField_calle.getText(), textField_colonia.getText(), textField_numInt.getText(), textField_num_ext.getText());
        }

        Persona datos = new Persona(textField_nombre.getText(),textField_notas.getText(),direccion);
        //si cliente == null, esa propiedad debe ser creada
     /*   ServicioRegular servicioRegular =
                new ServicioRegular(datos,0,LocalDateTime.now(),LocalDateTime.of(datePicker_dia.getValue(),timePicker_horaServicio.getValue()),null,false,cliente, Statics.empleadoSesionActual);
        servicioRegular.setTelefonoAux(textField_telefono.getText());
        */
        ServiciosProgramado serviciosProgramado =
                new ServiciosProgramado(datos, 0, LocalDateTime.now(),
                        LocalDateTime.of(datePicker_dia.getValue(),timePicker_horaServicio.getValue()),null,
                        false,cliente, Statics.empleadoSesionActual,null,
                        cb_lunes.isSelected(), cb_martes.isSelected(),cb_miercoles.isSelected() ,
                        cb_jueves.isSelected(),cb_viernes.isSelected() ,cb_sabado.isSelected() ,cb_domingo.isSelected() );

        serviciosProgramado.setTelefonoAux(textField_telefono.getText());



        return serviciosProgramado;
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

        textField_telefono_buscar.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                extraerRegistro(new ClienteSQL().existe(newValue));
            } catch (SQLException e) {
                e.printStackTrace();
                //ventana error.
            }
        });
    }

    @Override
    public void setLengthValidation() {

        textField_telefono.getValidators().add(new StringLengthValidator("Máximo 11 carácteres.",11));
        textField_colonia.getValidators().add(new StringLengthValidator("Máximo 45 carácteres.",45));
        textField_calle.getValidators().add(new StringLengthValidator("Máximo 45 carácteres.",45));
        textField_num_ext.getValidators().add(new StringLengthValidator("Máximo 10 carácteres.",10));
        textField_numInt.getValidators().add(new StringLengthValidator("Máximo 10 carácteres.",10));


    }

    @Override
    public void setRequiredValidation() {
        textField_telefono.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        textField_colonia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        textField_calle.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        datePicker_dia.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
        timePicker_horaServicio.getValidators().add(new RequiredFieldValidator("Este campo es requerido."));
    }

    private void setFocusedProperty() {

        textField_nombre.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_telefono.validate();
        });

        textField_colonia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_colonia.validate();
        });
        textField_calle.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_calle.validate();
        });
        datePicker_dia.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                datePicker_dia.validate();
        });
        timePicker_horaServicio.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                timePicker_horaServicio.validate();
        });
        textField_numInt.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_numInt.validate();
        });
        textField_num_ext.focusedProperty().addListener((observable,  oldValue,  newValue)-> {
            if(!newValue)
                textField_num_ext.validate();
        });



    }

    @Override
    public boolean validarCampos() {
        ObservableList<Node> listaHijos = root.getChildren();
        boolean validacioExitosa = true;

        //estos 2 no son hijos directos de root, entonces los valido individualmente.
        validacioExitosa = datePicker_dia.validate();
        validacioExitosa &= timePicker_horaServicio.validate();

        for(Node node : listaHijos){
            if(node instanceof IFXValidatableControl){
                boolean validate = ((IFXValidatableControl) node).validate();
                validacioExitosa = validacioExitosa&&validate;
            }
        }

        if(!isAnyDayChek()) {
            validacioExitosa = false;
            //ventana o mensaje error diciendo que necesita seleccionar al menos un dia
        }
        //validacioExitosa &= isAnyDayChek();

        return validacioExitosa;
    }


    private void setChecksDays(boolean state){
        this.cb_lunes.selectedProperty().set(state);
        this.cb_martes.selectedProperty().set(state);
        this.cb_miercoles.selectedProperty().set(state);
        this.cb_jueves.selectedProperty().set(state);
        this.cb_viernes.selectedProperty().set(state);
        this.cb_sabado.selectedProperty().set(state);
        this.cb_domingo.selectedProperty().set(state);
    }

    /**
     * Cuando se manipula un checkbox de los dias entonces significa que el servicio es
     * programado personalizado, al menos que los 7 checks estén activos ahí se queda como servicio
     * programado diario.
     */
    private void setClickedEventCheckBox(){


        listaCheckBox.add(this.cb_lunes);
        listaCheckBox.add(this.cb_martes);
        listaCheckBox.add(this.cb_miercoles);
        listaCheckBox.add(this.cb_jueves);
        listaCheckBox.add(this.cb_viernes);
        listaCheckBox.add(this.cb_sabado);
        listaCheckBox.add(this.cb_domingo);

        for(JFXCheckBox actualCB : listaCheckBox){
            actualCB.setOnAction(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {

                     rb_personalizado.selectedProperty().set(true);

                     if(isAllDaysChecked()){
                         rb_diario.fire();
                     }
                 }
             });
        }
    }

    private boolean isAllDaysChecked(){
        boolean check = true;

        for(JFXCheckBox cb_actual : listaCheckBox){
            check &= cb_actual.isSelected();
        }
        return check;
    }

    private boolean isAnyDayChek(){
        boolean check = false;
        for(JFXCheckBox cb_actual : listaCheckBox){
            check |= cb_actual.isSelected();
        }
        return check;
    }


}

