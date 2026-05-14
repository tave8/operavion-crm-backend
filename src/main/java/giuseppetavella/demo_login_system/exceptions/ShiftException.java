package giuseppetavella.demo_login_system.exceptions;

public class ShiftException extends RuntimeException {
    public ShiftException(String message) {
        super("An error occurred while working with a shift. DETAILS: " + message);
    }
}
