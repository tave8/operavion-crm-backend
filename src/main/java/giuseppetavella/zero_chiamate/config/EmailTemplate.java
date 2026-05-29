package giuseppetavella.zero_chiamate.config;

public enum EmailTemplate implements Template {
    // emails
    CONTRACT_DISCREPANCY("emails/contract_discrepancy"),
    FORGOT_PASSWORD_AUTHORIZATION("emails/forgot_password_authorization"),
    VERIFY_EMAIL("emails/verify_email"),
    SHIFTS_COUNT_BY_OPERATOR("emails/shifts_count_by_operator"),
    // add more email templates here...
    
    // emails: dev
    DEV_ERROR("emails/dev/error"),
    DEV_UNSUCCESSFUL_BACKGROUND_JOB("emails/dev/unsuccessful_background_job");
    
    
    private final String value;

    EmailTemplate(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}