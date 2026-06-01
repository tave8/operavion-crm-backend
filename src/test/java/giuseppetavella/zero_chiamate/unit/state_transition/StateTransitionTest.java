package giuseppetavella.zero_chiamate.unit.state_transition;

import giuseppetavella.zero_chiamate.exceptions.IllegalStateTransitionException;
import giuseppetavella.zero_chiamate.exceptions.InvalidStateTransitionException;
import giuseppetavella.zero_chiamate.helpers.StateTransitionHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StateTransitionTest {

    /**
     * <pre>
     *   Current state: null
     *   Desired state: A
     *   Valid: yes
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      null     ->      A
     * </pre>
     * 
     * 
     */
    @Test
    public void noStateYet() {
        
        String currentState = null;
        String desiredState = "A";
        
        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(

        );
        
        var isValidTransition = StateTransitionHelper.isValidStateTransition(
            currentState,
            desiredState,
            firstStates,
            stateMap,
            currentState == null    
        );
        
        assertTrue(isValidTransition);
        
    }



    /**
     * <pre>
     *   Current state: A
     *   Desired state: B
     *   Valid: yes
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      A     ->         B
     * </pre>
     *
     *
     */
    @Test
    public void hasFirstState() {

        String currentState = "A";
        String desiredState = "B";

        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(
                "A", List.of(
                        "B"
                )
        );

        var isValidTransition = StateTransitionHelper.isValidStateTransition(
                currentState,
                desiredState,
                firstStates,
                stateMap,
                currentState == null
        );

        assertTrue(isValidTransition);

    }


    /**
     * <pre>
     *   Current state: A
     *   Desired state: B
     *   Valid: yes
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      A     ->         B, C
     * </pre>
     *
     *
     */
    @Test
    public void hasFirstStateWithOption() {

        String currentState = "A";
        String desiredState = "B";

        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(
                "A", List.of(
                        "B", 
                        "C"
                )
        );

        var isValidTransition = StateTransitionHelper.isValidStateTransition(
                currentState,
                desiredState,
                firstStates,
                stateMap,
                currentState == null
        );

        assertTrue(isValidTransition);

    }

    /**
     * <pre>
     *   Current state: null
     *   Desired state: B
     *   Valid: no
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      A     ->         B
     * </pre>
     *
     *
     */
    @Test
    public void hasInvalidFirstState() {

        String currentState = null;
        String desiredState = "B";

        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(
                "A", List.of(
                        "B"
                )
        );

        var isValidTransition = StateTransitionHelper.isValidStateTransition(
                currentState,
                desiredState,
                firstStates,
                stateMap,
                currentState == null
        );

        assertFalse(isValidTransition);

    }
    
    
    /**
     * <pre>
     *   Current state: A
     *   Desired state: C
     *   Valid: no
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      A     ->         B
     * </pre>
     *
     *
     */
    @Test
    public void hasInvalidDesiredState() {

        String currentState = "A";
        String desiredState = "C";

        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(
                "A", List.of(
                        "B"
                )
        );

        var isValidTransition = StateTransitionHelper.isValidStateTransition(
                currentState,
                desiredState,
                firstStates,
                stateMap,
                currentState == null
        );

        assertFalse(isValidTransition);

    }




    /**
     * <pre>
     *   Current state: A
     *   Desired state: null
     *   Valid: no
     * </pre>
     *
     * <pre>
     *   CURRENT            NEXT
     *   ----------------------------
     *      A     ->         B
     * </pre>
     *
     *
     */
    @Test
    public void desiredStateCannotBeNull() {

        String currentState = "A";
        String desiredState = null;

        List<String> firstStates = List.of(
                "A"
        );

        Map<String, List<String>> stateMap = Map.of(
                "A", List.of(
                        "B"
                )
        );
        
        assertThrows(IllegalStateTransitionException.class, () -> {
            StateTransitionHelper.isValidStateTransition(
                    currentState,
                    desiredState,
                    firstStates,
                    stateMap,
                    currentState == null
            );
        });

    }
    
}
