package giuseppetavella.zero_chiamate.integrations.cloudflare_r2;

import giuseppetavella.zero_chiamate.exceptions.FileNotFoundException;
import giuseppetavella.zero_chiamate.infrastructure.storage.exceptions.FileUploadException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.exceptions.CloudflareR2APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

import java.util.UUID;


@Service
public class CloudflareR2APIService {

    @Autowired
    private S3Client s3Client;
    
    // this is a dependency
    private final String bucket;

    // this is a dependency
    private final String publicUrl;
    
    // we use constructor dependency injection
    public CloudflareR2APIService(
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
     * @param fileExtNoDot the file extension, for example "pdf" (without dot)
     */
    public String upload(byte[] bytes, String fileExtNoDot)
    {
        
        var filename = generateFilename(fileExtNoDot);
        
        try {
            
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucket).key(filename).build(),
                    RequestBody.fromBytes(bytes)
            );
            
        } catch(S3Exception ex) {
            
            throw new CloudflareR2APIException(ex.getMessage());
        }
        
        // return the public URL of this file
        return buildFileUrlFrom(filename);
        
    }


    /**
     * Download a file by its filename.
     * Returns the file bytes.
     */
    public byte[] download(String filename) {
        try {
          
            return s3Client.getObjectAsBytes(
                    GetObjectRequest.builder().bucket(bucket).key(filename).build()
            ).asByteArray();
            
        } catch (NoSuchKeyException ex) {
          
            throw new FileNotFoundException(filename);
        
        } catch (S3Exception ex) {
            throw new CloudflareR2APIException(ex.getMessage());
        }
    }
    
    
    private String generateFilename(String fileExtNoDot) {
        // generate a random id
        var id = UUID.randomUUID();
        var filename = id + "." + fileExtNoDot;
        return filename;
    }
    
    private String buildFileUrlFrom(String filename) {
        return publicUrl + "/" + filename;
    }
    
}
