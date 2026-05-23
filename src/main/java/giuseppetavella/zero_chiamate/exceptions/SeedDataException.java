package giuseppetavella.zero_chiamate.exceptions;

public class SeedDataException extends RuntimeException {
    public SeedDataException(String message) {
        super("Error while seeding data. DETAILS: " + message);
    }
}
