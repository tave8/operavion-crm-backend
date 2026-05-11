package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.api_payloads.in_response.GeoapifyJsonSentDTO;
import giuseppetavella.demo_login_system.payloads.in_response.GeocodingAutocompleteToSendDTO;
import giuseppetavella.demo_login_system.services.base.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppGeocodingService extends GeocodingService {
    
    @Autowired
    private GeocodingService geocodingService;

    /**
     * Do a geocoding request.
     * 
     * @param query
     * @param lang
     * @param limit
     * @return
     */
    public GeocodingAutocompleteToSendDTO doGeocodeRequest(String query, String lang, Integer limit) 
    {
        GeocodingAutocompleteToSendDTO payload = this.geocodingService.doGeocodeRequest(query, lang, limit);
    
        return payload;
    }
    
}
