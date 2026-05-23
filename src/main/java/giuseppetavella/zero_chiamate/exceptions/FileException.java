package giuseppetavella.zero_chiamate.exceptions;

public class FileException extends RuntimeException {
    public FileException(String message) {
        super("Generic error while working with a file. "
                +"Possible causes: reading bytes from file. DETAILS: " + message);
    }
}
