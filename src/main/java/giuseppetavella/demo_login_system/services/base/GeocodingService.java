package giuseppetavella.demo_login_system.services.base;

import giuseppetavella.demo_login_system.api_payloads.in_response.GeoapifyJsonSentDTO;
import giuseppetavella.demo_login_system.exceptions.GeocodingAPIException;
import giuseppetavella.demo_login_system.payloads.in_response.GeocodingAutocompleteResultItemToSendDTO;
import giuseppetavella.demo_login_system.payloads.in_response.GeocodingAutocompleteToSendDTO;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API-dependent Geocoding service.
 */
@Service
public class GeocodingService {

    @Value("${geoapify-apikey}")
    private String GEOAPIFY_API_KEY;
    
    private final String API_URL = "https://api.geoapify.com/v1/geocode/search";

    /**
     * API-agnostic geocoding call.
     * Call this from outside.
     */
    public GeocodingAutocompleteToSendDTO doGeocodeRequest(String query,
                                                           String lang,
                                                           Integer limit) 
    {
        
        // **********************
        // Make API-specific request
        // ************************
        
        ResponseEntity<GeoapifyJsonSentDTO> responseEntity = this.doGeocodeRequestInternal(query, lang, limit);
        
        GeoapifyJsonSentDTO body = responseEntity.getBody();
        
        if(body == null) {
            throw new GeocodingAPIException(query, "The body of the API response is null.");
        }

        // ************************
        // MAPPING: API-SPECIFIC -> API-AGNOSTIC 
        // ************************
        
        // map the API-specific item, to the API-agnostic item
        // this makes our code API-independent
        
        List<GeocodingAutocompleteResultItemToSendDTO> results = body.getResults().stream().map(
                item -> new GeocodingAutocompleteResultItemToSendDTO(
                item.getLat(),
                item.getLon(),
                item.getFormatted(),
                item.getRank().getConfidence(),
                item.getCountry(),
                item.getState(),
                item.getCounty(),
                item.getResultType()
        )).toList();
        
        return new GeocodingAutocompleteToSendDTO(results);
        
    }
    
    
    /**
     * Do request to API.
     * 
     * @param lang
     * @param limit
     * @return
     */
    private ResponseEntity<GeoapifyJsonSentDTO> doGeocodeRequestInternal(String query, 
                                                                         String lang, 
                                                                         Integer limit) 
    {

        RestTemplate restTemplate = new RestTemplate();
        
        Map<String, Object> params = new HashMap<>();
        
        // this is what the API will search for, for example "rome, italy"
        params.put("text", query);
        // json format by default
        params.put("format", "json");
        
        // add keys if specified 
        if(limit != null) {
            params.put("limit", limit);
        }
        
        if(lang != null) {
            params.put("lang", lang);
        }

        String url = this.buildUrl(params);

        URI uri;
        
        // bug fix: url was double encoded.
        // make sure that url is encoded once.
        // using uri prevents double encoding
        
        try {
            
            uri = new URI(url);  
            
        } catch(URISyntaxException ex) {
            
            throw new GeocodingAPIException(query, "Error while parsing URI. DETAILS: " + ex.getMessage());
        
        }
        
        
        try {

            // System.out.println("BODY AS STRING: " + restTemplate.getForEntity(uri, String.class).getBody());
            
            return restTemplate.getForEntity(uri, GeoapifyJsonSentDTO.class);
            
        } catch(HttpClientErrorException ex) {
            
            throw new GeocodingAPIException(query, ex.getMessage());
            
        }
    }
    
    private ResponseEntity<GeoapifyJsonSentDTO> doGeocodeRequestInternal(String query, String lang) 
    {
        return this.doGeocodeRequestInternal(query, lang, null);    
    }

    private ResponseEntity<GeoapifyJsonSentDTO> doGeocodeRequestInternal(String query, Integer limit)
    {
        return this.doGeocodeRequestInternal(query, null, limit);
    }

    private ResponseEntity<GeoapifyJsonSentDTO> doGeocodeRequestInternal(String query)
    {
        return this.doGeocodeRequestInternal(query, null, null);
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
                throw new GeocodingAPIException("After parsing API URL, result was null.");
            }
            
        } catch(RuntimeException ex) {
            
            throw new GeocodingAPIException("Could not parse URL correctly. DETAILS: " + ex.getMessage());
            
        }
        
        HttpUrl.Builder builder = httpUrl
                                .newBuilder()
                                .addQueryParameter("apiKey", GEOAPIFY_API_KEY);

        queryParams.forEach((key, value) -> builder.addQueryParameter(key, String.valueOf(value)));

        return builder.build().toString();
    
    }
    
    
}
