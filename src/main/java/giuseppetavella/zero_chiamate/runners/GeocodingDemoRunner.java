package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.infrastructure.geocoding.GeocodingService;
import giuseppetavella.zero_chiamate.integrations.geoapify.GeoapifyAPIGeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GeocodingDemoRunner implements CommandLineRunner {
    
    @Autowired
    private GeocodingService appGeocodingService;
    
    @Autowired
    private GeoapifyAPIGeocodingService geoapifyAPIGeocodingService;
    
    @Override
    public void run(String... args) throws Exception {
        

        // ResponseEntity<GeoapifyJsonSentDTO> responseEntity = this.geocodingService.doGeocodeRequest("san calogero, vibo valentia","it", 10);
        //
        // GeoapifyJsonSentDTO body = responseEntity.getBody();
        //
        // System.out.println(body);
        
        // System.out.println(raw.getBody());
        
        // System.out.println(responseEntity.getBody());
        
    }
}
