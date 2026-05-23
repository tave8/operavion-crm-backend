package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.infrastructure.Language;

public class LanguageHelper {

    private static Language DEFAULT_LANGUAGE = Language.IT;
    
    /**
     * Get default language
     * @return
     */
    public static Language getLanguage() {
        return DEFAULT_LANGUAGE;
    }
    
}
