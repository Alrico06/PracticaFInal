package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import backup.JSONQuestionBackupIO;
import backup.QuestionBackupIO;
import backup.QuestionBackupIOException;
import questionCreator.QuestionCreator;
import questionCreator.QuestionCreatorException;
import repository.BinaryRepository;
import repository.IRepository;
import repository.RepositoryException;

public class Model {

    private final IRepository repository;
    private final QuestionBackupIO backupHandler;
    private final List<QuestionCreator> questionCreators;

    private List<Question> questions;
    private boolean autoSave = true;

    public Model(IRepository repository, QuestionBackupIO backupHandler, List<QuestionCreator> questionCreators) throws RepositoryException {
        this.repository = repository;
        this.backupHandler = backupHandler;
        this.questionCreators = questionCreators != null ? questionCreators : new ArrayList<>();
        this.questions = new ArrayList<>(repository.getAllQuestions());
    }

    /**
     * Factory that instantiates default persistence components inside the model layer.
     */
    public static Model createDefault(List<QuestionCreator> questionCreators) throws RepositoryException {
        IRepository repository = new BinaryRepository("questions.bin");
        QuestionBackupIO backupHandler = new JSONQuestionBackupIO();
        return new Model(repository, backupHandler, questionCreators);
    }

    /* ============================================================
     *                    CONFIGURACIÓN DE GUARDADO
     * ============================================================ 
     */
    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void persistState() throws RepositoryException {
        repository.saveAll(questions);
    }

    private void persistIfNeeded() throws RepositoryException {
        if (autoSave) {
            persistState();
        }
    }

    /* ============================================================
     *                    MÉTODOS DEL CRUD
     * ============================================================ 
     */

    public Question createQuestion(
            String author,
            String statement,
            Set<String> topics,
            List<String> optionTexts,
            List<String> optionRationales,
            int correctIndex
    ) throws RepositoryException {
        validateBaseFields(author, statement, topics);
        validateOptions(optionTexts, optionRationales, correctIndex);

        List<Option> options = new ArrayList<>();
        for (int i = 0; i < optionTexts.size(); i++) {
            boolean isCorrect = (i + 1 == correctIndex);
            options.add(new Option(optionTexts.get(i), optionRationales.get(i), isCorrect));
        }

        Question q = new Question(author, statement, topics, options);
        repository.addQuestion(q);
        refreshCache();
        persistIfNeeded();
        return q;
    }

    private void validateOptions(List<String> texts, List<String> rationales, int correctIndex) throws RepositoryException {
        if (texts == null || rationales == null || texts.size() != 4 || rationales.size() != 4) {
            throw new RepositoryException("Questions must have exactly 4 options.");
        }
        if (correctIndex < 1 || correctIndex > 4) {
            throw new RepositoryException("Correct option must be between 1 and 4.");
        }
        for (String t : texts) {
            if (t == null || t.trim().isEmpty()) {
                throw new RepositoryException("Option text cannot be empty.");
            }
        }
        for (String r : rationales) {
            if (r == null || r.trim().isEmpty()) {
                throw new RepositoryException("Option rationale cannot be empty.");
            }
        }
    }

    private void validateBaseFields(String author, String statement, Set<String> topics) throws RepositoryException {
        if (author == null || author.trim().isEmpty()) {
            throw new RepositoryException("Author cannot be empty.");
        }
        if (statement == null || statement.trim().isEmpty()) {
            throw new RepositoryException("Statement cannot be empty.");
        }
        if (topics == null || topics.isEmpty()) {
            throw new RepositoryException("Topics cannot be empty.");
        }
        for (String t : topics) {
            if (t == null || t.trim().isEmpty()) {
                throw new RepositoryException("Topics cannot contain empty values.");
            }
        }
    }

