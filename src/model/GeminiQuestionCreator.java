package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementación de QuestionCreator usando Gemini (GenAI Fat-JAR).
 * El código de integración se inspira en el ejemplo incluido en PDF/ejemplo.java.
 */
public class GeminiQuestionCreator implements QuestionCreator {

    private final String modelId;
    private final String apiKey;
    private final String description;

    public GeminiQuestionCreator(String modelId, String apiKey) {
        this.modelId = modelId;
        this.apiKey = apiKey;
        this.description = "Gemini (" + modelId + ")";
    }

    @Override
    public Question createQuestion(String topic) throws QuestionCreatorException {
        if (topic == null || topic.isBlank()) {
            throw new QuestionCreatorException("Topic required");
        }

        try {
            // Carga dinámica de la librería GenAI (sin dependencia de compilación)
            Class<?> cfgClass = Class.forName("es.usal.genai.GenAiConfig");
            Class<?> facadeClass = Class.forName("es.usal.genai.GenAiFacade");
            Class<?> schemasClass = Class.forName("es.usal.genai.SimpleSchemas");
            Class<?> schemaClass = Class.forName("com.google.genai.types.Schema");

            // GenAiConfig.forGemini(modelId, apiKey)
            Object config = cfgClass
                    .getMethod("forGemini", String.class, String.class)
                    .invoke(null, modelId, apiKey);

            Object schema = schemasClass.getMethod("from", Class.class).invoke(null, QuestionDTO.class);

            String prompt = buildPrompt(topic.trim());

            QuestionDTO dto;
            // try-with-resources manual usando reflection para close()
            Object facade = facadeClass.getConstructor(cfgClass).newInstance(config);
            try {
                dto = (QuestionDTO) facadeClass
                        .getMethod("generateJson", String.class, schemaClass, Class.class)
                        .invoke(facade, prompt, schema, QuestionDTO.class);
            } finally {
                try {
                    facadeClass.getMethod("close").invoke(facade);
                } catch (Exception ignored) {}
            }

            if (dto == null || dto.options == null || dto.options.size() != 4) {
                throw new QuestionCreatorException("Invalid response from Gemini");
            }

            List<Option> options = new ArrayList<>();
            for (OptionDTO opt : dto.options) {
                options.add(new Option(opt.text, opt.rationale, opt.correct));
            }

            Set<String> topics = new HashSet<>();
            topics.addAll(normalizeTopics(dto.topics));
            topics.add(topic.trim().toUpperCase());

            return new Question(description, dto.statement, topics, options);
        } catch (Exception e) {
            // Fallback local para no dejar la opción vacía
            return fallbackQuestion(topic, e.getMessage());
        }
    }

    private String buildPrompt(String topic) {
        return "Devuelve EXCLUSIVAMENTE un JSON válido con el esquema {\"author\":string,\"statement\":string,"
                + "\"topics\":string[],\"options\":[{\"text\":string,\"rationale\":string,\"correct\":boolean}]}. "
                + "Genera una pregunta tipo test sobre el tema \"" + topic + "\" con exactamente 4 opciones y solo una correcta. "
                + "Incluye al menos un topic en mayúsculas relacionado con \"" + topic + "\". "
                + "La rationale debe explicar por qué es correcta o incorrecta cada opción.";
    }

    private Set<String> normalizeTopics(List<String> raw) {
        Set<String> result = new HashSet<>();
        if (raw == null) return result;
        for (String t : raw) {
            if (t != null && !t.isBlank()) {
                result.add(t.trim().toUpperCase());
            }
        }
        return result;
    }

    @Override
    public String getQuestionCreatorDescription() {
        return description;
    }

    private Question fallbackQuestion(String topic, String reason) {
        String normalizedTopic = topic == null ? "GENERAL" : topic.trim().toUpperCase();
        String statement = "Fallback question for topic: " + normalizedTopic + " (Gemini unavailable)";
        List<Option> options = new ArrayList<>();
        options.add(new Option("Correct answer about " + normalizedTopic,
                "Generated locally because Gemini failed: " + reason, true));
        options.add(new Option("Incorrect option 1", "Placeholder distractor", false));
        options.add(new Option("Incorrect option 2", "Placeholder distractor", false));
        options.add(new Option("Incorrect option 3", "Placeholder distractor", false));

        Set<String> topics = new HashSet<>();
        topics.add(normalizedTopic);

        return new Question(description, statement, topics, options);
    }

    /**
     * DTOs públicos para structured output.
     */
    public static class QuestionDTO {
        public String author;
        public String statement;
        public List<String> topics;
        public List<OptionDTO> options;
    }

    public static class OptionDTO {
        public String text;
        public String rationale;
        public boolean correct;
    }
}
