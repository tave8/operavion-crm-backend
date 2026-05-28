package giuseppetavella.zero_chiamate.infrastructure.geocoding;

public enum GeocodingLanguage {
    EN("en"),
    IT("it");

    private final String value;

    GeocodingLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
