package giuseppetavella.zero_chiamate.integrations.geoapify.exceptions;

import giuseppetavella.zero_chiamate.infrastructure.geocoding.exceptions.GeocodingAPIException;

public class GeoapifyAPIException extends GeocodingAPIException {
  public GeoapifyAPIException(String message) {
    super("Error while working with Geoapify API. DETAILS: " + message);
  }

  public GeoapifyAPIException(String query, String message) {
    super("Error while working with Geoapify API for query '"+query+"'. DETAILS: " + message);
  }
}
