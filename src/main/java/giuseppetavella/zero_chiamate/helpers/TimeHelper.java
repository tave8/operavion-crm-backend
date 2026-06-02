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
     * Returns true if the input time is older than {@code minutes} minutes ago (i.e. expired).
     */
    public static boolean isExpiredWithin(OffsetDateTime inputTime, long minutes)
    {
        TimeHelper.requireNotNull(inputTime);
        OffsetDateTime now = OffsetDateTime.now();
        return inputTime.plusMinutes(minutes).isBefore(now);
    }

    /**
     * Returns true if the input time is within the last {@code minutes} minutes (i.e. not expired).
     */
    public static boolean isNotExpiredWithin(OffsetDateTime inputTime, long minutes)
    {
        return !TimeHelper.isExpiredWithin(inputTime, minutes);
    }
    

    /**
     * Is the given input in the past?
     * 
     * @param inputTime
     * @return
     */
    public static boolean isPast(OffsetDateTime inputTime) {
        TimeHelper.requireNotNull(inputTime);
        return inputTime.isBefore(OffsetDateTime.now());
    }

    public static boolean isFuture(OffsetDateTime inputTime) {
        TimeHelper.requireNotNull(inputTime);
        return inputTime.isAfter(OffsetDateTime.now());
    }
    
    public static boolean isExpired(OffsetDateTime inputTime) {
        return isPast(inputTime);
    }
    
    public static boolean isNotExpired(OffsetDateTime inputTime)
    {
        return isFuture(inputTime);
    }
    
    
    public static void requireNotNull(OffsetDateTime inputTime) throws InvalidDataException 
    {
        if(inputTime == null) {
            throw new InvalidDataException("Input time cannot be null.");
        }
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