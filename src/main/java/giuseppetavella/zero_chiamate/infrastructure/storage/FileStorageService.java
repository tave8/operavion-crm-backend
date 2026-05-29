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
    

    public String upload(byte[] bytes)
    {
        
        // extract the file extension/type, without dot
        var fileType = FileHelper.getFileType(bytes);
        
        return cloudflareR2APIService.upload(bytes, fileType);


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
    
    
    
    
}
