package giuseppetavella.zero_chiamate.integrations.anthropic.dto.sent;

import java.util.List;

public record AnthropicResponseDTO(
        List<AnthropicContentSentDTO> content
) {}