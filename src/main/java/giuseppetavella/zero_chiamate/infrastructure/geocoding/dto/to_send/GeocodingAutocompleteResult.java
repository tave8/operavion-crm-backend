package giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send;


import java.util.List;

/**
 * This class represents the entire payload
 * to send to the frontend/client, regardless 
 * of the geocoding API used. 
 * It is therefore API-agnostic.
 */
public class GeocodingAutocompleteResult {
    
    private final List<GeocodingAutocompleteResultItem> results;
    
    public GeocodingAutocompleteResult(List<GeocodingAutocompleteResultItem> results) {
        this.results = results;
    }

    public List<GeocodingAutocompleteResultItem> getResults() {
        return results;
    }
}
