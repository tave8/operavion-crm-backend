package giuseppetavella.demo_login_system.payloads.in_response;

/**
 * This class represents one item of the list
 * of result items of any geocoding API.
 * It is therefore API-agnostic.
 */
public class GeocodingAutocompleteResultItemToSendDTO {
    
    private final double lat;
    private final double lon;
    private final String label;
    
    public GeocodingAutocompleteResultItemToSendDTO(
            double lat, double lon, String label
    ) {
        
        this.lat = lat;
        this.lon = lon;
        this.label = label;
        
    }

    public String getLabel() {
        return label;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
