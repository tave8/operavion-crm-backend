package giuseppetavella.zero_chiamate.infrastructure.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.exceptions.QrCodeException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class QrCodeService {

    private static final int DEFAULT_WIDTH  = 300;
    private static final int DEFAULT_HEIGHT = 300;


    /**
     * Generate a QR code image from a string content.
     * Returns the PNG bytes of the QR code image.
     */
    public byte[] generate(String content) {
        return generate(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    /**
     * Generate a QR code image from a string content.
     * Returns the PNG bytes of the QR code image.
     *
     * @param width  the width of the image in pixels
     * @param height the height of the image in pixels
     */
    public byte[] generate(String content, int width, int height) {
        try {

            var matrix = new MultiFormatWriter().encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            var out = new ByteArrayOutputStream();
            
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            
            return out.toByteArray();

        } catch (Exception ex) {
            throw new QrCodeException("Failed to generate QR code. DETAILS: " + ex.getMessage());
        }
    }

}
