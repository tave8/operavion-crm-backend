package giuseppetavella.zero_chiamate.exceptions;

public class FileUploadException extends FileException {
    public FileUploadException(String message) {
        super("Error during file upload. DETAILS: " + message);
    }
}
