package giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions;

public class DocumentTextExtractionException extends RuntimeException {
    public DocumentTextExtractionException(String message) {
        super("Error while extracting text from document. DETAILS: " +message);
    }
}
