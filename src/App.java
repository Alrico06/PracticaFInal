import controller.Controller;
import view.InteractiveView;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Test");
        Controller controller = new Controller();
        InteractiveView vista = new InteractiveView(controller);

        vista.init();
    }
}
