package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.FileDownloadException;
import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.exceptions.UnknownFileTypeException;
import org.apache.tika.Tika;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class FileHelper {

    private static final Map<String, String> MIME_TO_EXTENSION = Map.of(
            "application/pdf",  "pdf",
            "text/csv",         "csv",
            "image/png",        "png",
            "image/jpeg",       "jpg"
    );

    // dependency to extract file extension from bytes
    private static final Tika TIKA = new Tika();

    // 1 MB
    public static final long MB = 1024 * 1024;
    
    /**
     * Is the uploaded file an image?
     */
    public static boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        if(contentType == null) {
            return false;
        }
        // the file is an image if its content type 
        // starts with image/
        return contentType.startsWith("image/");
    }

    /**
     * Get the file size in MB.
     */
    public static double getFileSizeInMB(MultipartFile file) {
        return file.getSize() / (double) FileHelper.MB;
    }

    /**
     * Is the file size smaller than the
     * provided size in bytes?
     */
    public static boolean sizeIsSmallerThan(MultipartFile file, long maxSizeInBytes) {
        return file.getSize() < maxSizeInBytes;
    }

    /**
     * Is the given file within the avatar image
     * default limit (2 MB)?
     */
    public static boolean isWithinAvatarSize(MultipartFile file) {
        return FileHelper.sizeIsSmallerThan(file, 2 * FileHelper.MB);
    }

    /**
     * byte array -> base64 
     */
    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * URL -> base64
     * TODO: fix semantics. it's not url to base64, it's 
     *  "download the content of the URL" and that is converted to base64
     */
    public static String urlToBase64(String url) throws FileDownloadException 
    {
        try {
            
            byte[] bytes = new URL(url).openStream().readAllBytes();
            return FileHelper.toBase64(bytes);
        
        } catch(MalformedURLException ex) {
            throw new FileDownloadException("URL is malformed. DETAILS: " + ex.getMessage());  
        } catch(IOException ex) {
            throw new FileDownloadException(ex.getMessage());
        }
        
    }

    /**
     * Get the file extension from bytes.
     * The file extension is without the dot, for example "pdf" or "jpg".
     */
    public static String getFileType(byte[] bytes) throws UnknownFileTypeException
    {

        // if bytes are empty, there's no point getting file extension
        if(bytes.length == 0) {

            throw new UnknownFileTypeException("No bytes found in this file, it means the file is empty. "
                                                +"So cannot determine its extension.");

        }
        
        String mimeType = TIKA.detect(bytes);

        // recognized is a synonim for "supported or known"
        boolean filenameIsInternallyRecognized = MIME_TO_EXTENSION.containsKey(mimeType);
        
        if (!filenameIsInternallyRecognized) {
            throw new UnknownFileTypeException(mimeType);
        }

        return MIME_TO_EXTENSION.get(mimeType);
    }

    /**
     * Get the file extension from a file.
     * The file extension is without the dot, for example "pdf" or "jpg".
     * 
     * @throws UnknownFileTypeException if the type of the file was not mapped/not internally recognized
     * @throws FileException if any error while getting bytes from file
     */
    public static String getFileType(MultipartFile file) throws UnknownFileTypeException, FileException
    {
        try {
            
            return FileHelper.getFileType(file.getBytes());
            
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
        
    }
    
    public static byte[] getBytes(MultipartFile file) throws FileException
    {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    
    }

    /**
     * Read a pdf into bytes.
     */
    public static byte[] readPdf(String filepath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filepath);
        try (InputStream is = resource.getInputStream()) {
            return is.readAllBytes();
        }
    }
    
}
