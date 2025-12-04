package controller;

import model.ExamResult;
import model.ExamSession;
import model.Model;
import model.Question;
import model.QuestionCreatorException;
import model.QuestionBackupIOException;
import model.RepositoryException;
import view.BaseView;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Controller {
    

    private Model model;
    private BaseView view;

    public Controller(){
    }

    public Controller(Model model) {
        this.model = model;
    }
    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
    }

    public void setView(BaseView view) {
        this.view = view;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void start() {
        if (view != null) {
            view.showMessage("Loaded " + model.getQuestionCount() + " questions from storage. Backup handler: " + model.getBackupDescription());
            view.init();
        }
    }

    public void end() {
        if (view != null) {
            view.end();
        }
    }

    public void createQuestion(
        String author,
        String statement,
        Set<String> topics,
        List<String> optionTexts,
        List<String> optionRationales,
        int correctIndex
    ) throws RepositoryException {
        model.createQuestion(author, statement, topics, optionTexts, optionRationales, correctIndex);
    }


    public List<Question> getAllQuestions() {
        return model.getAllQuestionsSorted();
    }


    public List<Question> getQuestionsByTopic(String topic) {
        return model.getQuestionsByTopic(topic);
    }


    public Question getQuestionById(UUID id) {
        return model.getQuestionById(id);
    }


    public void deleteQuestion(Question q) {
        try {
            model.deleteQuestion(q);
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }


    public void exportQuestions(String fileName) throws QuestionBackupIOException {
        model.exportQuestions(fileName);
    }


    public void importQuestions(String fileName) throws QuestionBackupIOException, RepositoryException {
        model.importQuestions(fileName);
    }


    public boolean hasQuestionCreators() {
        return model.hasQuestionCreators();
    }

    public List<String> getQuestionCreatorDescriptions() {
        return model.getQuestionCreatorDescriptions();
    }

    public Question generateAutomaticQuestion(int creatorIndex, String topic) throws QuestionCreatorException {
        return model.generateAutomaticQuestion(creatorIndex, topic);
    }


    public void addGeneratedQuestion(Question q) throws RepositoryException {
        model.addGeneratedQuestion(q);
    }


    public int getMaxQuestionsForTopic(String topic) {
        return model.getMaxQuestionsForTopic(topic);
    }

    public ExamSession configureExam(String topic, int num) throws RepositoryException {
        return model.configureExam(topic, num);
    }

    public String answerQuestion(ExamSession session, int index, int answerIndex) {
        return model.answerQuestion(session, index, answerIndex);
    }

    public ExamResult finishExam(ExamSession session) {
        return model.finishExam(session);
    }

    public void modifyAuthor(Question q, String newAuthor) throws RepositoryException {
        model.modifyAuthor(q, newAuthor);
    }

    public void modifyTopics(Question q, Set<String> newTopics) throws RepositoryException {
        model.modifyTopics(q, newTopics);
    }

    public void modifyStatement(Question q, String newStatement) throws RepositoryException {
        model.modifyStatement(q, newStatement);
    }

    public void modifyOptions(
            Question q,
            List<String> texts,
            List<String> rationales,
            int correctIndex
    ) throws RepositoryException {
        model.modifyOptions(q, texts, rationales, correctIndex);
    }

    public Set<String> getAvailableTopics() {
        return model.getAvailableTopics();
    }

    public void setAutoSave(boolean autoSave) {
        model.setAutoSave(autoSave);
    }

    public String getBackupDescription() {
        return model.getBackupDescription();
    }

    public void persistState() {
        try {
            model.persistState();
        } catch (RepositoryException e) {
            if (view != null) {
                view.showErrorMessage("Error saving data: " + e.getMessage());
            }
        }
    }
}
