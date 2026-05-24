package giuseppetavella.zero_chiamate.exceptions;

public class InvalidUrlException extends InvalidDataFormatException {
    public InvalidUrlException(String invalidUrl) {
        super("URL is not valid. Invalid url was: " + invalidUrl);
    }
}
