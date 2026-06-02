package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUUIDStringException;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class StringHelper {
    
    
    /**
     * Parse a string to UUID.
     *
     * @throws InvalidUUIDStringException if the string is an invalid UUID or null
     * @return a valid UUID 
     */
    public static UUID parseUUID(String itemIdAsStr) throws InvalidUUIDStringException 
    {
        if (itemIdAsStr == null) {
            throw new InvalidUUIDStringException("<ID is null>");
        }
        
        try {
            return UUID.fromString(itemIdAsStr);
            
        } catch(IllegalArgumentException ex) {
            
            throw new InvalidUUIDStringException(itemIdAsStr);
            
        }
    }


    /**
     * Require a string to match one of given strings. 
     * 
     * @throws InvalidDataException if the input string does not match 
     *   any of the given match strings
     */
    @Deprecated
    public static void requireInValues(String input, 
                                       List<String> matches, 
                                       String varName) throws InvalidDataException
    {
        
        ValidationHelper.requireInValues(
                input,
                matches,
                varName
        );
        
    }



    
    public static boolean isValidEmail(String email) {
        if(email == null) {
            throw new InvalidDataException("While validating if email is valid, email cannot be null.");
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(emailRegex);
    }


    /**
     * Generate a temporary human-friendly password.
     * Only uppercase letters and numbers, 5-7 characters long.
     *
     * @example "A3K9F", "BX72Q4", "T4KW29R"
     */
    public static String generatePassword() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        final int MIN_LENGTH = 5;
        final int MAX_LENGTH = 7;

        Random random = new Random();
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }


    /**
     * If query is not empty (contains something), return a search pattern.
     * 
     * If query is empty or blank or null, return null.
     * 
     * <pre>
     * Example:
     *      null    ->   null
     *      "  "    ->   null
     *      "abc "  ->   "%abc%"
     *      " ABC " ->   "%abc%"
     * </pre>
     * 
     * @return
     */
    public static String buildSearchQueryPattern(String query) {
        
        if(query == null) {
            return null;
        }
        
        if(query.trim().isEmpty()) {
            return null;
        }

        String queryCleaned = query.toLowerCase().trim();
        
        return "%" + queryCleaned + "%";

    }
    
}
