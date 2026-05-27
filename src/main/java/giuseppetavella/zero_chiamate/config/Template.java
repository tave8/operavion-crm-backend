package giuseppetavella.zero_chiamate.config;

public enum Template {
    // emails
    EMAIL_CONTRACT_DISCREPANCY("emails/contract_discrepancy"),
    EMAIL_FORGOT_PASSWORD_AUTHORIZATION("emails/forgot_password_authorization"),
    EMAIL_VERIFY_EMAIL("emails/verify_email"),
    EMAIL_SHIFTS_COUNT_BY_OPERATOR("emails/shifts_count_by_operator"),
    // add more email templates here...
    
    // reports
    REPORT_CONTRACT_DISCREPANCY("reports/contract_discrepancy"),
    REPORT_SHIFTS_COUNT_BY_OPERATOR("reports/shifts_count_by_operator"),
    // add more report templates here...
    
    // emails: dev
    EMAIL_DEV_ERROR("emails/dev/error"),
    EMAIL_DEV_UNSUCCESSFUL_BACKGROUND_JOB("emails/dev/unsuccessful_background_job");

    
    
    private final String value;

    Template(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}