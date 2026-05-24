package giuseppetavella.zero_chiamate.exceptions;

import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class ErrorsHandler {

    @Autowired
    private ProblemsEmailService problemsEmailService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorsHandler.class);
    
    
    //
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsToSendDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }
    

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsToSendDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    
    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleFileUpload(FileUploadException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    
    @ExceptionHandler(InvalidFileUploadedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleInvalidFileUploaded(InvalidFileUploadedException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(BillingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleBillingException(BillingException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }


    @ExceptionHandler(StripeAPIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleStripeAPIException(StripeAPIException ex) {

        LOGGER.error("Error with Stripe API. DETAILS: {}", ex.getMessage());
        
        problemsEmailService.alertDevIfNonLocal(
                "Error with Stripe API",
                ex.getMessage(),
                ex
        );
        
        return new ErrorsToSendDTO(ex.getMessage());
    }



    @ExceptionHandler(EmailVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsToSendDTO handleEmailVerification(EmailVerificationException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(ForgotPasswordVerificationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsToSendDTO handleForgotPasswordVerification(ForgotPasswordVerificationException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }


    @ExceptionHandler(PdfGenerationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handlePdfGeneration(PdfGenerationException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }


    @ExceptionHandler(AIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handlAiException(AIException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    
    @ExceptionHandler(ContractExpectationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleContractExpectation(ContractExpectationException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    

    @ExceptionHandler(NotificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleNotificationException(NotificationException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }
    


    @ExceptionHandler(UnknownFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleUnknownFileTypeException(UnknownFileTypeException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(ClientAddressChecklistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleClientAddressChecklistException(ClientAddressChecklistException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(ShiftException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleShiftException(ShiftException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }



    /**
     * Handles exceptions raised when current user of request
     * is not authorized to access an endpoint. We protect
     * the endpoint by using @PreAuthorize annotation
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsToSendDTO handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return new ErrorsToSendDTO("You don't have the right authorization. DETAILS: "+ ex.getMessage());
    }

    @ExceptionHandler(PayloadValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handlePayloadValidationError(PayloadValidationException ex) {
        return new ErrorsToSendDTO(ex.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleInvalidDataException(InvalidDataException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(GeocodingAPIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleGeocodingAPIException(GeocodingAPIException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }

    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleEmailSendingException(EmailSendingException ex) {
        return new ErrorsToSendDTO(ex.getMessage());
    }
    

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage();

        if (message.contains("duplicate key")) {
            return new ErrorsToSendDTO("One or more values already exist and cannot be duplicated.");
        }
        if (message.contains("foreign key") && message.contains("insert")) {
            return new ErrorsToSendDTO("One or more referenced resources do not exist.");
        }
        if (message.contains("foreign key") && message.contains("delete")) {
            return new ErrorsToSendDTO("This resource cannot be deleted because it is referenced by other data.");
        }
        if (message.contains("not-null") || message.contains("null value")) {
            return new ErrorsToSendDTO("One or more required fields are missing.");
        }
        if (message.contains("check constraint")) {
            return new ErrorsToSendDTO("One or more values do not meet the required constraints.");
        }

        return new ErrorsToSendDTO("The request contains conflicting or invalid data.");
    }
    
    
    

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMethodArgumentoTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "The type of some request parameter cannot be cast to its correct type. "
                +"DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMethodNotValidMismatch(MethodArgumentNotValidException ex) {
        String msg = "Some fields are missing or are not valid. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }
    
    

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        String msg = "This media type is not supported. Maybe this request expected another media type? DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }




    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleHTTPMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String msg = "Questo metodo HTTP non è supportato. DETTAGLI: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    /**
     * This is the 404 error.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsToSendDTO handleMissingRoute(NoResourceFoundException ex, HttpServletRequest request) {
        String msg = "This resource does not exist, or this endpoint does not exist. "
                + "Endpoint called: " + request.getMethod() + " " + request.getRequestURI();
        return new ErrorsToSendDTO(msg);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMaybeMissingBody(HttpMessageNotReadableException ex) {
        String msg = "The request body is malformed or missing. Possible causes: " +
                "the body is missing entirely; " +
                "a field has the wrong type (e.g. a string was given where a number is expected); " +
                "a list was given where a single value is expected, or vice versa; " +
                "an invalid enum value was provided; " +
                "the JSON syntax is invalid (e.g. missing quotes, brackets, or commas); " +
                "a date or number format is incorrect.";
        
        // i make a list with with the error message coming from the exception
        // i need the specific exception message to give an appropriate message to the client,
        // because this exception seems to prove hard to debug or hard to 
        // trace back the problem
        List<String> errors = List.of(ex.getMessage());
        
        ErrorsToSendDTO errorsToSend = new ErrorsToSendDTO(msg, errors);
        
        return errorsToSend;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        String msg = "File size exceeds server file size upload limit.";
        return new ErrorsToSendDTO(msg);
    } 

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleRequestIsNotMultipartRequest(MultipartException ex) {
        String msg = "This endpoint expects the request to be multipart form-data, "
                + "but it does not appear to be. Try setting the request headers "
                + "with content type multipart form-data.";
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleRequestIsMissingPart(MissingServletRequestPartException ex) {
        String msg = "This endpoint expects the request to have at least one part in the multipart, "
                + "but it seems there is none. This can happen if you are trying to upload a file. "
                + "Is the endpoint expecting a file upload? "
                + "DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMissingRequestParameter(MissingServletRequestParameterException ex) {
        String msg = "Some query string parameter is missing in the URL. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleFileDownload(FileDownloadException ex) {
        String msg = "Error while fetching or downloading remote file. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleDataAccessApiUsage(InvalidDataAccessApiUsageException ex) {
        String msg = "Error while using an API. DETAILS: " + ex.getMessage();
        return new ErrorsToSendDTO(msg);
    }

    /**
     * This error occurred when a table did not exist in DB.
     * It said "JDBC exception executing SQL [ERROR: relation "users" does not exist"
     * So it should be a good error handler in cases like this.
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleIncorrectInternalAPIUsage(InvalidDataAccessResourceUsageException ex) {
        ex.printStackTrace();
        return new ErrorsToSendDTO("There was an error in the server.");
    }

    // this is a startup error, not an error during request lifecyle.
    // it means, it cannot be caught like i do with other errors
    // @ExceptionHandler(CommandAcceptanceException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorsToSendDTO handleCommandAcceptanceException(CommandAcceptanceException ex) {
    //     // ex.printStackTrace();
    //     LOGGER.error(ex.getMessage());
    //     return new ErrorsToSendDTO("Fatal error at the ORM level. "
    //                                 +"This is likely due to a fatal error at the database level.");
    // }
    

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ErrorsToSendDTO("There was an error in the server.");
    }

}