package model;

/**
 * Excepción para envolver errores derivados de la generación automática
 * de preguntas (por ejemplo, al invocar un servicio externo).
 */
public class QuestionCreatorException extends Exception {

    public QuestionCreatorException(String message) {
        super(message);
    }

    public QuestionCreatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
