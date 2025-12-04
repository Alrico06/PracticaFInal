package model;

/**
 * Representa una opción de respuesta en una pregunta.
 * Contiene texto, justificación (rationale) y si es correcta o no.
 */
public class Option {

    private String text;
    private String rationale;
    private boolean correct;

    /** Constructor completo */
    public Option(String text, String rationale, boolean correct) {
        this.text = text;
        this.rationale = rationale;
        this.correct = correct;
    }

    // --- GETTERS & SETTERS --- //

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Option{text='" + text + "', correct=" + correct + "}";
    }
}
