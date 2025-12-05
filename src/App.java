import controller.Controller;
import model.GeminiQuestionCreator;
import model.Model;
import model.QuestionCreator;
import model.RepositoryException;
import view.InteractiveView;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            List<QuestionCreator> creators = parseQuestionCreators(args);

            Model model = Model.createDefault(creators);
            Controller controller = new Controller(model);
            InteractiveView view = new InteractiveView(controller);
            controller.setView(view);

            controller.start();
        } catch (RepositoryException e) {
            System.err.println("Error initializing application: " + e.getMessage());
        }
    }

    private static List<QuestionCreator> parseQuestionCreators(String[] args) {
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
