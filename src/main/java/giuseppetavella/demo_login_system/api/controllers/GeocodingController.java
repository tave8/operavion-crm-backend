package giuseppetavella.demo_login_system.api.controllers;

import giuseppetavella.demo_login_system.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteToSendDTO;
import giuseppetavella.demo_login_system.infrastructure.geocoding.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {
    
    @Autowired
    private GeocodingService appGeocodingService;

    /**
     * Get the geocoding autocompletion.
     * Autocompletion means, a list of items 
     * that are the result of the API finding their best match against a query.
     * 
     * @return
     */
    @GetMapping("/autocomplete")
    public GeocodingAutocompleteToSendDTO geocode(
            @RequestParam(value = "q") String query,
            @RequestParam(value = "lang", defaultValue = "en") String language
    ) 
    {
        // TODO: should check that the language is valid
        //  also, could give some flexibility into how many results are returned
        return this.appGeocodingService.doGeocodeRequest(query, language, 10);
        
    }
    
}
