package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;

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
    public static void requireInValues(String input, 
                                       List<String> matches, 
                                       String varName) throws InvalidDataException
    {
        
        for(String match : matches) {
            if(input.equals(match)) {
                return;
            }
        }
        
        throw new InvalidDataException("While validating if a string's value matches " 
                                        +"any of potential values for variable '" + varName + "', "
                                        +"no matching string was found. "
                                        + "Input value '" + input + "'. Possible matches: " + String.join(",", matches));
    }


    /**
     * Require valid email.
     * 
     * @param email
     * @throws InvalidDataException
     */
    public static void requireValidEmail(String email) throws InvalidDataException {
        
        if (email == null) {
            throw new InvalidDataException("While validating if a string's value is a valid email, "
                    + "the email is null");
        }
        
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (!email.matches(emailRegex)) {
            throw new InvalidDataException("While validating if a string's value is a valid email, "
                    + "the format was not recognized. "
                    + "Input value '" + email + "' does not match a valid email pattern.");
        }
        
    }
    
    public static void requireValidEmailElseThrowWith(String email, String message) 
    {
        
        try {
            
            StringHelper.requireValidEmail(email);
            
        } catch (InvalidDataException e) {
            throw new InvalidDataException(message);
        }
        
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

}
