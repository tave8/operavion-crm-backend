package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;

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


    public static boolean isValidRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) return true;
        return !start.isAfter(end);
    }

    public static boolean isValidRange(LocalTime start, LocalTime end) {
        if (start == null || end == null) return true;
        return !start.isAfter(end);
    }

    public static boolean isValidRange(LocalDate startDate, LocalDate endDate,
                                       LocalTime startTime, LocalTime endTime) {
        return isValidRange(startDate, endDate) && isValidRange(startTime, endTime);
    }



    /**
     * Returns the Monday of last week.
     */
    public static LocalDate lastMonday() {
        // 1. Get the Monday of THIS week (May 18)
        LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 2. Go back exactly 7 days to get LAST week's Monday (May 11)
        return thisMonday.minusDays(7);
    }

    /**
     * Returns the Friday of last week.
     */
    public static LocalDate lastFriday() {
        // Aligns perfectly with the Monday calculated above (May 15)
        return lastMonday().with(DayOfWeek.FRIDAY);
    }
}