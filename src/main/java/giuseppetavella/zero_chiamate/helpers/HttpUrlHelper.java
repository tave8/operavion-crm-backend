package giuseppetavella.zero_chiamate.helpers;

import okhttp3.HttpUrl;

import java.util.Map;
import java.util.function.Supplier;

public class HttpUrlHelper {
    
    /**
     * Build the query
     *
     * @param queryParams
     * @return
     */
    public static String buildUrl(HttpUrl.Builder httpBuilder, 
                            Map<String, Object> queryParams) 
    {

        queryParams.forEach((key, value) -> httpBuilder.addQueryParameter(key, String.valueOf(value)));

        return httpBuilder.build().toString();

    }

    

    /**
     *
     * @return
     */
    public static HttpUrl buildHttpUrlElseThrow(String url,
                                                Supplier<? extends RuntimeException> supplier)
    {
        HttpUrl httpUrl;

        try {

            httpUrl = HttpUrl.parse(url);

            if(httpUrl == null) {
                throw supplier.get();
            }

            return httpUrl;

        } catch(RuntimeException ex) {

            throw supplier.get();

        }

    }
    
    
}
