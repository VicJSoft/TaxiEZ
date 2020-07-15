/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author VicEspino
 */
public class ErrorController extends AnchorPane{

    @FXML
    private ImageView iv_icon;
    @FXML
    private Button btn_cerrar;
    @FXML
    private Label lbl_tittleContent;
    @FXML
    private JFXTextArea txt_contentError;
    @FXML
    private Button btn_aceptar;

    public ErrorController(String tittleBarMessage, String tittleContentMessage, String contentMessage){
       
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/VentanaError.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(ErrorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.lbl_tittleContent.setText(tittleContentMessage);
        this.txt_contentError.setText(contentMessage);
        
    }



   
    
    
}
