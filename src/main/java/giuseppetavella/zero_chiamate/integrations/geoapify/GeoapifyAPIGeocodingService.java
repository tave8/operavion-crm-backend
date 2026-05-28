package giuseppetavella.zero_chiamate.integrations.geoapify;

import giuseppetavella.zero_chiamate.config.GeoapifyAPIConfig;
import giuseppetavella.zero_chiamate.helpers.UrlHelper;
import giuseppetavella.zero_chiamate.helpers.ValidationHelper;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonResultItemDTO;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonSentDTO;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.exceptions.GeocodingAPIException;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResultItem;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResult;
import giuseppetavella.zero_chiamate.integrations.geoapify.exceptions.GeoapifyAPIException;
import giuseppetavella.zero_chiamate.integrations.geoapify.params.GeoapifyAPIRequestParams;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API service.
 * API name: Geoapify.
 * 
 */
@Service
public class GeoapifyAPIGeocodingService {

    private final String apiKey;
    private final String geocodingUrl;
    private final RestTemplate restTemplate;

    public GeoapifyAPIGeocodingService(
            @Qualifier("geoapifyAPIkey") String apiKey,
            @Qualifier("geoapifyAPIGeocodingUrl") String geocodingUrl) {
        this.apiKey = apiKey;
        this.geocodingUrl = geocodingUrl;
        // instantiate once, not per request
        this.restTemplate = new RestTemplate();
    }

    /**
     * Do request to Geoapify geocoding API.
     */
    public GeoapifyJsonSentDTO doRequest(GeoapifyAPIRequestParams params) {

        // query cannot be blank
        ValidationHelper.requireStringNotBlankElseThrowWith(
                params.query(),
                "Query cannot be null or empty."
        );

        // build the URI from params
        URI uri = buildUri(params);

        try {
            // do a GET request and deserialize payload as the class
            ResponseEntity<GeoapifyJsonSentDTO> response = restTemplate.getForEntity(
                    uri,
                    GeoapifyJsonSentDTO.class
            );

            GeoapifyJsonSentDTO body = response.getBody();

            // body should never be null on a successful response
            if (body == null) {
                throw new GeoapifyAPIException(
                        params.query(),
                        "The body of the API response is null. URI was: " + uri
                );
            }

            return body;

        } catch (RestClientException ex) {
            throw new GeocodingAPIException(params.query(), ex.getMessage());
        }
    }

    /**
     * Build the URI for the geocoding API request.
     * Uses Spring's UriComponentsBuilder to handle
     * query param encoding and URL construction.
     */
    private URI buildUri(GeoapifyAPIRequestParams params) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(geocodingUrl)
                // API authentication key
                .queryParam("apiKey", apiKey)
                // this is what the API will search for, for example "rome, italy"
                .queryParam("text", params.query())
                // response format, defaults to "json"
                .queryParam("format", params.format());

        // add optional params if specified
        if (params.limit() != null) {
            // the number of items we want back, at most
            builder.queryParam("limit", params.limit());
        }

        if (params.lang() != null) {
            // language of results (en, it, de, es, fr etc.)
            builder.queryParam("lang", params.lang());
        }

        return builder.build().toUri();
    }
    
}
