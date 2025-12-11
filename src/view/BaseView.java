package view;
import controller.Controller;

public abstract class BaseView {

    protected Controller controller;

    protected BaseView(Controller controller){
        this.controller = controller;
    }

    protected void setController(Controller controller){
        this.controller = controller;
    }

    protected Controller getController(){
        return this.controller;
    }

    public abstract void init();

    public abstract void end();

    public abstract void showMessage(String message);

    public abstract void showErrorMessage(String errorMessage);
}
