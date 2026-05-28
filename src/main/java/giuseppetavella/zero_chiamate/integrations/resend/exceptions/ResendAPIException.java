package giuseppetavella.zero_chiamate.integrations.resend.exceptions;

import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;

public class ResendAPIException extends EmailSendingException {
    public ResendAPIException(String message) {
        super("Error while sending email with Resend API. DETAILS: " + message);
    }
}
