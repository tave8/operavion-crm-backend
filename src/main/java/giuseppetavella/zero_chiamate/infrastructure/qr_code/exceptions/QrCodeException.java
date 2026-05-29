package giuseppetavella.zero_chiamate.infrastructure.qr_code.exceptions;

public class QrCodeException extends RuntimeException 
{
    public QrCodeException(String message) {
        super("Error with QR code. DETAILS: " + message);
    }
}
