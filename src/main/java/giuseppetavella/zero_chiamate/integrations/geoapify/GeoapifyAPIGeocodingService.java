package giuseppetavella.zero_chiamate.integrations.geoapify;

import giuseppetavella.zero_chiamate.config.GeoapifyAPIConfig;
import giuseppetavella.zero_chiamate.helpers.HttpUrlHelper;
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
    
    public GeoapifyAPIGeocodingService(
            @Qualifier("geoapifyAPIkey") String apiKey,
            @Qualifier("geoapifyAPIGeocodingUrl") String geocodingUrl) 
    {
        this.apiKey = apiKey;
        this.geocodingUrl = geocodingUrl;
    }
    
    
    /**
     * API-agnostic geocoding call.
     * Call this from outside.
     */
    public GeoapifyJsonSentDTO doRequest(GeoapifyAPIRequestParams params) 
    {
        
        ResponseEntity<GeoapifyJsonSentDTO> responseEntity = this.doRequestInternal(params);
        
        GeoapifyJsonSentDTO body = responseEntity.getBody();
        
        if(body == null) {
            throw new GeoapifyAPIException(
                    params.query(), 
                    "The body of the API response is null."
            );
        }
        
        return body;
        
    }
    
    
    /**
     * Do request to API.
     * 
     */
    private ResponseEntity<GeoapifyJsonSentDTO> doRequestInternal(GeoapifyAPIRequestParams params) 
    {
        
        var url = HttpUrlHelper.buildUrl(
                getHttpUrlBuilder(),
                paramsToMap(params)
        );
        
        var restTemplate = new RestTemplate();
        
        
        var uri = UrlHelper.buildURIElseThrow(
                url,
                () -> new GeocodingAPIException(params.query(), "Error while parsing URL into URI. URL was: " + url)
        );

        // System.out.println(uri.toString());
        
        try {

            // System.out.println("BODY AS STRING: " + restTemplate.getForEntity(uri, String.class).getBody());
            
            // do a GET request and deserialize payload as the class
            return restTemplate.getForEntity(
                    uri, 
                    GeoapifyJsonSentDTO.class
            );
            
        } catch(RestClientException ex) {
            
            throw new GeocodingAPIException(
                    params.query(), 
                    ex.getMessage()
            );
            
        }
    }
    


    /**
     * Convert typed params to a map of those params.
     * 
     * @param params
     * @return
     */
    public Map<String, Object> paramsToMap(GeoapifyAPIRequestParams params) {
        
        ValidationHelper.requireStringNotBlankElseThrowWith(
                params.query(),
                "Query cannot be null or empty."
        );
        
        Map<String, Object> map = new HashMap<>();

        // this is what the API will search for, for example "rome, italy"
        map.put("text", params.query());
        
        map.put("format", params.format());

        // add keys if specified 
        if(params.limit() != null) {
            map.put("limit", params.limit());
        }

        if(params.lang() != null) {
            map.put("lang", params.lang());
        }
        return map;
    }

    /**
     *
     * @return
     */
    public HttpUrl.Builder getHttpUrlBuilder()
    {

        var httpUrl = HttpUrlHelper.buildHttpUrlElseThrow(
                geocodingUrl,
                () -> new GeoapifyAPIException("Parsing of Geoapify API URL threw error. API URL was: " + geocodingUrl)
        );
        
        return httpUrl
                .newBuilder()
                .addQueryParameter("apiKey", apiKey);

    }
    
}
