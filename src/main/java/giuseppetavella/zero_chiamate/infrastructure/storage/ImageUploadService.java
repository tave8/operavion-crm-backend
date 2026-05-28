package giuseppetavella.zero_chiamate.infrastructure.storage;

import giuseppetavella.zero_chiamate.infrastructure.storage.exceptions.InvalidFileUploadedException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.integrations.cloudinary.CloudinaryAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

    @Autowired
    private CloudinaryAPIService cloudinaryAPIService;
    
    
    /**
     * Upload an image.
     * 
     * @return URL of the uploaded image
     */
    public String upload(MultipartFile image) 
    {
        // if file is not an image
        ValidationHelper.requireFileImageElseThrow(
                image,
                () -> new InvalidFileUploadedException("File is not an image.")
        );

        var bytes = FileHelper.getBytes(image);

        return cloudinaryAPIService.upload(bytes);
    }
    
    
}
