package backup;

import java.util.List;

import model.Question;

/**
 * Abstracción para importar/exportar preguntas a distintos formatos.
 */
public interface QuestionBackupIO {

    void exportQuestions(List<Question> questions, String fileName) throws QuestionBackupIOException;

    List<Question> importQuestions(String fileName) throws QuestionBackupIOException;

    String getBackupIODescription();
}
