package model;

public class ExamResult {

    private final int correct;
    private final int wrong;
    private final int unanswered;
    private final double grade;
    private final long durationSeconds;

    public ExamResult(int correct, int wrong, int unanswered, double grade, long durationSeconds) {
        this.correct = correct;
        this.wrong = wrong;
        this.unanswered = unanswered;
        this.grade = grade;
        this.durationSeconds = durationSeconds;
    }

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
    }

    public int getUnanswered() {
        return unanswered;
    }

    public double getGrade() {
        return grade;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public String toString() {
        return "ExamResult{correct=" + correct +
                ", wrong=" + wrong +
                ", unanswered=" + unanswered +
                ", grade=" + grade +
                ", durationSeconds=" + durationSeconds + "}";
    }
}
