package giuseppetavella.demo_login_system.infrastructure.storage;

import com.cloudinary.Cloudinary;
import giuseppetavella.demo_login_system.exceptions.FileUploadException;
import giuseppetavella.demo_login_system.exceptions.InvalidFileUploadedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class MediaUploadService {


    @Autowired
    private Cloudinary cloudinaryUploader;


    /**
     * Upload bytes to the file upload cloud provider.
     */
    public String upload(byte[] bytes) throws FileUploadException, 
                                              InvalidFileUploadedException
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
            throw new FileUploadException(ex.getMessage());
        }
    }



    /**
     * Upload a file.
     *
     * @return the URL of the uploaded file
     */
    public String uploadFile(MultipartFile inputFile) throws InvalidFileUploadedException, 
                                                             FileUploadException 
    {
        
        // if file is empty
        if (inputFile.isEmpty()) {
            throw new InvalidFileUploadedException("The inputFile uploaded cannot be empty.");
        }

        try {
            
            return this.upload(inputFile.getBytes());
            
        } catch (IOException ex) {
            throw new FileUploadException(ex.getMessage());
        }

    }
    


}
