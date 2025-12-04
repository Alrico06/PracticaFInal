package controller;

import model.Question;
import view.BaseView;
import model.ExamResult;
import model.Model;

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
    ) {
        model.createQuestion(author, statement, topics, optionTexts, optionRationales, correctIndex);
    }


    public List<Question> getAllQuestions() {
        return model.getAllQuestions();
    }


    public List<Question> getQuestionsByTopic(String topic) {
        return model.getQuestionsByTopic(topic);
    }


    public Question getQuestionById(UUID id) {
        return model.getQuestionById(id);
    }


    public void deleteQuestion(Question q) {
        model.deleteQuestion(q);
    }


    public void exportQuestions() {
        model.exportQuestions();
    }


    public void importQuestions() {
        model.importQuestions();
    }


    public boolean hasQuestionCreators() {
        return model.hasQuestionCreators();
    }


    public Question generateAutomaticQuestion(String topic) {
        return model.generateAutomaticQuestion(topic);
    }


    public void addGeneratedQuestion(Question q) {
        model.addGeneratedQuestion(q);
    }


    public List<Question> getExamQuestions(int num, String topic) {
        return model.getExamQuestions(num, topic);
    }   


    public ExamResult evaluateExam(List<Question> questions, List<Integer> userAnswers) {
        return model.evaluateExam(questions, userAnswers);
    }

    public void modifyAuthor(Question q, String newAuthor) {
        model.modifyAuthor(q, newAuthor);
    }

    public void modifyTopics(Question q, Set<String> newTopics) {
        model.modifyTopics(q, newTopics);
    }

    public void modifyStatement(Question q, String newStatement) {
        model.modifyStatement(q, newStatement);
    }

    public void modifyOptions(
            Question q,
            List<String> texts,
            List<String> rationales,
            int correctIndex
    ) {
        model.modifyOptions(q, texts, rationales, correctIndex);
    }
}
