package view;
import controller.Controller;

import java.util.HashSet;

import com.coti.tools.Esdia;

public class InteractiveView extends BaseView {
    
    /* CONSTRUCTOR */

    public InteractiveView(Controller controller) {
        super(controller);
    }

    /* METODOS OBLIGATORIOS */

    // METODO INICIAR//
    @Override
    public void init() {

        boolean exit = false;

        showMessage("Welcome to the question management system.");

        while (!exit) {
            showMainMenu();

            int option = Esdia.readInt("Select an option: ", 0, 4);

            switch (option) {
                case 1 -> optionCRUD();
                case 2 -> optionImportExport();
                case 3 -> optionAutomaticQuestion();
                case 4 -> optionExamMode();
                case 0 -> exit = true;
                default -> showErrorMessage("Invalid option. Try again.");
            }
        }

        end();
    }

    // METODO FINALIZAR //
    @Override
    public void end() {

        
    }

    // METODO MOSTRAR MENSAJE //
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    // METODO MOSTRAR MENSAJE DE ERROR //
    @Override
    public void showErrorMessage(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }


    /* METODOS AUXILIARES */

    // Mostrar el menu principal //
    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. CRUD (Questions)");
        System.out.println("2. Import / Export");
        System.out.println("3. Automatic Question Creation");
        System.out.println("4. Exam Mode");
        System.out.println("0. Exit");
    }

    // Gestionar la opción CRUD //
    private void optionCRUD() {

        boolean back = false;

        while (!back) {
            showCRUDMenu();

            int option = Esdia.readInt("Select an option: ", 0, 3);

            switch (option) {
                case 1 -> createQuestion();
                case 2 -> listQuestions();
                case 3 -> viewQuestionDetail();
                case 0 -> back = true;
                default -> showErrorMessage("Invalid option.");
            }
        }
    }

    // Mostrar el menu CRUD //
    private void showCRUDMenu() {
        System.out.println("\n=== CRUD MENU ===");
        System.out.println("1. Create new question");
        System.out.println("2. List questions");
        System.out.println("3. View question detail");
        System.out.println("0. Back to main menu");
    }

    
    // Pide datos para crear una nueva pregunta //
    private void createQuestion() {

        showMessage("=== Create New Question ===");

        String author = Esdia.readString("Enter author: ");

        String statement = Esdia.readString("Enter question statement: ");

        //Los temas se introducirán separados por comas (ej: "TEMA 1, TEMA 2, TEMA 3")
        String topicsInput = Esdia.readString("Enter topics (comma separated): ");

        // normalizo y guardo en un HashSet para evitar duplicados
        HashSet<String> topics = new HashSet<>();
        for (String t : topicsInput.split(",")) {
            topics.add(t.trim().toUpperCase()); 
        }

        /* ----- CONTINUAR POR AQUI IMPLEMENTANDO ----- */
    }





    //Faltan por crear//

    private void optionImportExport() {
        showMessage("Import/Export (pending implementation).");
    }

    private void optionAutomaticQuestion() {
        showMessage("Automatic question creation (pending implementation).");
    }

    private void optionExamMode() {
        showMessage("Exam mode (pending implementation).");
    }


    private void listQuestions() {
        showMessage("Has seleccionado Importar/Exportar (todavía sin implementar).");

    }

    private void viewQuestionDetail() {
        showMessage("Has seleccionado crear pregunta automática (todavía sin implementar).");

    }

}
