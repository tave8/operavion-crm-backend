package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.AppConfigurationException;
import giuseppetavella.zero_chiamate.exceptions.InvalidUrlException;
import giuseppetavella.zero_chiamate.infrastructure.geocoding.exceptions.GeocodingAPIException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Helper class for working with URLs,
 * for example for validating that a URL is valid.
 */
public class UrlHelper {

    private static final List<String> VALID_SCHEMES = List.of("http", "https");

    /**
     * Build a URL.
     * 
     * @param base
     * @param path
     * @return
     * 
     * @throws InvalidUrlException 
     */
    public static String buildUrl(String base, String path) throws InvalidUrlException
    {
        try {
            String combined = base + path;

            // reject double slashes in path
            String pathPart = combined.replaceFirst("^https?://[^/]+", "");
            
            if (pathPart.contains("//")) {
                throw new InvalidUrlException(
                        "Invalid URL: '" + combined + "'. Double slashes are not allowed in path."
                );
            }

            String[] parts = combined.split("\\?", 2);
            String urlBase = parts[0];
            String query = parts.length > 1 ? parts[1] : null;

            URI uri;
            if (query != null) {
                String encodedQuery = query.replace(" ", "%20");
                uri = new URI(urlBase + "?" + encodedQuery).normalize();
            } else {
                uri = new URI(urlBase).normalize();
            }

            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new InvalidUrlException(
                        "Invalid URL: '" + combined + "'. Missing scheme or host."
                );
            }

            if (!VALID_SCHEMES.contains(uri.getScheme())) {
                throw new InvalidUrlException(
                        "Invalid URL scheme: '" + uri.getScheme() + "'. Must be http or https."
                );
            }

            return uri.toString();

        } catch (URISyntaxException e) {
            throw new InvalidUrlException(
                    "Invalid URL syntax: '" + base + path + "'. Error: " + e.getMessage()
            );
        }
    }


    
    /**
     * Is a URL valid?
     * 
     * @param base
     * @param path
     * @return
     */
    public static boolean isValidUrl(String base, String path) 
    {
        try {
            buildUrl(base, path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    public static boolean isValidUrl(String url) 
    {
        return UrlHelper.isValidUrl(url, "");
    }


    /**
     * Build URI from url, else throw.
     * 
     * This solution solved the following problem description:
     * 
     * "
     *       // bug fix: url was double encoded.
     *         // make sure that url is encoded once.
     *         // using uri prevents double encoding
     * "
     * 
     * @return
     */
    public static URI buildURIElseThrow(String url,
                                        Supplier<? extends RuntimeException> supplier) {

        try {

            return new URI(url);

        } catch(URISyntaxException ex) {
            throw supplier.get();
        }
        
    }
    
}
