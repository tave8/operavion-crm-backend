package giuseppetavella.zero_chiamate.integrations.anthropic.exceptions;

public class AnthropicAPIException extends RuntimeException {
    public AnthropicAPIException(String message) {
        super("Error while working with Anthropic API. DETAILS: " + message);
    }
}
