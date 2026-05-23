package giuseppetavella.demo_login_system.infrastructure.storage;

import giuseppetavella.demo_login_system.exceptions.FileUploadException;
import giuseppetavella.demo_login_system.exceptions.InvalidFileUploadedException;
import giuseppetavella.demo_login_system.helpers.FileHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService extends MediaUploadService {


    /**
     * Upload an image.
     * 
     * @return URL of the uploaded image
     */
    public String uploadImage(MultipartFile image)  throws InvalidFileUploadedException,
                                                            FileUploadException
    {
        // if file is not an image
        if(!FileHelper.isImage(image)) {
            throw new InvalidFileUploadedException("The file uploaded is not an image.");
        }
        
        return this.uploadFile(image);
    }

    /**
     * Upload avatar image.
     */
    public String uploadAvatarImage(MultipartFile image) throws InvalidFileUploadedException,
                                                                FileUploadException
    {
        // if image is too big
        if(!FileHelper.isWithinAvatarSize(image)) {
            throw new InvalidFileUploadedException("The file uploaded ("
                                                    +FileHelper.getFileSizeInMB(image)
                                                    +"MB) is too big. Max file size is 2MB.");
        }

        return this.uploadImage(image);
    }
    
}
