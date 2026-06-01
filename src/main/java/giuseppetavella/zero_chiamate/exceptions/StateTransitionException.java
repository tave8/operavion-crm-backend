package giuseppetavella.zero_chiamate.exceptions;

public class StateTransitionException extends InvalidDataException {
    public StateTransitionException(String message) {
        super("Error with state transition. DETAILS: " + message);
    }
}
