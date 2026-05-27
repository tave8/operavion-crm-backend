package giuseppetavella.zero_chiamate.helpers;

import org.springframework.core.io.ClassPathResource;

/**
 * Helper class for dealing with filesystem.
 */
public class FileSystemHelper {

    /**
     * The resource exists?
     * By resources we mean those in <code>src/main/resources</code>
     * 
     * @param resourcePath
     * @return
     */
    public static boolean resourceExists(String resourcePath)
    {
        return new ClassPathResource(resourcePath).exists();    
    }
    
    
    /**
     * Html template exists?
     * - Must be in templates directory, inside resources
     * - Must exclude file extensione, so without .html
     * 
     * @return
     */
    public static boolean templateExists(String templatePathWithoutExt)
    {
        return resourceExists("templates/" + templatePathWithoutExt + ".html");
    }
    
    
    
}
