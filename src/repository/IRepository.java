package repository;

import java.util.List;

import model.Question;

public interface IRepository {

    Question addQuestion(Question q) throws RepositoryException;

    void removeQuestion(Question q) throws RepositoryException;

    Question modifyQuestion(Question q) throws RepositoryException;

    List<Question> getAllQuestions() throws RepositoryException;

    void saveAll(List<Question> questions) throws RepositoryException;
}
