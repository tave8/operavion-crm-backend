package giuseppetavella.demo_login_system.payloads.in_response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents one item of the list
 * of result items of any geocoding API.
 * It is therefore API-agnostic.
 */

public class GeocodingAutocompleteResultItemToSendDTO {
    private final double lat;
    private final double lon;
    private final String displayName;
    private final double confidence;
    private final String country;
    private final String state;
    private final String county;
    private final String resultType;

    public GeocodingAutocompleteResultItemToSendDTO(double confidence, double lat, double lon, String displayName, String country, String state, String county, String resultType) {
        this.confidence = confidence;
        this.lat = lat;
        this.lon = lon;
        this.displayName = displayName;
        this.country = country;
        this.state = state;
        this.county = county;
        this.resultType = resultType;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getCounty() {
        return county;
    }

    public String getCountry() {
        return country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getResultType() {
        return resultType;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "GeocodingAutocompleteResultItemToSendDTO{" +
                "confidence=" + confidence +
                ", lat=" + lat +
                ", lon=" + lon +
                ", label='" + displayName + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", county='" + county + '\'' +
                ", resultType='" + resultType + '\'' +
                '}';
    }
}