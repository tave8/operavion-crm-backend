package giuseppetavella.zero_chiamate.infrastructure.geocoding;

import giuseppetavella.zero_chiamate.integrations.geoapify.GeoapifyAPIService;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteToSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService extends GeoapifyAPIService {
    
    @Autowired
    private GeoapifyAPIService geoapifyAPIService;

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
        GeocodingAutocompleteToSendDTO payload = this.geoapifyAPIService.doGeocodeRequest(query, lang, limit);
    
        return payload;
    }
    
}
