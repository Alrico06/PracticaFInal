package view;
import controller.Controller;
import model.Question;
import model.Option;

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
        System.out.println("\n=== CRUD MENU ===");
        System.out.println("1. Create new question");
        System.out.println("2. List questions");
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
        int correctIndex = -1;

        while (correctIndex < 1 || correctIndex > 4) {
        try {
            correctIndex = Esdia.readInt("\nWhich option is correct? (1-4): ");
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid number.");
        }
        }

        // -- ENVIAR DATOS AL CONTROLLER --
        try {
            controller.createQuestion(author, statement, topics, optionTexts, optionRationales, correctIndex);
            showMessage("Question created successfully.");
        } catch (Exception e) {
            showErrorMessage("Could not create question: " + e.getMessage());
        }



    }

    private void listQuestions() {

    // -- Preguntar el tipo de listado --
    System.out.println("\n=== LIST QUESTIONS ===");
    System.out.println("1. List all questions");
    System.out.println("2. List questions by topic");
    System.out.println("0. Back to CRUD menu");

    int choice = Esdia.readInt(    "Select an option: ",1,2);

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
            optionCRUD();
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

        showMessage("=== Exam Mode ===");

        // 1. Pedir número de preguntas
        System.out.print("Enter the number of questions for the exam: ");
        int numQuestions;

        try {
            numQuestions = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid number.");
            return;
        }

        // 2. Pedir tema
        System.out.print("Enter topic (or type ALL): ");
        String topic = scanner.nextLine().trim().toUpperCase();

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

            System.out.print("Select an option (1-4). Press ENTER to leave unanswered: ");
            String answer = scanner.nextLine();

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

        System.out.println("\n=== EXAM SUMMARY ===");

        System.out.println("Correct answers: " + result.getCorrect());
        System.out.println("Wrong answers:   " + result.getWrong());
        System.out.println("Unanswered:      " + result.getUnanswered());
        System.out.println("Grade (0-10):    " + result.getGrade());

        showMessage("Exam finished.");
    }





    // Falta terminar de implementar las 2 funciones anteriores //




    //Faltan por crear//

    private void viewQuestionDetail(Question question) {
    // Aquí mostramos los datos de esa pregunta específica
    }

}
