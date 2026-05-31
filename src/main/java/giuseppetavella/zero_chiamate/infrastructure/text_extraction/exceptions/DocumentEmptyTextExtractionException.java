package giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions;

public class DocumentEmptyTextExtractionException extends RuntimeException {
    public DocumentEmptyTextExtractionException(String message) {
        super("During text extraction, text extracted was required to be non-empty. DETAILS: " +message);
    }
}
