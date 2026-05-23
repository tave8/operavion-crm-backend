package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.InvalidStateTransitionException;

import java.util.List;
import java.util.Map;

public class StateTransitionHelper {


    /**
     * Is the state transition valid?
     *
     */
    public static boolean isValidStateTransition(String currentState,
                                                 String desiredState,
                                                 List<String> firstStates,
                                                 Map<String, List<String>> stateMap,
                                                 boolean noStateYet)
    {

        // desired state cannot be null, regardless
        if(desiredState == null) {
            throw new InvalidStateTransitionException(
                "desiredState can never be null."
            );
        }

        // current state cannot be null and existing  
        if(currentState == null && !noStateYet) {
            throw new InvalidStateTransitionException(
                "currentState cannot be null (actual) and at the same time existing (declared)."
            );
        }
        
        // if there's no state yet
        if(noStateYet) {
            return firstStates.contains(desiredState);
        }
        
        // if there's a current state
        return stateMap.get(currentState).contains(desiredState);

    }


    /**
     * Is the state transition valid?
     *
     */
    public static <T extends Enum<T>> boolean isValidStateTransition(Class<T> enumClass,
                                                                     T currentState,
                                                                     T desiredState,
                                                                     List<T> firstStates,
                                                                     Map<T, List<T>> stateMap,
                                                                     boolean noStateYet)
    {

        return StateTransitionHelper.isValidStateTransition(
                currentState == null ? null : currentState.name(),
                desiredState == null ? null : desiredState.name(),
                EnumHelper.stringify(enumClass, firstStates),
                EnumHelper.stringify(enumClass, stateMap),
                noStateYet
        );

    }


}
