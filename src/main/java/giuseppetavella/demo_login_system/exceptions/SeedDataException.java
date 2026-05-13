package giuseppetavella.demo_login_system.exceptions;

public class SeedDataException extends RuntimeException {
    public SeedDataException(String message) {
        super("Error while seeding data. DETAILS: " + message);
    }
}
