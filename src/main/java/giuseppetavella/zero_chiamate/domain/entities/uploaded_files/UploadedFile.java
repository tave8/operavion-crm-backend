package giuseppetavella.zero_chiamate.domain.entities.uploaded_files;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue
    private UUID id;

    // the key used to identify the file in Cloudflare R2
    @Column(name = "storage_key", nullable = false, unique = true)
    private String storageKey;

    // the original filename as uploaded by the user, for example "invoice.pdf"
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    protected UploadedFile() {}

    public UploadedFile(String storageKey,
                        String originalFilename,
                        String mimeType) 
    {
    
        this.storageKey = storageKey;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.createdAt = OffsetDateTime.now();
        
    }

    public String getMimeType() {
        return mimeType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getStorageKey() {
        return storageKey;
    }

    
}