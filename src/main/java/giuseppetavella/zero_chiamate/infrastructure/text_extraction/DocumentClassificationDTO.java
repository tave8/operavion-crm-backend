package giuseppetavella.zero_chiamate.infrastructure.text_extraction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 

 */
public record DocumentClassificationDTO(
            @JsonProperty("isExpectedTopic") boolean isExpectedTopic,
            
            @JsonProperty("whatIfNotExpectedTopic") String whatIfNotExpectedTopic
    ) {}