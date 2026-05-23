package giuseppetavella.demo_login_system.infrastructure.storage;

import giuseppetavella.demo_login_system.exceptions.FileUploadException;
import giuseppetavella.demo_login_system.helpers.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

import java.util.UUID;


@Service
public class FileUploadService {

    @Autowired
    private S3Client s3Client;
    
    // this is a dependency
    private final String bucket;

    // this is a dependency
    private final String publicUrl;
    
    // we use constructor dependency injection
    public FileUploadService(
            @Value("${cloudflare.r2.bucket-name}") String bucket,
            @Value("${cloudflare.r2.public-url}") String publicUrl) 
    {
        this.bucket = bucket;
        this.publicUrl = publicUrl;
    }
    
    
    /**
     * Upload a file.
     * Return the file public URL.
     * 
     * @param fileExt the file extension, for example "pdf" (without dot)
     */
    public String upload(byte[] bytes, String fileExt) throws FileUploadException
    {
        // generate a random id
        UUID id = UUID.randomUUID();
        String filename = id + "." + fileExt;
        
        try {
            
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(this.bucket).key(filename).build(),
                    RequestBody.fromBytes(bytes)
            );
            
        } catch(S3Exception ex) {
            throw new FileUploadException(ex.getMessage());
        }
        
        return this.buildFileUrlFrom(filename);
        
    }
    
    
    public String upload(MultipartFile file, String fileExt) 
    {
        return this.upload(FileHelper.getBytes(file), fileExt);
    }
    
    
    // public String upload
    
    
    private String buildFileUrlFrom(String filename) {
        return this.publicUrl + "/" + filename;
    }
    
}
