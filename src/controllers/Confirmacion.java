package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.interfaces.ResultDialog;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Deprecated
 * Se reemplazó por un alert.
 */
public class Confirmacion implements Initializable {

    @FXML
    private Button btnContinuar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Label lbl_tittleContent;
    private ResultDialog resultDialog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    void btnCancelar_OnAction(ActionEvent event) {
        if(resultDialog!=null)
            this.resultDialog.giveResult(false);
    }

    @FXML
    void btnContinuar_OnAction(ActionEvent event) {
        if(resultDialog!=null)
            this.resultDialog.giveResult(true);
    }

    public void setListenerResultDialog(ResultDialog resultDialog){
        this.resultDialog = resultDialog;
    }


}
