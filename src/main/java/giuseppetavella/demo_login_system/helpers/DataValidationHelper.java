package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.enums.internal.ContractExpectationState;
import giuseppetavella.demo_login_system.exceptions.ContractExpectationException;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidStateTransitionException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DataValidationHelper {

    public static void requireValidRange(LocalDate start, LocalDate end) {
        if (!TimeHelper.isValidRange(start, end)) {
            throw new InvalidDataException(
                    "Invalid date range validation failed: 'startDate' cannot be after 'endDate'. " +
                            "Provided start: [" + start + "], provided end: [" + end + "]"
            );
        }
    }

    public static void requireValidRange(LocalTime start, LocalTime end) {
        if (!TimeHelper.isValidRange(start, end)) {
            throw new InvalidDataException(
                    "Invalid time range validation failed: 'startTime' cannot be after 'endTime'. " +
                            "Provided start: [" + start + "], provided end: [" + end + "]"
            );
        }
    }

    public static void requireValidRange(LocalDate startDate, LocalDate endDate,
                                         LocalTime startTime, LocalTime endTime) {
        requireValidRange(startDate, endDate);
        requireValidRange(startTime, endTime);
    }

    /**
     * Validate that a map contains at least these keys.
     */
    public static void requireMapContainsAtLeastKeys(Map<?, ?> map, List<?> keys) {
        if (keys == null) {
            return;
        }

        if (map == null) {
            throw new InvalidDataException(
                    "The map given is null, but it was required to contain keys: " + keys
            );
        }

        List<Object> missingKeys = new ArrayList<>();

        for (Object key : keys) {
            if (!map.containsKey(key)) {
                missingKeys.add(key);
            }
        }

        if (!missingKeys.isEmpty()) {
            throw new InvalidDataException(
                    "While checking if a map contains at least the given keys, at least a key was missing. "
                            + "Missing keys: " + missingKeys + ". " +
                            "Required keys: " + keys + ". " +
                            "Actual map keys present: " + map.keySet()
            );
        }
        
    }


    /**
     * Validate that a map contains EXACTLY and ONLY the specified keys.
     * Fails if any required key is missing OR if any unexpected key is present.
     */
    public static void requireMapContainsOnlyKeys(Map<?, ?> map, List<?> expectedKeys) {
        if (expectedKeys == null) {
            return;
        }

        if (map == null) {
            throw new InvalidDataException(
                    "The map given is null, but it was expected to contain exactly these keys: " + expectedKeys
            );
        }

        // 1. find missing keys
        List<Object> missingKeys = new ArrayList<>();
        for (Object expectedKey : expectedKeys) {
            if (!map.containsKey(expectedKey)) {
                missingKeys.add(expectedKey);
            }
        }

        // 2. find extra keys
        List<Object> unexpectedKeys = new ArrayList<>();
        Set<?> expectedKeysSet = new HashSet<>(expectedKeys); 
        for (Object actualKey : map.keySet()) {
            if (!expectedKeysSet.contains(actualKey)) {
                unexpectedKeys.add(actualKey);
            }
        }

        // 3. if any discrepancy, throw error
        if (!missingKeys.isEmpty() || !unexpectedKeys.isEmpty()) {
            StringBuilder msg = new StringBuilder("While checking if a map contains exclusively the expected keys, a mismatch was found. ");

            if (!missingKeys.isEmpty()) {
                msg.append("Missing keys: ").append(missingKeys).append(". ");
            }
            if (!unexpectedKeys.isEmpty()) {
                msg.append("Unexpected keys found: ").append(unexpectedKeys).append(". ");
            }

            msg.append("Expected keys: ").append(expectedKeys).append(". ")
                    .append("Actual map keys present: ").append(map.keySet());

            throw new InvalidDataException(msg.toString());
        }
        
    }

    /**
     * Require a valid state transition.
     * 
     * @throws InvalidStateTransitionException
     */
    public static void requireValidStateTransition(String currentState,
                                                   String desiredState,
                                                   List<String> firstStates,
                                                   Map<String, List<String>> stateMap,
                                                   boolean noStateYet,
                                                   String entity) throws InvalidStateTransitionException
    {
        
        boolean isValidTransition = StateTransitionHelper.isValidStateTransition(
                currentState,
                desiredState,
                firstStates,
                stateMap,
                noStateYet
        );
        
        if(!isValidTransition) {
            throw new InvalidStateTransitionException(
                    currentState, 
                    desiredState, 
                    entity
            );  
        }
        
    }


    /**
     * Require a valid state transition.
     *
     * @throws InvalidStateTransitionException
     */
    public static <T extends Enum<T>> void requireValidStateTransition(Class<T> enumClass,
                                                                       T currentState,
                                                                       T desiredState,
                                                                       List<T> firstStates,
                                                                       Map<T, List<T>> stateMap,
                                                                       boolean noStateYet,
                                                                       String entity) throws InvalidStateTransitionException
    {

        DataValidationHelper.requireValidStateTransition(
            currentState == null ? null : currentState.name(),
            desiredState == null ? null : desiredState.name(),
            EnumHelper.stringify(enumClass, firstStates),
            EnumHelper.stringify(enumClass, stateMap),
            noStateYet,
            entity
        );

    }

    

}