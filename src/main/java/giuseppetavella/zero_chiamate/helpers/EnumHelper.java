package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataFormatException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumHelper {

    /**
     * 
     * Try parsing a string into an enum constant, given the enum class.
     * Use this when:
     * - you get a string and want to convert it to the relevant enum constant
     * - want to test whether a string matches exactly an enum constant
     * 
     * @example 
     * <pre>
     *     
     *       UserRole desiredRole = EnumHelper.parseEnum(UserRole.class, body.role());
     *     
     * </pre>
     */
    public static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value) throws InvalidDataFormatException
    {

        try {
            
            return Enum.valueOf(enumClass, value);
            
        } catch (IllegalArgumentException e) {

            throw new InvalidDataFormatException("While casting a string into an "
                                        +"actual enum constant, the value '" 
                                        + value + "' is not valid for enum " + enumClass.getSimpleName());            
        }

    }

    /**
     * List of enum constants -> list of strings
     */
    public static <T extends Enum<T>> List<String> stringify(Class<T> enumClass, 
                                                             List<T> list)
    {
        return list.stream().map(Enum::name).toList();
    }

    /**
     * Map of string - list of enum consts -> map of string - list of strings
     */
    public static <T extends Enum<T>> Map<String, List<String>> stringify(Class<T> enumClass,
                                                                          Map<T, List<T>> map)
    {
        
        Map<String, List<String>> newMap = new HashMap<>();
        
        for (T key : map.keySet()) {
            newMap.put(
                //  assume: not null   
                key.name(),
                // stringify the list
                EnumHelper.stringify(enumClass, map.get(key)) 
            );
        }
        
        return newMap;
        
    }


}
