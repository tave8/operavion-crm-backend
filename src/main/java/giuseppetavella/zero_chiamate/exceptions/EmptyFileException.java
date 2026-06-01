package giuseppetavella.zero_chiamate.exceptions;

public class EmptyFileException extends FileException {
    public EmptyFileException(String message) {
        super("The file is empty. DETAILS: " + message);
    }
}
