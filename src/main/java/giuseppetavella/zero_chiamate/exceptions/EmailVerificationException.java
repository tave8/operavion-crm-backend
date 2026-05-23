package giuseppetavella.zero_chiamate.exceptions;

public class EmailVerificationException extends RuntimeException {
    public EmailVerificationException(String message) {
        super("Error regarding account email verification. DETAILS: " + message);
    }
}
