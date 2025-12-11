package backup;

import java.util.List;

import model.Question;

public interface QuestionBackupIO {

    void exportQuestions(List<Question> questions, String fileName) throws QuestionBackupIOException;

    List<Question> importQuestions(String fileName) throws QuestionBackupIOException;

    String getBackupIODescription();
}
