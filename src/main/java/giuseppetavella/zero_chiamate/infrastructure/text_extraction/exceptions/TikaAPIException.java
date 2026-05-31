package giuseppetavella.zero_chiamate.infrastructure.text_extraction.exceptions;

public class TikaAPIException extends RuntimeException {
    public TikaAPIException(String message) {
        super("Error while working with Tika "
                +"(text extraction from document). DETAILS: " + message);
    }
}
