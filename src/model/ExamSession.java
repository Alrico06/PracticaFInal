package model;

import java.util.ArrayList;
import java.util.List;

public class ExamSession {

    private final List<Question> questions;
    private final List<Integer> answers;
    private final long startMillis;
    private long endMillis;

    public ExamSession(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
        this.answers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            this.answers.add(0);
        }
        this.startMillis = System.currentTimeMillis();
        this.endMillis = -1;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void recordAnswer(int index, int answer) {
        if (index < 0 || index >= answers.size()) return;
        answers.set(index, answer);
    }

    public void finish() {
        this.endMillis = System.currentTimeMillis();
    }

    public long getDurationSeconds() {
        long end = endMillis == -1 ? System.currentTimeMillis() : endMillis;
        return Math.max(0, (end - startMillis) / 1000);
    }
}
