package giuseppetavella.zero_chiamate.exceptions;

public class AppStartupException extends RuntimeException {
    public AppStartupException(String message) {
        super("Error during app startup. DETAILS: " + message);
    }
}
