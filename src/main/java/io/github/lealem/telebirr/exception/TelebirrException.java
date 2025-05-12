package io.github.lealem.telebirr.exception;

/**
 * Indicates a failure in Telebirr SDK operations.
 */
public class TelebirrException extends Exception {
  public TelebirrException(String message) {
    super(message);
  }
  public TelebirrException(String message, Throwable cause) {
    super(message, cause);
  }
}