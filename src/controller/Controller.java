package controller;

import model.Question;
import model.ExamResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Controller {
    
    public void createQuestion(String temp1, String temp2, HashSet<String> temp3, List<String> temp4,List<String> temp5, int temp6  ) {
        // Lógica para crear una pregunta   

    }

    public List<Question> getAllQuestions(){
        return null;
    }

    public List<Question> getQuestionsByTopic(String topic){
        return null;
    }

    public Question getQuestionById(UUID id){

        return null;
    }

    public void deleteQuestion(Question q){

    }

    public void exportQuestions(){

    }

    public void importQuestions(){

    }

    public boolean hasQuestionCreators(){
        return false;
    }

    public Question generateAutomaticQuestion(String topic)
    {
        return null;
    }

    public void addGeneratedQuestion(Question q)
    {

    }

    public List <Question> getExamQuestions(int num, String topic){
        return null;
    }

    public ExamResult evaluateExam(List <Question> questions, List<Integer> userAnswers){
        return null;
    }


    public void modifyAuthor(Question q, String newAuthor){}
    public void modifyTopics(Question q, Set<String> newTopics){}
    public void modifyStatement(Question q, String newStatement){}
    public void modifyOptions(Question q, List<String> texts, List<String> rationales, int correctIndex){}



}
