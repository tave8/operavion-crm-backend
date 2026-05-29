package giuseppetavella.zero_chiamate.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File was not found in cloud storage.");
    }
}
