package giuseppetavella.zero_chiamate.infrastructure.ai.exceptions;

public class AIException extends RuntimeException {
    public AIException(String message) {
        super("Error while working with AI. DETAILS: " + message);
    }
}
