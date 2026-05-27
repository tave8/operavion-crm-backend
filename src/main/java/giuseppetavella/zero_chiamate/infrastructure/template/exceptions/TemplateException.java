package giuseppetavella.zero_chiamate.infrastructure.template.exceptions;

public class TemplateException extends RuntimeException {
    public TemplateException(String message) {
        super("Error while working with / filling a HTML template. DETAILS: " + message);
    }
}
