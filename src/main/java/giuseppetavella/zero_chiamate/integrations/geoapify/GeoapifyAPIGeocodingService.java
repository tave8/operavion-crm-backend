package giuseppetavella.zero_chiamate.integrations.geoapify;

import giuseppetavella.zero_chiamate.helpers.UrlHelper;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonResultItemDTO;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonSentDTO;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.exceptions.GeocodingAPIException;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResultItem;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResult;
import giuseppetavella.zero_chiamate.integrations.geoapify.exceptions.GeoapifyAPIException;
import giuseppetavella.zero_chiamate.integrations.geoapify.params.GeoapifyAPIRequestParams;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    private final String GEOAPIFY_API_KEY;
    
    public GeoapifyAPIGeocodingService(@Qualifier("geoapifyAPIkey") String apiKey) {
        this.GEOAPIFY_API_KEY = apiKey;
    }
    
    private final String API_URL = "https://api.geoapify.com/v1/geocode/search";
    

    /**
     * API-agnostic geocoding call.
     * Call this from outside.
     */
    public GeoapifyJsonSentDTO doRequest(String query,
                                        String lang,
                                        Integer limit) 
    {
        
        // the request typed params
        var params = new GeoapifyAPIRequestParams(
                query, lang, limit
        );        
        
        ResponseEntity<GeoapifyJsonSentDTO> responseEntity = this.doRequestInternal(params);
        
        GeoapifyJsonSentDTO body = responseEntity.getBody();
        
        if(body == null) {
            throw new GeoapifyAPIException(query, "The body of the API response is null.");
        }
        
        return body;
        
    }
    
    
    /**
     * Do request to API.
     * 
     */
    private ResponseEntity<GeoapifyJsonSentDTO> doRequestInternal(GeoapifyAPIRequestParams params) 
    {
        // build the url 
        var url = buildUrl(
                paramsToMap(params)
        );

        
        var restTemplate = new RestTemplate();
        
        var uri = UrlHelper.buildURIElseThrow(
                url,
                () -> new GeocodingAPIException(params.query(), "Error while parsing URL into URI. URL was: " + url)
        );
        
        
        try {

            // System.out.println("BODY AS STRING: " + restTemplate.getForEntity(uri, String.class).getBody());
            
            // do a GET request and deserialize payload as the class
            return restTemplate.getForEntity(
                    uri, 
                    GeoapifyJsonSentDTO.class
            );
            
        } catch(HttpClientErrorException ex) {
            
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
        
        Map<String, Object> map = new HashMap<>();

        map.put("format", params.format());
        // this is what the API will search for, for example "rome, italy"
        map.put("text", params.query());

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
     * Build the query
     * 
     * @param queryParams
     * @return
     */
    private String buildUrl(Map<String, Object> queryParams) {

        HttpUrl httpUrl;
        
        try {
            
            httpUrl = HttpUrl.parse(API_URL);
            
            if(httpUrl == null) {
                throw new GeoapifyAPIException("After parsing API URL, result was null.");
            }
            
        } catch(RuntimeException ex) {
            
            throw new GeoapifyAPIException("Could not parse URL correctly. DETAILS: " + ex.getMessage());
            
        }
        
        HttpUrl.Builder builder = httpUrl
                                .newBuilder()
                                .addQueryParameter("apiKey", GEOAPIFY_API_KEY);

        queryParams.forEach((key, value) -> builder.addQueryParameter(key, String.valueOf(value)));

        return builder.build().toString();
    
    }
    
    
}
