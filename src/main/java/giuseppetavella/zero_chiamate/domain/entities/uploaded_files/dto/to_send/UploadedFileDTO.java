package giuseppetavella.zero_chiamate.domain.entities.uploaded_files.dto.to_send;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UploadedFileDTO(
        
        // the id of the uploaded file, in DB
        UUID id,
        
        String mimeType,
        
        String originalFilename,
        
        OffsetDateTime createdAt
        
) {
}
