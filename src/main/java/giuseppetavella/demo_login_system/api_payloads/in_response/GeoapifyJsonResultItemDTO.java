package giuseppetavella.demo_login_system.api_payloads.in_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyJsonResultItemDTO {
    
    private double lat;
    private double lon;
    private String formatted;

    public String getFormatted() {
        return formatted;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "GeoapifyJsonResultItemDTO{" +
                "formatted='" + formatted + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
