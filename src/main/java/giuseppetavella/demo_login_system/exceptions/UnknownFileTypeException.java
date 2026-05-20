package giuseppetavella.demo_login_system.exceptions;

public class UnknownFileTypeException extends RuntimeException {
    public UnknownFileTypeException(String mimeType) {
        super("Unsupported or unrecognized MIME type: '" + mimeType + "'. The system does not recognize this file extension."
                +"This can happen if the input file's extension is internally recognized, and therefore invalid. "
                +"Are you sure you the input file has a valid extension and this extension is allowed?");
    }
}