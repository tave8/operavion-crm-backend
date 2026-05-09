package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.InvalidDataFormatException;

public class EnumHelper {

    /**
     * Parse a string into its enum, if you can.
     * 
     * @example 
     * <pre>
     *     
     *     UserRole desiredRole = EnumHelper.parseEnum(UserRole.class);
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


}
