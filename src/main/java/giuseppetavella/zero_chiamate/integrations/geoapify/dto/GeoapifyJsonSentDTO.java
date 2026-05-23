package giuseppetavella.zero_chiamate.integrations.geoapify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Geoapify API JSON response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyJsonSentDTO {

    private List<GeoapifyJsonResultItemDTO> results;

    public List<GeoapifyJsonResultItemDTO> getResults() {
        return results;
    }

    public void setResults(List<GeoapifyJsonResultItemDTO> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "GeoapifyJsonSentDTO{" +
                "results=" + results +
                '}';
    }
}
