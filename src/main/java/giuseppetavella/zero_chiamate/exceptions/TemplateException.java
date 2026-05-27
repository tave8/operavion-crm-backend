package giuseppetavella.zero_chiamate.exceptions;

public class TemplateException extends RuntimeException {
    public TemplateException(String message) {
        super("Error while working with / filling a HTML template. DETAILS: " + message);
    }
}
