package model;

/**
 * Clase que representa el resultado de un examen.
 * Contiene aciertos, fallos, no respondidas y nota sobre 10.
 */
public class ExamResult {

    private int correct;
    private int wrong;
    private int unanswered;
    private double grade;

    public ExamResult(int correct, int wrong, int unanswered, double grade) {
        this.correct = correct;
        this.wrong = wrong;
        this.unanswered = unanswered;
        this.grade = grade;
    }

    // --- GETTERS --- //
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

    @Override
    public String toString() {
        return "ExamResult{correct=" + correct +
                ", wrong=" + wrong +
                ", unanswered=" + unanswered +
                ", grade=" + grade + "}";
    }
}
