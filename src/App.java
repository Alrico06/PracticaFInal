import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import model.Model;
import questionCreator.GeminiQuestionCreator;
import questionCreator.QuestionCreator;
import repository.RepositoryException;
import view.InteractiveView;

/**
 * @author Alvaro
 * @version 1.0
 * Clase principal para iniciar la aplicación de preguntas y respuestas.
 */





public class App {
    public static void main(String[] args) {
        try {
            // Crear la lista de generadores automáticos desde los argumentos
            List<QuestionCreator> creators = parseQuestionCreators(args);

            // Instanciar modelo, controlador y vista interactiva
            Model model = Model.createDefault(creators);
            Controller controller = new Controller(model);
            InteractiveView view = new InteractiveView(controller);
            controller.setView(view);

            // Lanzar la aplicación
            controller.start();
        } catch (RepositoryException e) {
            System.err.println("Error initializing application: " + e.getMessage());
        }
    }

    private static List<QuestionCreator> parseQuestionCreators(String[] args) {
        // Leer el flag -question-creator modelo apiKey de la línea de comandos
        List<QuestionCreator> creators = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if ("-question-creator".equalsIgnoreCase(args[i]) && i + 2 < args.length) {
                String modelId = args[i + 1];
                String apiKey = args[i + 2];
                for (String singleModel : modelId.split(",")) {
                    String clean = singleModel.trim();
                    if (!clean.isEmpty()) {
                        creators.add(new GeminiQuestionCreator(clean, apiKey));
                    }
                }
                i += 2;
            }
        }
        return creators;
    }
}
