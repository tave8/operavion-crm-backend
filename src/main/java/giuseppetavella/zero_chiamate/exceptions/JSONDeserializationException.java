package giuseppetavella.zero_chiamate.exceptions;

public class JSONDeserializationException extends RuntimeException {
    public JSONDeserializationException(String message) {
        super("Error during JSON deserialization. DETAILS: " + message);
    }
}
