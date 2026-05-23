package giuseppetavella.zero_chiamate.infrastructure.workers.dto.to_send;

import java.time.OffsetDateTime;

/**
 * Use this payload when the client requests 
 * an operation that involves async/worker/background job operations
 */
public class BackgroundJobAcceptedDTO {
    
    private final String message;
    private final OffsetDateTime timestamp;
    
    public BackgroundJobAcceptedDTO(String message) {
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
