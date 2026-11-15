package id.ac.pens.psdku.campuslosthub.exception;

/**
 * Exception yang dilempar ketika pengajuan klaim, verifikasi bukti,
 * atau perubahan status klaim tidak valid.
 */
public class InvalidClaimException extends Exception {

    public InvalidClaimException(String message) {
        super(message);
    }

    public InvalidClaimException(String message, Throwable cause) {
        super(message, cause);
    }
}
