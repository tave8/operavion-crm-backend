package giuseppetavella.zero_chiamate.infrastructure.csv.exceptions;

public class CsvGenerationException extends CsvException {
    public CsvGenerationException(String message) {
        super("Error while generating a CSV. DETAILS: " + message);
    }
}
