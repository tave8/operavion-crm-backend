package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.InvalidStateTransitionException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUrlException;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Supplier;

/**
 * Helper class for all things validation.
 * Ideally, the methods should:
 * <pre>
 * - be static
 * - return void
 * - throw one exception only, InvalidDataException
 * - give possibility to throw a custom exception through a callback
 * - start with "requireValid" for example
 *  "requireValidRange". Or "requireObjectContains" for example
 *  "requireMapContains" or "requireListContains"
 * - thus, one method is a "requireValidSomething" method, 
 *   the other method is a "requireValidSomethingElseThrow" method 
 *   
 *   
 *   METHOD SUFFIX  |  WHAT IT DOES
 *   ------------------------------
 *   orElseThrow       throw a custom exception
 *   orElseThrowWith   throw the default exception (InvalidDataException)
 *                     with a custom message
 *   
 *  </pre>
 *  
 * This is not guaranteed; In the process of standardization.
 */
public class ValidationHelper {

    
    public static void requireFileImage(MultipartFile file)
    {
        if(!FileHelper.isImage(file)) {
            throw new InvalidDataException("File with original filaname '"+file.getOriginalFilename()+"' is not an image");
        }
    }
    
    
    public static void requireFileImageElseThrow(MultipartFile file,
                                                 Supplier<? extends RuntimeException> supplier)
    {
        try {
            ValidationHelper.requireFileImage(file);
        } catch (InvalidDataException ex) {
            throw supplier.get();
        }
    }
    

    /**
     * Require that html template exists.
     */
    public static void requireTemplateExists(String templatePathWithoutExt)
    {
        if(!FileSystemHelper.templateExists(templatePathWithoutExt)) {
            throw new InvalidDataException("Template '" +  templatePathWithoutExt + "' does not exist.");
        }
    }
    
    public static void requireTemplateExistsElseThrow(String templatePathWithoutExt,
                                                      Supplier<? extends RuntimeException> exceptionSupplier)
    {
        try {
            requireTemplateExists(templatePathWithoutExt);
        } catch(InvalidDataException e) {
            throw exceptionSupplier.get();
        }
    }
    
    
    public static void requireStringNotBlank(@Nullable String s) throws InvalidDataException 
    {
        if(s == null) {
            throw new InvalidDataException(
                    "String was required to be not blank, it's null instead."
            );
        }
        if(s.isBlank()) {
            throw new InvalidDataException(
                    "String was required to be not blank, but it's blank. "
                    + "Value: " + s
            );
        }
    }
    
    public static void requireStringNotBlankElseThrow(@Nullable String s,
                                                     Supplier<? extends RuntimeException> exceptionSupplier) 
    {
        try {
            ValidationHelper.requireStringNotBlank(s);
        } catch (InvalidDataException e) {
            throw exceptionSupplier.get();
        }
    }

    public static void requireStringNotBlankElseThrowWith(@Nullable String s,
                                                        String customMsg)
    {
        try {
            ValidationHelper.requireStringNotBlank(s);
        } catch (InvalidDataException e) {
            throw new InvalidDataException(customMsg);
        }
    }


    /**
     * Require valid email.
     *
     * @param email
     * @throws InvalidDataException
     */
    public static void requireValidEmail(@Nullable String email) throws InvalidDataException 
    {
        if (email == null) {
            throw new InvalidDataException("While validating if a string's value is a valid email, "
                                            + "the email is null");
        }
        if(StringHelper.isValidEmail(email)) {
            throw new InvalidDataException("While validating if a string's value is a valid email, "
                                            + "the format was not recognized. "
                                            + "Input value '" + email + "' does not match a valid email pattern.");
        }
    }

    
    public static void requireValidEmailElseThrow(@Nullable String email, 
                                                  Supplier<? extends RuntimeException> exceptionSupplier)
    {
        try {
            ValidationHelper.requireValidEmail(email);
        } catch (InvalidDataException e) {
            throw exceptionSupplier.get();
        }

    }

    
    public static void requireValidEmailElseThrowWith(@Nullable String email,
                                                      String customErrorMsg)
    {
        try {
            ValidationHelper.requireValidEmail(email);
        } catch (InvalidDataException e) {
            throw new InvalidDataException(customErrorMsg);
        }

    }
    
    
    

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

        ValidationHelper.requireValidStateTransition(
            currentState == null ? null : currentState.name(),
            desiredState == null ? null : desiredState.name(),
            EnumHelper.stringify(enumClass, firstStates),
            EnumHelper.stringify(enumClass, stateMap),
            noStateYet,
            entity
        );

    }

    /**
     * Require a valid URL.
     */
    public static void requireValidUrl(String url)
    {
        
        if(!UrlHelper.isValidUrl(url)) {
            throw new InvalidUrlException(url);
        }
        
    }

}