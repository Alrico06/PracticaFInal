package model;

import java.util.List;

/**
 * Abstracción para importar/exportar preguntas a distintos formatos.
 */
public interface QuestionBackupIO {

    void exportQuestions(List<Question> questions, String fileName) throws QuestionBackupIOException;

    List<Question> importQuestions(String fileName) throws QuestionBackupIOException;

    String getBackupIODescription();
}
