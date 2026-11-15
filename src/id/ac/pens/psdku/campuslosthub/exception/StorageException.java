package id.ac.pens.psdku.campuslosthub.exception;

/**
 * Exception yang dilempar ketika terjadi kegagalan membaca atau menulis
 * data persisten (file CSV).
 */
public class StorageException extends Exception {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
