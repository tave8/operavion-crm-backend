package giuseppetavella.zero_chiamate.exceptions;

public class DocumentTextExtractionException extends RuntimeException {
    public DocumentTextExtractionException(String message) {
        super("Error while extracting text from document. DETAILS: " +message);
    }
}
