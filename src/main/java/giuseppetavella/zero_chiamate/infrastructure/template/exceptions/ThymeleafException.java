package giuseppetavella.zero_chiamate.infrastructure.template.exceptions;

public class ThymeleafException extends TemplateException {
    public ThymeleafException(String message) {
        super("Error while working with Thymeleaf template library. DETAILS: " + message);
    }
}
