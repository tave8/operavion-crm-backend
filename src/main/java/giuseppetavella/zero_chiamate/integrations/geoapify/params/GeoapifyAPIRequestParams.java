package giuseppetavella.zero_chiamate.integrations.geoapify.params;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record GeoapifyAPIRequestParams(
        // the query made by the user,
        // for example: via Roma 89, Milan, Italy
        String query,

        // this will tell the GeoapifyAPI which language
        // to get the results in. must be
        // in lowercase and have two letters (en, it, de, es, fr etc.)
        // although this check is not being performed for now
        String lang,

        // the number of items we want back, at most
        Integer limit,

        // the format of the response
        // defaults to "json"
        String format
) {

    /**
     * Format is json by default.
     * 
     * @param query
     * @param lang
     * @param limit
     */
    public GeoapifyAPIRequestParams(
            @NonNull String query,
            @Nullable String lang,
            @Nullable Integer limit
    ) {
        this(query, lang, limit, "json");
    }

}


