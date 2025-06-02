package io.github.lealem.telebirr.exception;

/**
 * Exception thrown when signature generation or verification fails.
 */
public class SignatureException extends TelebirrException {
    public SignatureException(String message) {
        super(message);
    }
    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}