package giuseppetavella.demo_login_system.exceptions;

public class ContractExpectationException extends RuntimeException {
    public ContractExpectationException(String message) {
        super("Error while working with a contract expectations. DETAILS: " + message);
    }
}
