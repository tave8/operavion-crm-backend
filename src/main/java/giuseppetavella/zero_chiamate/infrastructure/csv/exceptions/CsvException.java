package giuseppetavella.zero_chiamate.infrastructure.csv.exceptions;

public class CsvException extends RuntimeException {
    public CsvException(String message) {
        super("Error while working with a CSV. DETAILS: " + message);
    }
}
