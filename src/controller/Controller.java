package controller;

import model.Question;
import java.util.HashSet;
import java.util.List;
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



}
