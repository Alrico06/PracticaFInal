package model;

import java.util.*;
import java.util.stream.Collectors;

public class Model {

    private List<Question> questions;

    // Repositorio binario
    private IRepository binaryRepo;

    // Import/export JSON
    private JSONQuestionBackupIO jsonIO;

    // Creadores automáticos
    private List<QuestionCreator> creators;

    public Model() {

        this.questions = new ArrayList<>();
        this.creators = new ArrayList<>();

        // Creador automático
        this.creators.add(new SimpleQuestionCreator());

        // Inicializar repositorios
        this.jsonIO = new JSONQuestionBackupIO();
        this.binaryRepo = new BinaryRepository("questions.dat");
    }

    /* ============================================================
     *                    MÉTODOS DEL CRUD
     * ============================================================ 
     */

    public void createQuestion(
            String author,
            String statement,
            Set<String> topics,
            List<String> optionTexts,
            List<String> optionRationales,
            int correctIndex
    ) {
        List<Option> options = new ArrayList<>();

        for (int i = 0; i < optionTexts.size(); i++) {
            boolean isCorrect = (i + 1 == correctIndex);
            options.add(new Option(optionTexts.get(i), optionRationales.get(i), isCorrect));
        }

        Question q = new Question(author, statement, topics, options);
        questions.add(q);

        binaryRepo.save(questions);
    }


    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions);
    }


    public List<Question> getQuestionsByTopic(String topic) {
        return questions.stream()
                .filter(q -> q.getTopics().contains(topic))
                .collect(Collectors.toList());
    }


    public Question getQuestionById(UUID id) {
        return questions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public void deleteQuestion(Question q) {
        questions.remove(q);
        binaryRepo.save(questions);
    }


    public void modifyAuthor(Question q, String newAuthor) {
        q.setAuthor(newAuthor);
        binaryRepo.save(questions);
    }

    public void modifyTopics(Question q, Set<String> newTopics) {
        q.setTopics(newTopics);
        binaryRepo.save(questions);
    }

    public void modifyStatement(Question q, String newStatement) {
        q.setStatement(newStatement);
        binaryRepo.save(questions);
    }

    public void modifyOptions(
            Question q,
            List<String> texts,
            List<String> rationales,
            int correctIndex
    ) {
        List<Option> newOptions = new ArrayList<>();

        for (int i = 0; i < texts.size(); i++) {
            boolean isCorrect = (i + 1 == correctIndex);
            newOptions.add(new Option(texts.get(i), rationales.get(i), isCorrect));
        }

        q.setOptions(newOptions);
        binaryRepo.save(questions);
    }

    /* ============================================================
     *                    IMPORT / EXPORT JSON
     * ============================================================ 
     */

    public void exportQuestions() {
        jsonIO.exportQuestions(questions);
    }


    public void importQuestions() {

        List<Question> imported = jsonIO.importQuestions();

        for (Question q : imported) {

            boolean exists = questions.stream()
                    .anyMatch(x -> x.getId().equals(q.getId()));

            if (!exists) {
                questions.add(q);
            }
        }

        binaryRepo.save(questions);
    }

    /* ============================================================
     *               PREGUNTAS AUTOMÁTICAS (ONE MORE THING)
     * ============================================================
     */

    public boolean hasQuestionCreators() {
        return !creators.isEmpty();
    }

    public Question generateAutomaticQuestion(String topic) {

        if (creators.isEmpty()) return null;

        QuestionCreator creator = creators.get(0);

        return creator.generate(topic);
    }

    public void addGeneratedQuestion(Question q) {
        if (q != null) {
            questions.add(q);
            binaryRepo.save(questions);
        }
    }

    /* ============================================================
     *                      MODO EXAMEN
     * ============================================================
     */

    public List<Question> getExamQuestions(int num, String topic) {

        List<Question> pool;

        if (topic.equalsIgnoreCase("ALL")) {
            pool = new ArrayList<>(questions);
        } else {
            pool = questions.stream()
                    .filter(q -> q.getTopics().contains(topic))
                    .collect(Collectors.toList());
        }

        if (pool.size() < num) return new ArrayList<>();

        Collections.shuffle(pool);

        return pool.subList(0, num);
    }


    public ExamResult evaluateExam(
            List<Question> questions,
            List<Integer> userAnswers
    ) {
        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        for (int i = 0; i < questions.size(); i++) {

            int ans = userAnswers.get(i);

            if (ans == 0) {
                unanswered++;
                continue;
            }

            Option selected = questions.get(i).getOptions().get(ans - 1);

            if (selected.isCorrect()) {
                correct++;
            } else {
                wrong++;
            }
        }

        double grade = 10.0 * correct / questions.size();

        return new ExamResult(correct, wrong, unanswered, grade);
    }

}