    public List<Question> getAllQuestionsSorted() {
        return questions.stream()
                .sorted(Comparator.comparing(Question::getCreationDate))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByTopic(String topic) {
        return questions.stream()
                .filter(q -> q.getTopics().contains(topic.toUpperCase()))
                .sorted(Comparator.comparing(Question::getCreationDate))
                .collect(Collectors.toList());
    }

    public Set<String> getAvailableTopics() {
        Set<String> topics = new HashSet<>();
        for (Question q : questions) {
            topics.addAll(q.getTopics());
        }
        return topics;
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public Question getQuestionById(UUID id) {
        return questions.stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void deleteQuestion(Question q) throws RepositoryException {
        repository.removeQuestion(q);
        refreshCache();
        persistIfNeeded();
    }

    public void modifyAuthor(Question q, String newAuthor) throws RepositoryException {
        if (newAuthor == null || newAuthor.trim().isEmpty()) {
            throw new RepositoryException("Author cannot be empty.");
        }
        q.setAuthor(newAuthor);
        repository.modifyQuestion(q);
        refreshCache();
        persistIfNeeded();
    }

    public void modifyTopics(Question q, Set<String> newTopics) throws RepositoryException {
        validateBaseFields(q.getAuthor(), q.getStatement(), newTopics);
        q.setTopics(newTopics);
        repository.modifyQuestion(q);
        refreshCache();
        persistIfNeeded();
    }

    public void modifyStatement(Question q, String newStatement) throws RepositoryException {
        if (newStatement == null || newStatement.trim().isEmpty()) {
            throw new RepositoryException("Statement cannot be empty.");
        }
        q.setStatement(newStatement);
        repository.modifyQuestion(q);
        refreshCache();
        persistIfNeeded();
    }

    public void modifyOptions(
            Question q,
            List<String> texts,
            List<String> rationales,
            int correctIndex
    ) throws RepositoryException {
        validateOptions(texts, rationales, correctIndex);

        List<Option> newOptions = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            boolean isCorrect = (i + 1 == correctIndex);
            newOptions.add(new Option(texts.get(i), rationales.get(i), isCorrect));
        }

        q.setOptions(newOptions);
        repository.modifyQuestion(q);
        refreshCache();
        persistIfNeeded();
    }

    /* ============================================================
     *                    IMPORT / EXPORT JSON
     * ============================================================ 
     */

    public void exportQuestions(String fileName) throws QuestionBackupIOException {
        backupHandler.exportQuestions(questions, normalizeFileName(fileName));
    }

    public void importQuestions(String fileName) throws QuestionBackupIOException, RepositoryException {
        List<Question> imported = backupHandler.importQuestions(normalizeFileName(fileName));
        // Política: si alguna pregunta es inválida, abortar toda la importación.
        for (Question q : imported) {
            validateImportedQuestion(q);
        }

        for (Question q : imported) {
            boolean exists = questions.stream()
                    .anyMatch(x -> x.getId().equals(q.getId()));

            if (!exists) {
                repository.addQuestion(q);
            }
        }

        refreshCache();
        persistIfNeeded();
    }

    private String normalizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "backup.json";
        }
        return fileName;
    }

    /* ============================================================
     *               PREGUNTAS AUTOMÁTICAS (ONE MORE THING)
     * ============================================================
     */

    public boolean hasQuestionCreators() {
        return !questionCreators.isEmpty();
    }

    public List<String> getQuestionCreatorDescriptions() {
        return questionCreators.stream()
                .map(QuestionCreator::getQuestionCreatorDescription)
                .collect(Collectors.toList());
    }

    public Question generateAutomaticQuestion(int creatorIndex, String topic) throws QuestionCreatorException {
        if (creatorIndex < 0 || creatorIndex >= questionCreators.size()) {
            throw new QuestionCreatorException("Invalid generator selected");
        }
        return questionCreators.get(creatorIndex).createQuestion(topic);
    }

    public Question addGeneratedQuestion(Question q) throws RepositoryException {
        if (q == null) {
            throw new RepositoryException("Generated question is empty.");
        }
        validateBaseFields(q.getAuthor(), q.getStatement(), q.getTopics());
        List<String> texts = q.getOptions().stream().map(Option::getText).collect(Collectors.toList());
        List<String> rationales = q.getOptions().stream().map(Option::getRationale).collect(Collectors.toList());
        int correctIndex = extractSingleCorrectIndex(q.getOptions());
        validateOptions(texts, rationales, correctIndex);

        repository.addQuestion(q);
        refreshCache();
        persistIfNeeded();
        return q;
    }

