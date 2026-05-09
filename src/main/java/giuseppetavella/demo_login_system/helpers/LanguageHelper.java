package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.enums.internal.Language;

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
