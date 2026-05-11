package giuseppetavella.demo_login_system.payloads.in_response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents one item of the list
 * of result items of any geocoding API.
 * It is therefore API-agnostic.
 */
@Data
@AllArgsConstructor
public class GeocodingAutocompleteResultItemToSendDTO {
    private final double lat;
    private final double lon;
    private final String label;
    private final double confidence;
    private final String country;
    private final String state;
    private final String county;
    private final String resultType;
}