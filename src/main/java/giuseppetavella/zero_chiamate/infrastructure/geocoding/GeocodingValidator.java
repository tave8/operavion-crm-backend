package giuseppetavella.zero_chiamate.infrastructure.geocoding;

import giuseppetavella.zero_chiamate.exceptions.InvalidDataFormatException;
import giuseppetavella.zero_chiamate.helpers.EnumHelper;
import org.springframework.stereotype.Component;

/**
 * Validating Geocoding data.
 */
@Component
public class GeocodingValidator {

    /**
     * Require that the language provided as string, is a valid language.
     */
    // public void requireValidLanguage(String language) {
    //     try {
    //         EnumHelper.parseEnum(GeocodingLanguage.class, language);
    //     } catch (InvalidDataFormatException e) {
    //         throw new InvalidDataFormatException("The language '"++" does not exist in the list of available ");
    //     }
    // }
    
}
