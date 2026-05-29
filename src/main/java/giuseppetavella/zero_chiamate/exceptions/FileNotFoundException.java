package giuseppetavella.zero_chiamate.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super("File was not found. DETAILS: " + message);
    }
}
