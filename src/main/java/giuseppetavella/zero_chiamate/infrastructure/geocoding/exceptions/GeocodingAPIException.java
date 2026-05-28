package giuseppetavella.zero_chiamate.infrastructure.geocoding.exceptions;

public class GeocodingAPIException extends RuntimeException {
    public GeocodingAPIException(String message) {
        super("Error while working with the Geocoding API. DETAILS: " + message);
    }

    public GeocodingAPIException(String query, String message) {
        super("Error while working with the Geocoding API for query '"+query+"'. DETAILS: " + message);
    }
}
