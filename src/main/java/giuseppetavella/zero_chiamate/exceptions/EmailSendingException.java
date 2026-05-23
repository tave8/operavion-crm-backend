package giuseppetavella.zero_chiamate.exceptions;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super("Error while sending an email: DETAILS: " + message);
    }
}
