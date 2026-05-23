package giuseppetavella.demo_login_system.integrations.geoapify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyJsonResultItemDTO {
    
    private double lat;
    private double lon;
    private String formatted;
    private GeoapifyJsonResultItemRankDTO rank; 
    private String country;
    private String state;
    private String county;
    // the actual property name in the received JSON is result_type
    @JsonProperty("result_type")
    private String resultType;
    
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

    public GeoapifyJsonResultItemRankDTO getRank() {
        return rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setRank(GeoapifyJsonResultItemRankDTO rank) {
        this.rank = rank;
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
