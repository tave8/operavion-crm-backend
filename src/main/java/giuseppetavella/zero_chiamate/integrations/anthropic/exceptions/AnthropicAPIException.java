package giuseppetavella.zero_chiamate.integrations.anthropic.exceptions;

import giuseppetavella.zero_chiamate.infrastructure.ai.exceptions.AIException;

public class AnthropicAPIException extends AIException {
    public AnthropicAPIException(String message) {
        super("Error while working with Anthropic API. DETAILS: " + message);
    }
}
