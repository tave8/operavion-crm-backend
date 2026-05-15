package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.time.LocalTime;

public class DataValidationHelper {
    
    public static void requireValidRange(LocalDate start, LocalDate end) {
        if (!TimeHelper.isValidRange(start, end)) {
            throw new InvalidDataException("'startDate' cannot be after 'endDate'");
        }
    }

    public static void requireValidRange(LocalTime start, LocalTime end) {
        if (!TimeHelper.isValidRange(start, end)) {
            throw new InvalidDataException("'startTime' cannot be after 'endTime'");
        }
    }

    public static void requireValidRange(LocalDate startDate, LocalDate endDate,
                                         LocalTime startTime, LocalTime endTime) {
        requireValidRange(startDate, endDate);
        requireValidRange(startTime, endTime);
    }
}