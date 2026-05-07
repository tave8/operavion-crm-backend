package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidUUIDStringException;

import java.util.List;
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

}
