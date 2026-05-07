package giuseppetavella.demo_login_system.exceptions;

import giuseppetavella.demo_login_system.payloads.in_response.ErrorsToSendDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

@RestControllerAdvice
public class ErrorsHandler {
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



    /**
     * Handles exceptions raised when current user of request
     * is not authorized to access an endpoint. We protect
     * the endpoint by using @PreAuthorize annotation
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsToSendDTO handleAuthorizationDenied(AuthorizationDeniedException ex) {
        return new ErrorsToSendDTO("Non hai la giusta autorizzazzione. DETTAGLI: "+ ex.getMessage());
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

    // @ExceptionHandler(CaricamentoFileException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public ErrorsToSendDTO handleCaricamentoFile(CaricamentoFileException ex) {
    //     String msg = "Errore durante il caricamento di un file. DETTAGLI: " + ex.getMessage();
    //     return new ErrorsToSendDTO(msg);
    // }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ErrorsToSendDTO("Some data violates data integrity.");
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
    public ErrorsToSendDTO handleMissingRoute(NoResourceFoundException ex) {
        String msg = "Questa risorsa sembra non esistere, o non esiste questo endpoint.";
        return new ErrorsToSendDTO(msg);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsToSendDTO handleMaybeMissingBody(HttpMessageNotReadableException ex) {
        String msg = "La richiesta non è ben formata; forse manca il body, "
                +"i campi del body non sono ben formati, o qualche valore categorico non viene soddisfatto (ENUM)?";
        return new ErrorsToSendDTO(msg);
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
    


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsToSendDTO handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ErrorsToSendDTO("C'è stato un error nei server. Stiamo risolvendo.");
    }

}