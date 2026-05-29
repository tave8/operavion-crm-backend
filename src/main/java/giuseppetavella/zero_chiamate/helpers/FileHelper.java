package giuseppetavella.zero_chiamate.helpers;

import giuseppetavella.zero_chiamate.exceptions.FileDownloadException;
import giuseppetavella.zero_chiamate.exceptions.FileException;
import giuseppetavella.zero_chiamate.exceptions.UnknownFileTypeException;
import org.apache.tika.Tika;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileHelper {

    private static final Map<String, String> MIME_TO_EXTENSION = new HashMap<>(Map.of(
            // images
            "image/png",        "png",
            "image/jpeg",       "jpg",
            "image/gif",        "gif",
            "image/webp",       "webp",
            "image/svg+xml",    "svg",
            "image/bmp",        "bmp",
            "image/tiff",       "tiff"
    ));

    // add more mime type : file type mappings
    static {
        MIME_TO_EXTENSION.put("application/pdf",    "pdf");
        MIME_TO_EXTENSION.put("text/csv",           "csv");
        MIME_TO_EXTENSION.put("text/plain",         "txt");
        MIME_TO_EXTENSION.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        MIME_TO_EXTENSION.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",       "xlsx");
    }
    
    // dependency to extract file extension from bytes
    private static final Tika TIKA = new Tika();

    // 1 MB
    public static final long MB = 1024 * 1024;


    /**
     * Detect the MIME type of a file from its bytes.
     * For example: "image/png", "application/pdf".
     *
     * @throws UnknownFileTypeException if the file is empty
     */
    public static String getMimeType(byte[] bytes) {
        if (bytes.length == 0) {
            throw new UnknownFileTypeException("File is empty, cannot determine MIME type.");
        }
        return TIKA.detect(bytes);
    }


    /**
     * Detect the MIME type of a file from its bytes and filename.
     * The filename is used as a hint to correctly detect CSV files,
     * since Tika detects them as "text/plain" by content alone.
     */
    public static String getMimeType(byte[] bytes, String filename) {
        var mimeType = getMimeType(bytes);
        if (filename.endsWith(".csv") && mimeType.equals("text/plain")) {
            return "text/csv";
        }
        return mimeType;
    }


    /**
     * Detect the MIME type of a multipart file.
     *
     * @throws FileException if the file is null
     */
    public static String getMimeType(MultipartFile file) {
        if (file == null) {
            throw new FileException("File cannot be null.");
        }
        return getMimeType(getBytes(file), file.getOriginalFilename());
    }


    /**
     * Get the file extension from bytes and filename.
     * The extension is without the dot, for example "pdf" or "jpg".
     *
     * @throws UnknownFileTypeException if the file type is not supported
     */
    public static String getFileType(byte[] bytes, String filename) {
        var mimeType = getMimeType(bytes, filename);
        if (!MIME_TO_EXTENSION.containsKey(mimeType)) {
            throw new UnknownFileTypeException(mimeType);
        }
        return MIME_TO_EXTENSION.get(mimeType);
    }


    /**
     * Get the file extension from bytes only, without filename hint.
     * The extension is without the dot, for example "pdf" or "jpg".
     *
     * @throws UnknownFileTypeException if the file type is not supported
     */
    public static String getFileType(byte[] bytes) {
        return getFileType(bytes, "");
    }


    /**
     * Get the file extension from a multipart file.
     * The extension is without the dot, for example "pdf" or "jpg".
     *
     * @throws UnknownFileTypeException if the file type is not supported
     * @throws FileException if any error while reading the file
     */
    public static String getFileType(MultipartFile file) {
        if (file == null) {
            throw new FileException("File cannot be null.");
        }
        return getFileType(getBytes(file), file.getOriginalFilename());
    }


    /**
     * Read the bytes from a multipart file.
     *
     * @throws FileException if the file cannot be read
     */
    public static byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new FileException(ex.getMessage());
        }
    }
    


    /**
     * Is the uploaded file an image?
     */
    public static boolean isImage(byte[] bytes) {
        var mimeType = getMimeType(bytes);
        if(mimeType == null) {
            return false;
        }
        // the file is an image if its mime type 
        // starts with image/
        return mimeType.startsWith("image/");
    }


    /**
     * Is this file a pdf?
     */
    public static boolean isPdf(byte[] bytes) {
        var mimeType = getMimeType(bytes);
        if(mimeType == null) {
            return false;
        }
        return mimeType.equals("application/pdf");
    }


    /**
     * Is this file a pdf?
     */
    public static boolean isPdf(MultipartFile file) {
        return isPdf(FileHelper.getBytes(file));
    }
    


    /**
     * Read a file from /resources directory.
     * 
     * Use forward slashes, something like:  extra/invoice.pdf;
     */
    public static byte[] readFile(String filepath) {
        
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            try (InputStream is = resource.getInputStream()) {
                return is.readAllBytes();
            }
        } catch (IOException ex) {
            throw new FileException("File not found or unreadable: " + filepath + ". DETAILS: " + ex.getMessage());
        }
            
        
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
    
}
