package giuseppetavella.zero_chiamate.domain.business;

import giuseppetavella.zero_chiamate.infrastructure.storage.exceptions.InvalidFileUploadedException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.storage.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Upload images of this app.
 * Define custom rules for each upload, 
 * perform specific checks before upload etc.
 */
@Service
public class AppImageUploadService {

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * Upload avatar image.
     */
    public String uploadAvatar(MultipartFile image)
    {
        // if image is too big
        if(!FileHelper.isWithinAvatarSize(image)) {
            throw new InvalidFileUploadedException(
                    "The file uploaded ("
                    +FileHelper.getFileSizeInMB(image)
                    +"MB) is too big. Max file size is 2MB."
            );
        }

        return imageUploadService.upload(image);
    }
    
    // add here an image upload specific of this app...     

}
