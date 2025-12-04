package model;

import java.util.List;

public interface IRepository {

    void save(List<Question> questions);

    List<Question> load();
}
