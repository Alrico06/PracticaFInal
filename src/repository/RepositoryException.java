package repository;

/**
 * Excepción específica para errores de repositorio/persistencia.
 * Se usa para envolver excepciones de IO o de acceso a datos.
 */
public class RepositoryException extends Exception {

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