    /* ============================================================
     *                      MODO EXAMEN
     * ============================================================
     */

    public int getMaxQuestionsForTopic(String topic) {
        if ("ALL".equalsIgnoreCase(topic)) {
            return questions.size();
        }
        return (int) questions.stream().filter(q -> q.getTopics().contains(topic.toUpperCase())).count();
    }

    public ExamSession configureExam(String topic, int num) throws RepositoryException {
        List<Question> pool;
        if ("ALL".equalsIgnoreCase(topic)) {
            pool = new ArrayList<>(questions);
        } else {
            pool = questions.stream()
                    .filter(q -> q.getTopics().contains(topic.toUpperCase()))
                    .collect(Collectors.toList());
        }

        if (pool.isEmpty()) {
            throw new RepositoryException("No questions available for topic " + topic);
        }
        if (num < 1 || num > pool.size()) {
            throw new RepositoryException("Number of questions must be between 1 and " + pool.size());
        }

        Collections.shuffle(pool);
        List<Question> selected = new ArrayList<>(pool.subList(0, num));
        return new ExamSession(selected);
    }

    public String answerQuestion(ExamSession session, int questionIndex, int answerIndex) {
        session.recordAnswer(questionIndex, answerIndex);
        Question q = session.getQuestions().get(questionIndex);

        if (answerIndex == 0) {
            return "Unanswered. Correct: " + findCorrectOption(q).getText();
        }

        Option chosen = q.getOptions().get(answerIndex - 1);
        Option correct = findCorrectOption(q);
        if (chosen.isCorrect()) {
            return "Correct. Rationale: " + chosen.getRationale();
        }
        return "Incorrect. You chose: " + chosen.getText() +
                " (" + chosen.getRationale() + "). Correct: " +
                correct.getText() + " (" + correct.getRationale() + ").";
    }

    private Option findCorrectOption(Question q) {
        return q.getOptions().stream()
                .filter(Option::isCorrect)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Question has no correct option set"));
    }

    public ExamResult finishExam(ExamSession session) {
        session.finish();
        List<Question> qs = session.getQuestions();
        List<Integer> answers = session.getAnswers();

        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        for (int i = 0; i < qs.size(); i++) {
            int ans = answers.get(i);
            if (ans == 0) {
                unanswered++;
                continue;
            }
            Option selected = qs.get(i).getOptions().get(ans - 1);
            if (selected.isCorrect()) {
                correct++;
            } else {
                wrong++;
            }
        }

        double value = 10.0 / qs.size();
        double score = correct * value - wrong * (value / 3.0);
        double grade = Math.max(0, Math.min(10, score));

        return new ExamResult(correct, wrong, unanswered, grade, session.getDurationSeconds());
    }

    public String getBackupDescription() {
        return backupHandler.getBackupIODescription();
    }

    private void refreshCache() throws RepositoryException {
        this.questions = new ArrayList<>(repository.getAllQuestions());
    }

    private int extractSingleCorrectIndex(List<Option> options) throws RepositoryException {
        if (options == null || options.size() != 4) {
            throw new RepositoryException("Questions must have exactly 4 options.");
        }
        int correctIndex = -1;
        for (int i = 0; i < options.size(); i++) {
            Option opt = options.get(i);
            if (opt == null) {
                throw new RepositoryException("Option cannot be null.");
            }
            if (opt.isCorrect()) {
                if (correctIndex != -1) {
                    throw new RepositoryException("Questions must have exactly one correct option.");
                }
                correctIndex = i + 1;
            }
        }
        if (correctIndex == -1) {
            throw new RepositoryException("Questions must have exactly one correct option.");
        }
        return correctIndex;
    }

    private void validateImportedQuestion(Question q) throws RepositoryException {
        if (q == null) {
            throw new RepositoryException("Invalid question data.");
        }
        validateBaseFields(q.getAuthor(), q.getStatement(), q.getTopics());
        List<String> texts = q.getOptions().stream().map(Option::getText).collect(Collectors.toList());
        List<String> rationales = q.getOptions().stream().map(Option::getRationale).collect(Collectors.toList());
        int correctIndex = extractSingleCorrectIndex(q.getOptions());
        validateOptions(texts, rationales, correctIndex);
    }
}
