package id.ac.pens.psdku.campuslosthub.exception;

/**
 * Exception yang dilempar ketika barang dengan ID tertentu tidak ditemukan
 * pada daftar inventaris CampusLostHub.
 */
public class ItemNotFoundException extends Exception {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
