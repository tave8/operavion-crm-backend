package giuseppetavella.zero_chiamate.exceptions;

public class IllegalStateTransitionException extends StateTransitionException {
    
    public IllegalStateTransitionException(String msg)
    {
        super("Illegal state transition. DETAILS: " + msg);
    }
    

}
