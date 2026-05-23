package giuseppetavella.demo_login_system.infrastructure;

public enum BrowserContentDispositionHeader {
    ATTACHMENT, INLINE;

    public String getValue() {
        return this.name().toLowerCase();
    }
}
