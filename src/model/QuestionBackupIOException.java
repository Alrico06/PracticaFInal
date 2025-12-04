package model;

/**
 * Excepción específica para operaciones de importación/exportación de backups.
 */
public class QuestionBackupIOException extends Exception {

    public QuestionBackupIOException(String message) {
        super(message);
    }

    public QuestionBackupIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
