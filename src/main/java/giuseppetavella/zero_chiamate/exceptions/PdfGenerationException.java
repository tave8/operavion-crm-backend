package giuseppetavella.zero_chiamate.exceptions;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String message) {
        super("Error while working with or generating a pdf. DETAILS: "  + message);
    }
}
