package view;
import controller.Controller;
import model.Question;
import model.Option;
import model.ExamResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    public void end(){

    showMessage("Closing application...");
    showMessage("Goodbye!");
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


    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Mostrar el menu principal //
        private void showMainMenu() {
        
        clearScreen();
        
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

            int option = Esdia.readInt("Select an option: ", 0, 2);

            switch (option) {
                case 1 -> createQuestion();
                case 2 -> listQuestions();
                case 0 -> back = true;
                default -> showErrorMessage("Invalid option.");
            }
        }
    }

    // Mostrar el menu CRUD //
    private void showCRUDMenu() {

        clearScreen();
        System.out.println("\n=== CRUD MENU ===");
        System.out.println("1. Create new question");
        System.out.println("2. List questions");
        System.out.println("0. Back to main menu");
    }

    
    // Pide datos para crear una nueva pregunta //
    private void createQuestion() {

        clearScreen();
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

        // -- PEDIR LAS 4 OPCIONES --
        // Comentario: Creamos una lista para guardar las opciones
        List<String> optionTexts = new ArrayList<>();
        List<String> optionRationales = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            System.out.println("\n--- Option " + i + " ---");

            // Texto de la opción
            String optText = Esdia.readString("Enter option text: ");
            optionTexts.add(optText);

            // Rationale
            String optRat = Esdia.readString("Enter option rationale: ");
            optionRationales.add(optRat);
        }

        // -- ELEGIR OPCIÓN CORRECTA --
        // Comentario: Se obliga al usuario a elegir un número entre 1 y 4
        int correctIndex = Esdia.readInt("\nWhich option is correct? (1-4): ", 1, 4);

        // -- ENVIAR DATOS AL CONTROLLER --
        try {
            controller.createQuestion(author, statement, topics, optionTexts, optionRationales, correctIndex);
            showMessage("Question created successfully.");
        } catch (Exception e) {
            showErrorMessage("Could not create question: " + e.getMessage());
        }



    }

    private void listQuestions() {

    clearScreen();
    // -- Preguntar el tipo de listado --
    System.out.println("\n=== LIST QUESTIONS ===");
    System.out.println("1. List all questions");
    System.out.println("2. List questions by topic");
    System.out.println("0. Back to CRUD menu");

    int choice = Esdia.readInt(    "Select an option: ",0,2);

    List<Question> questions = new ArrayList<>();

    try {
        if (choice == 1) {
           
            // -- Obtener todas las preguntas --
            questions = controller.getAllQuestions();

        } else if (choice == 2) {

            // -- Preguntar tema --
            String topic = Esdia.readString("Enter topic: ").trim().toUpperCase();

            questions = controller.getQuestionsByTopic(topic);

        } else if (choice == 0) {
            return;
        }else{
            
            showErrorMessage("Invalid option.");
            return;
        }

        // -- Si no hay preguntas --
        if (questions.isEmpty()) {
            showMessage("No questions found.");
            return;
        }

        // -- Mostrar preguntas (solo índice + statement) --
        System.out.println("\n--- Questions ---");
        int index = 1;
        for (Question q : questions) {
            System.out.println(index + ". " + q.getStatement());
            index++;
        }

        // -- Elegir una pregunta para ver detalle --
        int selected = Esdia.readInt("\nSelect a question to view details (0 to cancel): ");

        if (selected == 0) return;

        if (selected < 1 || selected > questions.size()) {
            showErrorMessage("Invalid selection.");
            return;
        }

        // Pasar a ver detalle
        viewQuestionDetail(questions.get(selected - 1));

    } catch (Exception e) {
        showErrorMessage("Could not list questions: " + e.getMessage());
    }
    }



    private void optionImportExport() {

        boolean back = false;

        while (!back) {
            clearScreen();
            // -- Menú Import/Export --
            System.out.println("\n=== IMPORT / EXPORT MENU ===");
            System.out.println("1. Export questions to JSON");
            System.out.println("2. Import questions from JSON");
            System.out.println("0. Back to main menu");

            int option = Esdia.readInt("Select an option: ", 0, 2);

            switch (option) {

                case 1 -> exportQuestions();

                case 2 -> importQuestions();

                case 0 -> back = true;

                default -> showErrorMessage("Invalid option.");
            }
        }
    }

    private void exportQuestions() {
        // Comentario: La vista solo pide al controller realizar la exportación.
        try {
            controller.exportQuestions();
            showMessage("Questions exported successfully.");
        } catch (Exception e) {
            showErrorMessage("Export failed: " + e.getMessage());
        }
    }


    private void importQuestions() {
        // Comentario: La vista pide al controller que importe desde JSON.
        try {
            controller.importQuestions();
            showMessage("Questions imported successfully.");
        } catch (Exception e) {
            showErrorMessage("Import failed: " + e.getMessage());
        }
    }


    private void optionAutomaticQuestion() {

        // Comentario: primero verificamos si existen QuestionCreators cargados.
        if (!controller.hasQuestionCreators()) {
            showErrorMessage("There are no automatic question generators available.");
            return;
        }

        // Pedimos el tema
        String topic = Esdia.readString("Enter topic for the automatic question: ").trim().toUpperCase();

        try {
            // Pedimos al controller que genere una pregunta
            Question generated = controller.generateAutomaticQuestion(topic);

            if (generated == null) {
                showErrorMessage("No question could be generated for this topic.");
                return;
            }

            // Mostramos la pregunta
            showGeneratedQuestionPreview(generated);

            // Confirmación
            String confirm = Esdia.readString("Do you want to add this question to the database? (Y/N): ").trim().toUpperCase();

            if (confirm.equals("Y")) {
                controller.addGeneratedQuestion(generated);
                showMessage("Question added successfully.");
            } else {
               showMessage("Operation cancelled.");
            }

        } catch (Exception e) {
         showErrorMessage("Could not generate automatic question: " + e.getMessage());
        }
    }

    // Comentario: muestra la pregunta generada automáticamente.
    private void showGeneratedQuestionPreview(Question q) {

        clearScreen();

        System.out.println("\n=== AUTOMATIC QUESTION PREVIEW ===");

        System.out.println("Statement: " + q.getStatement());
        System.out.println("Topics: " + q.getTopics());
        System.out.println("Author: " + q.getAuthor());

        System.out.println("Options:");

        int i = 1;
        for (Option op : q.getOptions()) {
            System.out.println(i + ". " + op.getText() +" (correct: " + op.isCorrect() + ")");
            System.out.println("   Rationale: " + op.getRationale());
            i++;
        }
    }

    private void optionExamMode() {
        clearScreen();
        showMessage("=== Exam Mode ===");

        // 1. Pedir número de preguntas

           int numQuestions = Esdia.readInt("Enter the number of questions for the exam: ");

        // 2. Pedir tema
        String topic = Esdia.readString("Enter topic (or type ALL): ").trim().toUpperCase();

        // 3. Pedir al controller que prepare el examen
        List<Question> examQuestions;

        try {
            examQuestions = controller.getExamQuestions(numQuestions, topic);

            if (examQuestions.isEmpty()) {
                showErrorMessage("Not enough questions available.");
                return;
           }

        } catch (Exception e) {
            showErrorMessage("Could not prepare exam: " + e.getMessage());
            return;
        }

        // 4. Registrar respuestas del usuario
        List<Integer> userAnswers = new ArrayList<>();

        for (int i = 0; i < examQuestions.size(); i++) {
            Question q = examQuestions.get(i);

            System.out.println("\nQuestion " + (i + 1) + ":");
            System.out.println(q.getStatement());

            List<Option> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j).getText());
            }

            String answer = Esdia.readString_ne("Select an option (1-4). Press ENTER to leave unanswered: ");

            if (answer.isBlank()) {
                userAnswers.add(0); // 0 = no respondida
            } else {
                try {
                    int selected = Integer.parseInt(answer);
                    if (selected < 1 || selected > 4) {
                        userAnswers.add(0);
                    } else {
                        userAnswers.add(selected);
                    }
                } catch (NumberFormatException e) {
                    userAnswers.add(0);
                }
            }
        }

        // 5. Calcular resultados mediante el controller
        try {
            ExamResult result = controller.evaluateExam(examQuestions, userAnswers);
            showExamSummary(result);
        } catch (Exception e) {
            showErrorMessage("Error evaluating exam: " + e.getMessage());
        }
    }


    // Comentario: muestra un resumen con aciertos, fallos, no respondidas y nota.
    private void showExamSummary(ExamResult result) {

        clearScreen();

        System.out.println("\n=== EXAM SUMMARY ===");

        System.out.println("Correct answers: " + result.getCorrect());
        System.out.println("Wrong answers:   " + result.getWrong());
        System.out.println("Unanswered:      " + result.getUnanswered());
        System.out.println("Grade (0-10):    " + result.getGrade());

        showMessage("Exam finished.");
    }

    private void viewQuestionDetail(Question question) {

        clearScreen();
        // Comentario: Mostrar todos los detalles de la pregunta que el usuario seleccionó.
        System.out.println("\n=== QUESTION DETAIL ===");

        System.out.println("ID: " + question.getId());
        System.out.println("Author: " + question.getAuthor());
        System.out.println("Topics: " + question.getTopics());
        System.out.println("Statement: " + question.getStatement());

        System.out.println("\nOptions:");
        int i = 1;
        for (Option op : question.getOptions()) {
            System.out.println(i + ". " + op.getText());
            System.out.println("   Rationale: " + op.getRationale());
            System.out.println("   Correct: " + op.isCorrect());
            i++;
        }   

        clearScreen();

        // Menú de acciones específicas sobre esta pregunta
        System.out.println("\n=== AVAILABLE ACTIONS ===");
        System.out.println("1. Modify this question");
        System.out.println("2. Delete this question");
        System.out.println("0. Back");

        int choice = Esdia.readInt("Select an option: ",0,2);

        switch (choice) {

            case 1 -> {
                // Comentario: modifyQuestion() será implementado después.
                modifyQuestion(question);
            }

            case 2 -> {
                // Comentario: La eliminación siempre la gestiona el controller.
                try {
                    controller.deleteQuestion(question);
                    showMessage("Question deleted successfully.");
                } catch (Exception e) {
                    showErrorMessage("Error deleting question: " + e.getMessage());
                }
            }

            case 0 -> {
                // Simplemente volver
                return;
            }

            default -> showErrorMessage("Invalid option.");
        }
    }

    // Comentario: permite modificar cualquier atributo de una pregunta salvo el ID.
    private void modifyQuestion(Question question) {

        boolean back = false;

        while (!back) {

            clearScreen();
            System.out.println("\n=== MODIFY QUESTION ===");

            System.out.println("1. Modify author");
            System.out.println("2. Modify topics");
            System.out.println("3. Modify statement");
            System.out.println("4. Modify options");
            System.out.println("0. Back");

            int option = Esdia.readInt("Select an option: ", 0, 4);

            switch (option) {

                case 1 -> modifyAuthor(question);

                case 2 -> modifyTopics(question);

                case 3 -> modifyStatement(question);

                case 4 -> modifyOptions(question);

                case 0 -> back = true;

                default -> showErrorMessage("Invalid option.");
            }
        }
    }


    // Comentario: modificar el autor de la pregunta
    private void modifyAuthor(Question question) {

        String newAuthor = Esdia.readString("Enter new author: ");

        try {
            controller.modifyAuthor(question, newAuthor);
            showMessage("Author updated successfully.");
        } catch (Exception e) {
            showErrorMessage("Error updating author: " + e.getMessage());
        }
    }

    // Comentario: modificar los temas, siempre en mayúsculas
    private void modifyTopics(Question question) {

        String input = Esdia.readString("Enter new topics (comma separated): ");

        HashSet<String> newTopics = new HashSet<>();

        for (String t : input.split(",")) {
            newTopics.add(t.trim().toUpperCase());
        }

        try {
            controller.modifyTopics(question, newTopics);
            showMessage("Topics updated successfully.");
        } catch (Exception e) {
            showErrorMessage("Error updating topics: " + e.getMessage());
        }
    }

    // Comentario: modificar el enunciado de la pregunta
    private void modifyStatement(Question question) {

        String newStatement = Esdia.readString("Enter new statement: ");

        try {
            controller.modifyStatement(question, newStatement);
            showMessage("Statement updated successfully.");
        } catch (Exception e) {
            showErrorMessage("Error updating statement: " + e.getMessage());
        }
    }


    // Comentario: modificar las 4 opciones y su opción correcta
    private void modifyOptions(Question question) {

        List<String> newTexts = new ArrayList<>();
        List<String> newRationales = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            System.out.println("\n--- Option " + i + " ---");

            newTexts.add(Esdia.readString("Enter option text: "));

            newRationales.add(Esdia.readString("Enter option rationale: "));
        }

        // Elegir la correcta
        int correctIndex = -1;

        while (correctIndex < 1 || correctIndex > 4) {
                correctIndex = Esdia.readInt("Which option is correct? (1-4): ");
        }

        try {
            controller.modifyOptions(question, newTexts, newRationales, correctIndex);
            showMessage("Options updated successfully.");
        } catch (Exception e) {
            showErrorMessage("Error updating options: " + e.getMessage());
        }
    }


}
