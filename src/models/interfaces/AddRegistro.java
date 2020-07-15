package models.interfaces;

import controllers.crudsControllers.ClientesCrudController;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Callback;


/**
 * Clase que ayuda a la transferencia de un Registro(cualquier model que implemente @Registro)
 * La transferencia se realiza en el metodo abstracto para su propia implementación.
 */
public abstract class AddRegistro {

    protected Registro registro = null;

    public AddRegistro(Registro registro) {
        this.registro = registro;
    }

    /**
     * Invoca la transferencia del registro.
     * @param registro
     * @param event
     * A partir de él se obtiene la ventana al que pertenece.
     * La ventana es necesaria para colocar alguna confirmación o mensaje dentro de la ventana secundaria o crud.
     * @return
     * True si se añadió correctamente(criterio de cada ventana.
     * False si no se añadió correctamente(criterio de cada ventana.
     */
    public abstract boolean addRegistro(Registro registro, Stage stage);

    public AddRegistro() {
        super();
    }


    public Registro getRegistro() {
        return registro;
    }
}
