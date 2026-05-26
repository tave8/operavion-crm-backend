package giuseppetavella.zero_chiamate.unit.state_transition;

import giuseppetavella.zero_chiamate.exceptions.InvalidStateTransitionException;
import giuseppetavella.zero_chiamate.helpers.StateTransitionHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StripeAPIStateTransitionTest {

    // state map for testing
    private final List<String> firstStates = List.of("INCOMPLETE");

    private final Map<String, List<String>> stateMap = Map.of(
            "INCOMPLETE", List.of("TRIALING", "ACTIVE"),
            "TRIALING",   List.of("ACTIVE", "PAST_DUE"),
            "ACTIVE",     List.of("PAST_DUE", "CANCELED"),
            "PAST_DUE",   List.of("ACTIVE", "CANCELED"),
            "CANCELED",   List.of()
    );

    // -------------------------
    // VALID TRANSITIONS
    // -------------------------

    @Test
    void noState_toIncomplete_isValid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                null, "INCOMPLETE", firstStates, stateMap, true
        );
        assertTrue(result);
    }

    @Test
    void incomplete_toTrialing_isValid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                "INCOMPLETE", "TRIALING", firstStates, stateMap, false
        );
        assertTrue(result);
    }

    @Test
    void trialing_toActive_isValid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                "TRIALING", "ACTIVE", firstStates, stateMap, false
        );
        assertTrue(result);
    }

    @Test
    void pastDue_toActive_isValid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                "PAST_DUE", "ACTIVE", firstStates, stateMap, false
        );
        assertTrue(result);
    }

    // -------------------------
    // INVALID TRANSITIONS
    // -------------------------

    @Test
    void canceled_toAnything_isInvalid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                "CANCELED", "ACTIVE", firstStates, stateMap, false
        );
        assertFalse(result);
    }

    @Test
    void incomplete_toCanceled_isInvalid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                "INCOMPLETE", "CANCELED", firstStates, stateMap, false
        );
        assertFalse(result);
    }

    @Test
    void noState_toActive_isInvalid() {
        boolean result = StateTransitionHelper.isValidStateTransition(
                null, "ACTIVE", firstStates, stateMap, true
        );
        assertFalse(result);
    }

    // -------------------------
    // EXCEPTIONS
    // -------------------------

    @Test
    void desiredState_isNull_throwsException() {
        assertThrows(InvalidStateTransitionException.class, () ->
                StateTransitionHelper.isValidStateTransition(
                        "INCOMPLETE", null, firstStates, stateMap, false
                )
        );
    }

    @Test
    void currentState_isNull_butNotDeclaredAsNoState_throwsException() {
        assertThrows(InvalidStateTransitionException.class, () ->
                StateTransitionHelper.isValidStateTransition(
                        null, "ACTIVE", firstStates, stateMap, false
                )
        );
    }

    @Test
    void unknownCurrentState_throwsException() {
        assertThrows(InvalidStateTransitionException.class, () ->
                StateTransitionHelper.isValidStateTransition(
                        "UNKNOWN_STATE", "ACTIVE", firstStates, stateMap, false
                )
        );
    }
}
