package giuseppetavella.demo_login_system.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super("Some input or data is not valid, or the format is not as expected. DETAILS: " + message);
    }
}
