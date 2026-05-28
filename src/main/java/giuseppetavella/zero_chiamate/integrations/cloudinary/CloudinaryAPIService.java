package giuseppetavella.zero_chiamate.integrations.cloudinary;

import com.cloudinary.Cloudinary;
import giuseppetavella.zero_chiamate.infrastructure.storage.exceptions.InvalidFileUploadedException;
import giuseppetavella.zero_chiamate.integrations.cloudinary.exceptions.CloudinaryAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryAPIService {

    @Autowired
    private Cloudinary cloudinaryUploader;


    /**
     * Upload to Cloudfinary and return the URL.
     */
    public String upload(byte[] bytes)
    {

        // if there are no bytes = the file is empty
        if (bytes.length == 0) {
            throw new InvalidFileUploadedException("The byte array uploaded cannot be empty. The file is empty?");
        }

        try {
            Map result = cloudinaryUploader
                    .uploader()
                    .upload(bytes, Map.of());

            return (String) result.get("secure_url");

        } catch (IOException | RuntimeException ex) {
            
            throw new CloudinaryAPIException(ex.getMessage());
        
        }
    }
    
}
