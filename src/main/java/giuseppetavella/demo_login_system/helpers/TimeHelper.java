package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.InvalidDataException;

import java.sql.Time;
import java.time.OffsetDateTime;

public class TimeHelper {

    /**
     * Since {@code input time } have passed at least {@code minutes}
     */
    public static boolean sinceHavePassedAtLeast(OffsetDateTime inputTime, long minutes) throws InvalidDataException
    {
        TimeHelper.requireNotNull(inputTime);
        OffsetDateTime now = OffsetDateTime.now();
        return inputTime.plusMinutes(minutes).isBefore(now);
    }    
    
    /**
     * Returns true if the input time is older than {@code minutes} minutes ago.
     */
    public static boolean isExpiredWithin(OffsetDateTime inputTime, long minutes) throws InvalidDataException 
    {
        TimeHelper.requireNotNull(inputTime);
        return !TimeHelper.isValidWithin(inputTime, minutes);
    }

    /**
     * Returns true if the input time is within the last {@code minutes} minutes.
     */
    public static boolean isValidWithin(OffsetDateTime inputTime, long minutes) throws InvalidDataException
    {
        TimeHelper.requireNotNull(inputTime);
        OffsetDateTime now = OffsetDateTime.now();
        return inputTime.plusMinutes(minutes).isAfter(now); 
    }
    
    
    public static boolean isValid(OffsetDateTime inputTime) {
        TimeHelper.requireNotNull(inputTime);
        return OffsetDateTime.now().isAfter(inputTime);
    }

    
    public static boolean isExpired(OffsetDateTime inputTime) throws InvalidDataException
    {
        TimeHelper.requireNotNull(inputTime);
        return !TimeHelper.isValid(inputTime);
    }
    
    
    public static void requireNotNull(OffsetDateTime inputTime) throws InvalidDataException 
    {
        if(inputTime == null) {
            throw new InvalidDataException("Input time cannot be null.");
        }
    }

    /**
     * Is the input timestamp now or in the future?
     * 
     * @param inputTime
     * @return
     */
    public static boolean isNowOrFuture(OffsetDateTime inputTime) {

        TimeHelper.requireNotNull(inputTime);
        
        OffsetDateTime now = OffsetDateTime.now();

        boolean isNow = inputTime.equals(now);
        boolean isFuture = inputTime.isAfter(now);
        
        return isNow || isFuture;
    }
    
}