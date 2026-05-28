package giuseppetavella.zero_chiamate.integrations.cloudinary.exceptions;

import giuseppetavella.zero_chiamate.infrastructure.storage.exceptions.FileUploadException;

public class CloudinaryAPIException extends FileUploadException {
    public CloudinaryAPIException(String message) {
        super("Error while working with Cloudinary API. DETAILS: " + message);
    }
}
