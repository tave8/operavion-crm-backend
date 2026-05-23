package giuseppetavella.zero_chiamate.exceptions;

public class AIException extends RuntimeException {
    public AIException(String message) {
        super("Error while working with AI. DETAILS: " + message);
    }
}
