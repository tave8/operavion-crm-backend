package giuseppetavella.zero_chiamate.infrastructure.storage;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
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
     * @param fileType
     * @return
     */
    public String upload(byte[] bytes, String fileType)
    {
        
        return cloudflareR2APIService.upload(bytes, fileType);

    }


    /**
     * Upload a file. Return the unique filename.
     *
     * @param bytes
     * @return
     */
    public String upload(byte[] bytes)
    {
        
        // extract the file extension/type, without dot
        var fileType = FileHelper.getFileType(bytes);
        
        return upload(bytes, fileType);

    }


    /**
     * 
     * 
     * @param file
     * @return
     */
    public String upload(MultipartFile file)
    {
       
        var bytes = FileHelper.getBytes(file);
 
        return upload(bytes);
        
    }


    /**
     * Download a file by its filename.
     * 
     * @return
     */
    public byte[] download(String filename)
    {
        
        return cloudflareR2APIService.download(filename);

    }


    /**
     * Build the file URL, given the filename.
     * 
     * @return
     */
    public String buildUrl(String filename) {
    
        return cloudflareR2APIService.buildFileUrl(filename);
    
    }
    
    
}
