package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

public class CronSchedule {

    public static final String EVERY_SECOND = "* * * * * *";
    public static final String EVERY_MINUTE = "0 * * * * *";
    public static final String EVERY_5_MINUTES = "0 */5 * * * *";
    public static final String EVERY_15_MINUTES = "0 */15 * * * *";
    public static final String EVERY_30_MINUTES = "0 */30 * * * *";
    public static final String EVERY_HOUR = "0 0 * * * *";
    public static final String EVERY_6_HOURS = "0 0 */6 * * *";
    public static final String EVERY_12_HOURS = "0 0 */12 * * *";
    public static final String EVERY_DAY_MIDNIGHT = "0 0 0 * * *";
    public static final String EVERY_DAY_NOON = "0 0 12 * * *";
    public static final String EVERY_MONDAY_MIDNIGHT = "0 0 0 * * MON";
    public static final String EVERY_FIRST_OF_MONTH = "0 0 0 1 * *";
    
}