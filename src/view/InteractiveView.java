package view;
import controller.Controller;
import model.ExamResult;
import model.ExamSession;
import model.Option;
import model.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.coti.tools.Esdia;

public class InteractiveView extends BaseView {

    /* ---------------- UI ENHANCEMENTS START ---------------- */
    /* ---- BRANDING START ---- */
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String BRAND = BOLD + MAGENTA;

    private String colorize(String text, String color) {
        return color + text + RESET;
    }

    private void printHeader(String title) {
        String bar = "============================================================";
        System.out.println(colorize(bar, BLUE));
        System.out.println(colorize("★  " + title, BRAND));
        System.out.println(colorize(bar, BLUE));
    }

    private void printDivider() {
        System.out.println(colorize("------------------------------------------------------------", MAGENTA));
    }

    private void renderStatusBar(String left, String right) {
        String spacing = " ".repeat(Math.max(1, 60 - left.length() - right.length()));
        System.out.println(colorize("⏺ " + left + spacing + right + " ⏺", DIM + CYAN));
    }

    private void animateProgress(String label) {
        String base = colorize(label + " [", YELLOW);
        String end = colorize("]", YELLOW);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            bar.append("#");
            System.out.print("\r" + base + colorize(bar.toString(), GREEN) + " ".repeat(12 - i) + end);
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {
            }
        }
        System.out.println();
    }

    private void printMenuItem(int number, String text, String emoji, String color) {
        System.out.println(colorize(emoji + "  " + number + ". " + text, color));
    }

    private void renderScoreBar(double grade) {
        int segments = 20;
        int filled = (int) Math.round((grade / 10.0) * segments);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments; i++) {
            sb.append(i < filled ? colorize("█", GREEN) : colorize("░", YELLOW));
        }
        System.out.println("Score: " + sb + colorize(String.format("  %.2f/10", grade), BOLD + CYAN));
    }

    private void animateBanner(String text) {
        String banner = "✦ " + text + " ✦";
        for (int i = 0; i < banner.length(); i++) {
            System.out.print(colorize(String.valueOf(banner.charAt(i)), MAGENTA));
            try {
                Thread.sleep(6);
            } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }

    private void animateSectionTransition(String title) {
        printDivider();
        animateBanner(title);
        printDivider();
    }

    private void pulseMessage(String message, String color, int pulses) {
        for (int i = 0; i < pulses; i++) {
            System.out.print("\r" + colorize(message, i % 2 == 0 ? color : BOLD + color));
            try {
                Thread.sleep(120);
            } catch (InterruptedException ignored) {}
        }
        System.out.print("\r" + colorize(message, color) + "\n");
    }

    private Thread startSpinner(String label, AtomicBoolean running) {
        Thread spinner = new Thread(() -> {
            String[] frames = {"⣾","⣽","⣻","⢿","⡿","⣟","⣯","⣷"};
            int idx = 0;
            while (running.get()) {
                System.out.print("\r" + colorize(label + " " + frames[idx % frames.length], YELLOW));
                idx++;
                try {
                    Thread.sleep(120);
                } catch (InterruptedException ignored) {}
            }
            System.out.print("\r" + colorize(label + " ✓", GREEN) + "\n");
        });
        spinner.setDaemon(true);
        spinner.start();
        return spinner;
    }
    /* ---- BRANDING END ---- */
    /* ---------------- UI ENHANCEMENTS END ---------------- */
    
    /* CONSTRUCTOR */

    public InteractiveView(Controller controller) {
        super(controller);
    }

    /* METODOS OBLIGATORIOS */

    // METODO INICIAR//
    @Override
    public void init() {

        boolean exit = false;

        printHeader("WELCOME");
        pulseMessage("Navigate with numbers and press ENTER", CYAN, 3);
        boolean autoSave = Esdia.yesOrNo("Do you want to autosave after each operation? (y/n): ");
        controller.setAutoSave(autoSave);
        if (!autoSave) {
            showMessage("El guardado se realizará al salir de la aplicación.");
        }

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

    try {
        controller.persistState();
    } catch (Exception ignored) {
        // handled in controller
    }
    showMessage("Closing application...");
    showMessage("Goodbye!");
    return;
    }

    // METODO MOSTRAR MENSAJE //
    @Override
    public void showMessage(String message) {
        System.out.println(colorize("✓ " + message, GREEN));
    }

    // METODO MOSTRAR MENSAJE DE ERROR //
    @Override
    public void showErrorMessage(String errorMessage) {
        System.err.println(colorize("✖ Error: " + errorMessage, RED));
    }


    /* METODOS AUXILIARES */


    private void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux / macOS / otros
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: imprimir muchas líneas en caso de error
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }


    // Mostrar el menu principal //
        private void showMainMenu() {
        
        clearScreen();
        
        printHeader("MAIN MENU");
        pulseMessage("Navigate with numbers and press ENTER", CYAN, 3);
        printMenuItem(1, "CRUD (Questions)", "📝", CYAN);
        printMenuItem(2, "Import / Export", "📦", CYAN);
        printMenuItem(3, "Automatic Question Creation", "🤖", CYAN);
        printMenuItem(4, "Exam Mode", "🧠", CYAN);
        printDivider();
        System.out.println(colorize("🚪 0. Exit", RED));
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
        printHeader("CRUD MENU");
        pulseMessage("Manage your bank of questions", GREEN, 2);
        printMenuItem(1, "Create new question", "➕", CYAN);
        printMenuItem(2, "List questions", "📋", CYAN);
        printDivider();
        System.out.println(colorize("🔙 0. Back to main menu", RED));
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
    printHeader("LIST QUESTIONS");
    pulseMessage("Choose how you want to see the questions", MAGENTA, 2);
    renderStatusBar("Questions loaded: " + controller.getAllQuestions().size(), "");
    printMenuItem(1, "List all questions", "📚", CYAN);
    printMenuItem(2, "List questions by topic", "🎯", CYAN);
    printDivider();
    System.out.println(colorize("🔙 0. Back to CRUD menu", RED));

    int choice = Esdia.readInt(    "Select an option: ",0,2);

    List<Question> questions = new ArrayList<>();

    try {
        if (choice == 1) {
           
            // -- Obtener todas las preguntas --
            questions = controller.getAllQuestions();

        } else if (choice == 2) {

            // -- Preguntar tema disponible --
            String topic = chooseTopic(false);
            if (topic == null) return;

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
            System.out.println(index + ". " + q.getStatement() + " [" + q.getCreationDate() + "]");
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
            printHeader("IMPORT / EXPORT");
            pulseMessage("Backup your work or restore it", BLUE, 2);
            renderStatusBar("Backup: JSON", "");
            animateSectionTransition("Choose backup action");
            printMenuItem(1, "Export questions to JSON", "⬆️", CYAN);
            printMenuItem(2, "Import questions from JSON", "⬇️", CYAN);
            printDivider();
            System.out.println(colorize("🔙 0. Back to main menu", RED));

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
            String filename = Esdia.readString("Enter filename (stored in your home): ").trim();
            animateProgress("Exporting");
            controller.exportQuestions(filename);
            showMessage("Questions exported successfully.");
        } catch (Exception e) {
            showErrorMessage("Export failed: " + e.getMessage());
        }
    }


    private void importQuestions() {
        // Comentario: La vista pide al controller que importe desde JSON.
        try {
            String filename = Esdia.readString("Enter filename to import (from your home): ").trim();
            animateProgress("Importing");
            controller.importQuestions(filename);
            showMessage("Questions imported successfully.");
        } catch (Exception e) {
            showErrorMessage("Import failed: " + e.getMessage());
        }
    }


    private void optionAutomaticQuestion() {

        clearScreen();
        // Comentario: primero verificamos si existen QuestionCreators cargados.
        if (!controller.hasQuestionCreators()) {
            showErrorMessage("There are no automatic question generators available. Please start the app with -question-creator.");
            Esdia.readString("Press ENTER to continue.");
            return;
        }

        printHeader("GEMINI QUESTION CREATION 🤖");
        pulseMessage("Let the AI propose a new question", CYAN, 2);

        List<String> descriptions = controller.getQuestionCreatorDescriptions();
        System.out.println(colorize("Available generators:", BOLD + CYAN));
        for (int i = 0; i < descriptions.size(); i++) {
            System.out.println(colorize((i + 1) + ". " + descriptions.get(i), CYAN));
        }
        int selectedGenerator = Esdia.readInt("Choose generator: ", 1, descriptions.size()) - 1;

        // Pedimos el tema
        String topic = Esdia.readString("Enter topic for the automatic question: ").trim().toUpperCase();

        try {
            // Pedimos al controller que genere una pregunta
            AtomicBoolean running = new AtomicBoolean(true);
            Thread spinner = startSpinner("Contacting Gemini", running);
            Question generated;
            try {
                generated = controller.generateAutomaticQuestion(selectedGenerator, topic);
            } finally {
                running.set(false);
                try {
                    spinner.join();
                } catch (InterruptedException ignored) {}
            }

            if (generated == null) {
                showErrorMessage("No question could be generated for this topic.");
                return;
            }

            // Mostramos la pregunta
            showGeneratedQuestionPreview(generated);

            // Confirmación
            String confirm;
            while (true) {
                confirm = Esdia.readString("Do you want to add this question to the database? (Y/N): ").trim().toUpperCase();
                if (confirm.equals("Y") || confirm.equals("N") || confirm.equals("S")) {
                    break;
                }
                showErrorMessage("Please answer Y or N.");
            }

            if (confirm.equals("Y") || confirm.equals("S")) {
                try {
                    controller.addGeneratedQuestion(generated);
                    showMessage("Question added successfully.");
                } catch (Exception ex) {
                    showErrorMessage("Could not save question: " + ex.getMessage());
                }
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

        printHeader("AUTOMATIC QUESTION PREVIEW 🤖");
        printDivider();
        System.out.println(colorize("Statement:", BOLD + CYAN));
        System.out.println(colorize(q.getStatement(), YELLOW));
        printDivider();
        System.out.println(colorize("Topics: ", BOLD + MAGENTA) + colorize(q.getTopics().toString(), CYAN));
        System.out.println(colorize("Author: ", BOLD + MAGENTA) + colorize(q.getAuthor(), GREEN));
        printDivider();
        System.out.println(colorize("Options:", BOLD + CYAN));

        int i = 1;
        for (Option op : q.getOptions()) {
            String prefix = op.isCorrect() ? colorize("✔", GREEN) : colorize("✖", RED);
            System.out.println(prefix + " " + colorize(i + ". " + op.getText(), BOLD + YELLOW));
            System.out.println(colorize("   Rationale: ", DIM + CYAN) + colorize(op.getRationale(), CYAN));
            i++;
        }
        printDivider();
    }

    private String chooseTopic(boolean includeAll) {
        Set<String> topics = controller.getAvailableTopics();
        if (topics.isEmpty()) {
            showErrorMessage("No topics available.");
            return null;
        }
        List<String> topicList = new ArrayList<>(topics);
        topicList.sort(String::compareTo);
        if (includeAll) {
            topicList.add("ALL");
        }

        System.out.println("\nAvailable topics:");
        for (int i = 0; i < topicList.size(); i++) {
            System.out.println((i + 1) + ". " + topicList.get(i));
        }
        int choice = Esdia.readInt("Select topic: ", 1, topicList.size());
        return topicList.get(choice - 1);
    }

    private void optionExamMode() {
        clearScreen();
        printHeader("EXAM MODE 🧠");
        renderStatusBar("Backup: " + controller.getBackupDescription(), "");

        String topic = chooseTopic(true);
        if (topic == null) return;

        int maxQuestions = controller.getMaxQuestionsForTopic(topic);
        if (maxQuestions == 0) {
            showErrorMessage("No questions available for that topic.");
            return;
        }

        int numQuestions = Esdia.readInt("Enter the number of questions for the exam (1-" + maxQuestions + "): ", 1, maxQuestions);

        ExamSession session;
        try {
            session = controller.configureExam(topic, numQuestions);
        } catch (Exception e) {
            showErrorMessage("Could not prepare exam: " + e.getMessage());
            return;
        }

        printDivider();
        System.out.println(colorize("Exam starting... Good luck!", GREEN));

        List<Question> examQuestions = session.getQuestions();

        for (int i = 0; i < examQuestions.size(); i++) {
            Question q = examQuestions.get(i);

            System.out.println("\nQuestion " + (i + 1) + " of " + examQuestions.size() + ":");
            System.out.println(q.getStatement());

            List<Option> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j).getText());
            }

            String answer = Esdia.readString_ne("Select an option (1-4). Press ENTER to leave unanswered: ");
            int selected = 0;
            if (!answer.isBlank()) {
                try {
                    selected = Integer.parseInt(answer);
                    if (selected < 1 || selected > 4) {
                        selected = 0;
                    }
                } catch (NumberFormatException e) {
                    selected = 0;
                }
            }

            String feedback = controller.answerQuestion(session, i, selected);
            showMessage(feedback);
        }

        ExamResult result = controller.finishExam(session);
        showExamSummary(result);
    }


    // Comentario: muestra un resumen con aciertos, fallos, no respondidas y nota.
    private void showExamSummary(ExamResult result) {

        clearScreen();

        printHeader("EXAM SUMMARY");

        System.out.println("Correct answers: " + result.getCorrect());
        System.out.println("Wrong answers:   " + result.getWrong());
        System.out.println("Unanswered:      " + result.getUnanswered());
        renderScoreBar(result.getGrade());
        System.out.println("Time (s):        " + result.getDurationSeconds());

        showMessage("Exam finished. Press ENTER to continue.");
        Esdia.readString(" ");
    }

    private void viewQuestionDetail(Question question) {

        clearScreen();
        // Comentario: Mostrar todos los detalles de la pregunta que el usuario seleccionó.
        printHeader("QUESTION DETAIL 🔍");

        System.out.println("ID: " + question.getId());
        System.out.println("Author: " + question.getAuthor());
        System.out.println("Created: " + question.getCreationDate());
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

        // Menú de acciones específicas sobre esta pregunta
        printDivider();
        System.out.println(colorize("AVAILABLE ACTIONS", BOLD + YELLOW));
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
            printHeader("MODIFY QUESTION ✏️");

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
