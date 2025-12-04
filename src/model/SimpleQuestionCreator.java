package model;

import java.util.*;

public class SimpleQuestionCreator implements QuestionCreator {

    @Override
    public Question generate(String topic) {

        String statement = "Automatically generated question for topic: " + topic;

        List<Option> options = new ArrayList<>();

        options.add(new Option("Correct answer", "Generated automatically", true));
        options.add(new Option("Wrong answer 1", "Generated automatically", false));
        options.add(new Option("Wrong answer 2", "Generated automatically", false));
        options.add(new Option("Wrong answer 3", "Generated automatically", false));

        Set<String> topics = new HashSet<>();
        topics.add(topic.toUpperCase());

        return new Question("AUTO", statement, topics, options);
    }
}
