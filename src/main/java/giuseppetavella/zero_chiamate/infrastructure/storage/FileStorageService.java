package giuseppetavella.zero_chiamate.infrastructure.storage;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.storage.dto.FileUploadResult;
import giuseppetavella.zero_chiamate.integrations.cloudflare_r2.CloudflareR2APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    
    @Autowired
    private CloudflareR2APIService cloudflareR2APIService;
    
    
    /**
     * Upload a file. Return the unique filename.
     * 
     * @param bytes
     * @return
     */
    public FileUploadResult upload(byte[] bytes, String originalFilename)
    {
        
        var fileType = FileHelper.getFileType(bytes, originalFilename);
        
        var filename = cloudflareR2APIService.upload(bytes, fileType);

        return new FileUploadResult(
                filename,
                buildUrl(filename)
        );
        
    }
    

    /**
     * 
     * 
     * @param file
     * @return
     */
    public FileUploadResult upload(MultipartFile file)
    {
       
        var bytes = FileHelper.getBytes(file);
 
        return upload(bytes, file.getOriginalFilename());
        
    }


    /**
     * Download a file by its filename.
     * 
     * @return
     */
    public byte[] download(String storageKey)
    {
        
        return cloudflareR2APIService.download(storageKey);

    }


    /**
     * Build the file URL, given the filename.
     * 
     * @return
     */
    public String buildUrl(String storageKey) {
    
        return cloudflareR2APIService.buildFileUrl(storageKey);
    
    }
    
    
}
