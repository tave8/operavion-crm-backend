package giuseppetavella.zero_chiamate.infrastructure.storage;

import giuseppetavella.zero_chiamate.exceptions.FileUploadException;
import giuseppetavella.zero_chiamate.exceptions.InvalidFileUploadedException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
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
