package xyz.ibudai.dailyword.basic.exception;

/**
 * The type Socket exception.
 */
public class SocketException extends RuntimeException {

    /**
     * Instantiates a new Socket exception.
     *
     * @param message the message
     */
    public SocketException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Socket exception.
     *
     * @param cause the cause
     */
    public SocketException(Throwable cause) {
        super(cause);
    }
}
