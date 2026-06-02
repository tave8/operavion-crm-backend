package giuseppetavella.zero_chiamate.infrastructure.template.exceptions;

public class ThymeleafAPIException extends TemplateException {
    public ThymeleafAPIException(String message) {
        super("Error while working with Thymeleaf template library. DETAILS: " + message);
    }
}
