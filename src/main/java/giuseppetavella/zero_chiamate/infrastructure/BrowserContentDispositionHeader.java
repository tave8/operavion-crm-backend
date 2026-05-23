package giuseppetavella.zero_chiamate.infrastructure;

public enum BrowserContentDispositionHeader {
    ATTACHMENT, INLINE;

    public String getValue() {
        return this.name().toLowerCase();
    }
}
