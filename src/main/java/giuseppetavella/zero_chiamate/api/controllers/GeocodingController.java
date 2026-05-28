package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResult;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {
    
    @Autowired
    private GeocodingService geocodingService;

    /**
     * Get the geocoding autocompletion.
     * Autocompletion means, a list of items 
     * that are the result of the API finding their best match against a query.
     * 
     * @return
     */
    @GetMapping("/autocomplete")
    public GeocodingAutocompleteResult geocode(
            @RequestParam(value = "q") String query,
            @RequestParam(value = "lang", defaultValue = "en") String language
    ) 
    {
        // TODO: should check that the language is valid
        //  also, could give some flexibility into how many results are returned
        
        return geocodingService.doRequest(query, language, 10);
        
    }
    
}
