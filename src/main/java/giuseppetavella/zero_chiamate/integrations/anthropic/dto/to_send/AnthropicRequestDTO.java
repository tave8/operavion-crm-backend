package giuseppetavella.zero_chiamate.integrations.anthropic.dto.to_send;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AnthropicRequestDTO(
        String model,
        @JsonProperty("max_tokens") int maxTokens,
        List<AnthropicMessageToSendDTO> messages
) {}
