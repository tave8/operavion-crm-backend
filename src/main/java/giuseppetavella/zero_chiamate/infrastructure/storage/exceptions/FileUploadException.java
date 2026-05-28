package giuseppetavella.zero_chiamate.infrastructure.storage.exceptions;

import giuseppetavella.zero_chiamate.exceptions.FileException;

public class FileUploadException extends FileException {
    public FileUploadException(String message) {
        super("Error during file upload. DETAILS: " + message);
    }
}
