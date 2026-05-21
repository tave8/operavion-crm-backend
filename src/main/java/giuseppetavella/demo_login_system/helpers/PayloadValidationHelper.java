package giuseppetavella.demo_login_system.helpers;

import giuseppetavella.demo_login_system.exceptions.PayloadValidationException;
import giuseppetavella.demo_login_system.exceptions.UnknownFileTypeException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Helper class for dealing with request payloads.
 * 
 * The methods in this class should always throw {@code PayloadValidationError},
 * at the very least.
 * 
 * <pre>{@code 
 *    
 *    PayloadValidationHelper.requiredPdf(file);
 *    
 *    PayloadValidationHelper.requireNoErrors(validation);
 * 
 * }</pre> 
 * 
 */
public class PayloadValidationHelper {
    
    /**
     * Helper when validating payloads.
     * Avoids having to check for errors manually, in each controller.
     * 
     * @throws PayloadValidationException if there's at least one error in the payload validation
     */
    public static void requireNoErrors(BindingResult validation) throws PayloadValidationException 
    {
        if (validation.hasErrors()) {
            List<String> errors = validation.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new PayloadValidationException(errors);
        }
    }


    /**
     * Require that the type/extension of the given file 
     * conforms
     * 
     * @param expectedFileExtWithoutDot the file extension without a dot, for example "pdf" or "png"
     *                                  
     * @throws UnknownFileTypeException if the file type is not internally mapped or recognized
     * @throws PayloadValidationException if the expected file type does not match the actual file type
     */
    public static void requireFileType(byte[] bytes, 
                                       String expectedFileExtWithoutDot,
                                       String originalFilename) throws PayloadValidationException, 
                                                                                UnknownFileTypeException
    {
        
        String actualFileType = FileHelper.getFileType(bytes);
                
        boolean hasSameType = actualFileType.equals(expectedFileExtWithoutDot.trim().toLowerCase());
        
        if(hasSameType) {
            return;    
        }
        
        // original filename is used if possible, otherwise say that 
        // filename cannot be determined
        String finalFilename = originalFilename == null ? "<filename cannot be determined>" : originalFilename;
        
        throw new PayloadValidationException("The file with original name '" + finalFilename + "' " 
                                            + "does not match the required file type. Expected file type: '" 
                                            + expectedFileExtWithoutDot + "'. Got '" + actualFileType + "' instead.");
    }


    public static void requireFileType(byte[] bytes,
                                       String expectedFileExtWithoutDot) throws PayloadValidationException, UnknownFileTypeException
    {

        PayloadValidationHelper.requireFileType(
                bytes,
                expectedFileExtWithoutDot,
                // because we've passed bytes, we don't know the original filename
                null
        );
        
    }
    

    public static void requireFileType(MultipartFile file,
                                       String expectedFileExtWithoutDot) throws PayloadValidationException, UnknownFileTypeException
    {
        
        PayloadValidationHelper.requireFileType(
            FileHelper.getBytes(file),
            expectedFileExtWithoutDot, 
            file.getOriginalFilename()
        );
        
    }


    /**
     * Require that this file is a pdf.
     *
     * @throws PayloadValidationException
     */
    public static void requiredPdf(byte[] bytes) throws PayloadValidationException, UnknownFileTypeException
    {
        PayloadValidationHelper.requireFileType(bytes, "pdf");
    }


    /**
     * Require that this file is a pdf.
     * 
     * @param file
     * @throws PayloadValidationException
     */
    public static void requiredPdf(MultipartFile file) throws PayloadValidationException, 
                                                              UnknownFileTypeException
    {
        PayloadValidationHelper.requireFileType(file, "pdf");
    }

    
    
}
