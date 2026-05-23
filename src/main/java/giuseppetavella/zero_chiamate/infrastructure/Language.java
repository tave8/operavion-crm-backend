package giuseppetavella.zero_chiamate.infrastructure;

public enum Language {
    IT("it"),
    EN("en");  

    private final String value;

    /**
     * Usage:
     * 
     * 
     * Language.EN.getValue(); --> "en"
     * 
     * @param value
     */
    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
