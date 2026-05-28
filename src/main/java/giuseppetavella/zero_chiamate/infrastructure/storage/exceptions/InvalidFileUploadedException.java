package giuseppetavella.zero_chiamate.infrastructure.storage.exceptions;

public class InvalidFileUploadedException extends FileUploadException {
    public InvalidFileUploadedException(String message) {
        super("File sent is not valid. DETAILS: " + message);
    }
}
