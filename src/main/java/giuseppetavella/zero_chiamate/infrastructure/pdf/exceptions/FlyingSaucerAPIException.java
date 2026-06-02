package giuseppetavella.zero_chiamate.infrastructure.pdf.exceptions;

public class FlyingSaucerAPIException extends PdfGenerationException {
    public FlyingSaucerAPIException(String message) {
        super("Error while working with Flying Saucer API (pdf generation). DETAILS: " + message);
    }
}
