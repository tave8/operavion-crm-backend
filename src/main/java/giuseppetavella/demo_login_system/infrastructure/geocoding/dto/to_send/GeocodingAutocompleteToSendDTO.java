package giuseppetavella.demo_login_system.infrastructure.geocoding.dto.to_send;


import java.util.List;

/**
 * This class represents the entire payload
 * to send to the frontend/client, regardless 
 * of the geocoding API used. 
 * It is therefore API-agnostic.
 */
public class GeocodingAutocompleteToSendDTO {
    
    private final List<GeocodingAutocompleteResultItemToSendDTO> results;
    
    public GeocodingAutocompleteToSendDTO(List<GeocodingAutocompleteResultItemToSendDTO> results) {
        this.results = results;
    }

    public List<GeocodingAutocompleteResultItemToSendDTO> getResults() {
        return results;
    }
}
