package giuseppetavella.zero_chiamate.infrastructure.geocoding;

import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResultItem;
import giuseppetavella.zero_chiamate.integrations.geoapify.GeoapifyAPIGeocodingService;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.dto.to_send.GeocodingAutocompleteResult;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonResultItemDTO;
import giuseppetavella.zero_chiamate.integrations.geoapify.dto.GeoapifyJsonSentDTO;
import giuseppetavella.zero_chiamate.integrations.geoapify.params.GeoapifyAPIRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * App layer.
 * 
 */
@Service
public class GeocodingService {
    
    @Autowired
    private GeoapifyAPIGeocodingService geoapifyAPIGeocodingService;

    // these result types go from the most precise (left)
    // to the most generic (right)
    private static final List<String> RESULT_TYPE_PRECISION = List.of(
            "building", "amenity", "street", "suburb", "postcode", "city", "county", "state", "country"
    );


    /**
     * Do a geocoding request.
     * 
     * @param query
     * @param lang
     * @param limit
     * @return
     */
    public GeocodingAutocompleteResult doRequest(String query,
                                                 String lang,
                                                 Integer limit) 
    {

        // the request typed params
        var params = new GeoapifyAPIRequestParams(
                query, lang, limit
        );

        // check that the integer is > 0 

        GeoapifyJsonSentDTO body = geoapifyAPIGeocodingService.doRequest(params);


        // map the API-specific item, to the API-agnostic item
        // this makes our code API-independent

        List<GeocodingAutocompleteResultItem> results = body.getResults().stream()

                // sort by confidence first, and then by result type
                .sorted(Comparator
                        .comparingDouble((GeoapifyJsonResultItemDTO r) -> r.getRank().getConfidence())
                        .reversed()
                        .thenComparingInt(r -> RESULT_TYPE_PRECISION.indexOf(r.getResultType()))
                )

                // map the API-specific payload, to the API-agnostic payload
                .map(
                        item -> {

                            return new GeocodingAutocompleteResultItem(
                                    item.getRank().getConfidence(),
                                    item.getLat(),
                                    item.getLon(),
                                    item.getFormatted(),
                                    item.getCountry(),
                                    item.getState(),
                                    item.getCounty(),
                                    item.getResultType()
                            );

                        }).toList();

        return new GeocodingAutocompleteResult(results);
    
    }
    
}
