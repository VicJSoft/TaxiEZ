package resources;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import controllers.Confirmacion;
import controllers.ErrorController;
import controllers.crudsControllers.EmpleadosCrudController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import models.Empleado;
import models.interfaces.ResultDialog;
import services.sql.ConexionSQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Statics {

    //Singleton.
    private static Connection connection;
    public static Empleado empleadoSesionActual;


    public static void createConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        connection = new ConexionSQL().getConexion();
    }

    public static void setConnections(Connection connection)
    {
        Statics.connection=connection;
    }
    public static Connection getConnections()
    {
        return Statics.connection;
    }

    /**
     * Crea una ventana de error, de estilo modal. Necesita indicar quien es la ventana padre,
     * para hacerla modal.
     * @param ownerWindowError
     * Ventana padre de la ventana error a crear.
     * @param tittleBar
     * Titulo que llevara´ la ventana de error.
     * @param tittleContent
     * Titulo interno de la ventana.
     * @param contentMessage
     * Texto que informara´ sobre el error especificado.
     */
    public static void crearVentanaError(Window ownerWindowError, String tittleBar, String tittleContent, String contentMessage){
        //TODO Crear XML de la ventana error.
        System.out.println("Error presente.");

        ErrorController ventanaError = new ErrorController( tittleBar, tittleContent , contentMessage);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage;
                stage = new Stage();
                Scene scene = new Scene(ventanaError);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.setTitle(tittleBar);
                stage.getIcons().add(new Image(getClass().getResource("/resources/imagenes/error.png").toString() ));
                if(ownerWindowError!=null)
                    stage.initOwner(ownerWindowError);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.show();
            }
        });

    }

    public static void crearVentanaConfinrmación(Window ownerWindowConfirmacion, String textConfirmacion, ResultDialog resultDialog){

        FXMLLoader controladorLoader = new FXMLLoader(Statics.class.getResource("/views/Confirmacion.fxml"));
        try {
            AnchorPane contenedor = controladorLoader.load();
            Confirmacion confirmacion = controladorLoader.getController();
            confirmacion.setListenerResultDialog(resultDialog);
            Stage stage = new Stage();
            Scene scene = new Scene(contenedor);
            stage.setScene(scene);
            stage.setTitle("¿Desea continuar?");
            stage.getIcons().add(new Image(Statics.class.getResource("/resources/imagenes/info.png").toString() ));
            if(ownerWindowConfirmacion!=null)
                stage.initOwner(ownerWindowConfirmacion);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static Optional<Boolean> crearConfirmacion(Stage stage,String tittle,String body){

        JFXAlert<Boolean> alert = new JFXAlert<>(stage);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.initModality(Modality.WINDOW_MODAL);

        JFXDialogLayout content = new JFXDialogLayout();
        content.getStylesheets().add("/resources/css/normalizacion.css");

        content.getStyleClass().add("label");

        content.setHeading(new Text(tittle));
        content.setBody(new Text(body));

        Button btnContinuar = new Button("Continuar");
        btnContinuar.getStyleClass().add("boton");

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.getStyleClass().add("boton-cancelar");


        content.setActions(btnContinuar,btnCancelar);

        btnCancelar.setOnAction((action)->{
            alert.setResult(false);
            alert.hideWithAnimation();
        });

        btnContinuar.setOnAction(action->{

            alert.setResult(true);
            alert.hideWithAnimation();

        });

        alert.setContent(content);
        return alert.showAndWait();

        //     JFXDialog jfxDialog = new JFXDialog((StackPane) ((Node) event.getSource()).getScene().getRoot(),content, JFXDialog.DialogTransition.CENTER);
        //    jfxDialog.show();

           /* if(event!=null){
                Statics.crearVentanaConfinrmación(
                        ((Node) event.getSource()).getScene().getWindow(),
                        "El número de telefono introducido ya existe. Los nuevos datos reemplazarán al anterior. \n ¿Desea continuar?",
                        new ResultDialog() {
                            @Override
                            public void giveResult(boolean estado) {
                                if(borrar&&existe){
                                    eliminar(cliente);
                                    //ventana de advertencia de que ya existe
                                }
                            }
                        }
                );
            }*/
    }

    /**
     * Busca el ultimo ID primario IA insertado o updateado.
     * Si no existe Ultimo ID se retorna 1.
     * Solo Aplica para campos IA.
     * @return
     * Devuelve ese ID.
     * @throws SQLException
     */
    public static int getLastId() throws SQLException {

        int lastId = -1;
        String query=" SELECT LAST_INSERT_ID()";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            lastId = resultSet.getInt(1) ;
            if( lastId == 0){
                lastId = 1;
            }
        }

        return lastId;
    }



}
