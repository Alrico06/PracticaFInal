package repository;

import java.util.List;

import model.Question;

/**
 * Abstracción de acceso a persistencia del banco de preguntas.
 */
public interface IRepository {

    Question addQuestion(Question q) throws RepositoryException;

    void removeQuestion(Question q) throws RepositoryException;

    Question modifyQuestion(Question q) throws RepositoryException;

    List<Question> getAllQuestions() throws RepositoryException;

    /**
     * Guarda el estado completo del banco de preguntas.
     */
    void saveAll(List<Question> questions) throws RepositoryException;
}
