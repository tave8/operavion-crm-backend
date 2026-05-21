package giuseppetavella.demo_login_system.exceptions;

public class InvalidStateTransitionException extends InvalidDataException {
    public InvalidStateTransitionException(String currentState, 
                                           String desiredState,
                                           String entity) 
    {
        super("Invalid state transition. Cannot transition from "
                +"current state '" + InvalidStateTransitionException.formatState(currentState) 
                + "' to desired state '" + InvalidStateTransitionException.formatState(desiredState) 
                + "' for entity '" + entity+ "'.");
    }
    
    public static String formatState(String state)
    {
        return state == null ? "<no state yet>" : state;
    }

}
