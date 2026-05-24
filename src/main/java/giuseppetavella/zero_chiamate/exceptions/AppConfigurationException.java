package giuseppetavella.zero_chiamate.exceptions;

public class AppConfigurationException extends RuntimeException {
    public AppConfigurationException(String message) {
        super("Error with some internal configuration. DETAILS: " + message);
    }
}
