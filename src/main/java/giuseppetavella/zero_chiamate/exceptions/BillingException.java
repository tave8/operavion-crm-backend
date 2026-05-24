package giuseppetavella.zero_chiamate.exceptions;

public class BillingException extends RuntimeException {
    public BillingException(String message) {
        super("An error occurred while working with billing. DETAILS: " + message);
    }
}
