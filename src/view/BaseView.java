package view;
import controller.Controller;

public abstract class BaseView {


    //--ATRIBUTOS--//
    protected Controller controller;


    //--CONSTRUCTOR--//
    protected BaseView(Controller controller){
        this.controller = controller;
    }


    //--METODOS--//

    //METODOS GETTERS Y SETTERS//
    protected void setController(Controller controller){
        this.controller = controller;
    }

    protected Controller getController(){
        return this.controller;
    }


    //METODO INICIAR//
    public abstract void init();


    //METODO FINALIZAR//
    public abstract void end();

    //METODO MOSTRAR MENSAJE//

    /**
     * @param message Mensaje a mostrar
     */

    public abstract void showMessage(String message);

    //METODO MOSTRAR MENSAJE DE ERROR//

    /**
     * @param errorMessage Mensaje de error a mostrar
     */

    public abstract void showErrorMessage(String errorMessage);

    
}
