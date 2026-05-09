package giuseppetavella.demo_login_system.exceptions;

public class IncorrectInternalAPIUsage extends RuntimeException {
    public IncorrectInternalAPIUsage(String message) {
        super("The usage of some internal API or functionality is not allowed or incorrect. DETAILS: " + message);
    }
}
