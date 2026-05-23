package giuseppetavella.zero_chiamate.exceptions;

import java.util.ArrayList;
import java.util.List;

public class PayloadValidationException extends RuntimeException {
    private List<String> errors = new ArrayList<>();

    public PayloadValidationException(String message) {
        super(message);
    }

    public PayloadValidationException(List<String> errors) {
        super("There's at least one invalid field in your payload.");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}