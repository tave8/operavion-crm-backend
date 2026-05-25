package giuseppetavella.zero_chiamate.config;

public enum FrontendShortcutRoute {

    DASHBOARD("dashboard"),
    EMAIL_VERIFICATION_SUCCESS("emailverification.success"),
    EMAIL_VERIFICATION_INVALID("emailverification.invalid");

    private final String value;

    FrontendShortcutRoute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}