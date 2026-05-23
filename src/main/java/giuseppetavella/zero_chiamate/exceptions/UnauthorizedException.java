package giuseppetavella.zero_chiamate.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super("You are not authenticated or not authorized to access this resource: DETAILS: " + message);
    }
}