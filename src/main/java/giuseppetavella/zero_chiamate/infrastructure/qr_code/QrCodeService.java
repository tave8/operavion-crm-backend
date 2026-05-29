package giuseppetavella.zero_chiamate.infrastructure.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import giuseppetavella.zero_chiamate.infrastructure.qr_code.exceptions.QrCodeException;
import giuseppetavella.zero_chiamate.infrastructure.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Component
public class QrCodeService {
    

    private static final int DEFAULT_WIDTH        = 300;
    private static final int DEFAULT_HEIGHT       = 300;
    private static final int DEFAULT_LABEL_HEIGHT = 20;  // fits 12pt text with ~4px padding top/bottom
    private static final Font DEFAULT_FONT        = new Font("Arial", Font.PLAIN, 12);
    
    /**
     * Handles QR code generation only — no label, no composition.
     * Single responsibility: encode content → return raw QR image.
     */
    public BufferedImage generateQrImage(String content) {
        return generateQrImage(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public BufferedImage generateQrImage(String content, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(
                    content, BarcodeFormat.QR_CODE, width, height
            );
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (Exception ex) {
            throw new QrCodeException("Failed to generate QR code. DETAILS: " + ex.getMessage());
        }
    }

// -------------------------------------------------------------------------

    /**
     * Composes a QR image with a label below it.
     * Does not know or care how the QR was generated.
     *
     * @param qrImage  raw QR image (from generateQrImage)
     * @param label    text to render beneath the QR
     */
    public BufferedImage addLabel(BufferedImage qrImage, String label) {
        return addLabel(qrImage, label, DEFAULT_LABEL_HEIGHT, DEFAULT_FONT);
    }

    public BufferedImage addLabel(BufferedImage qrImage, String label, int labelHeight, Font font) {
        int width  = qrImage.getWidth();
        int height = qrImage.getHeight();

        BufferedImage composite = new BufferedImage(
                width, height + labelHeight, BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = composite.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // white background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height + labelHeight);

            // draw QR in top portion
            g.drawImage(qrImage, 0, 0, null);

            // draw label centered below QR
            g.setFont(font);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int textX = (width - fm.stringWidth(label)) / 2;
            int textY = height + labelHeight - 6;
            g.drawString(label, textX, textY);
        } finally {
            g.dispose(); // always release Graphics2D
        }

        return composite;
    }

// -------------------------------------------------------------------------

    /**
     * Encodes a BufferedImage to PNG bytes.
     * Kept separate so callers can use generateQrImage/addLabel without
     * being forced into a specific output format.
     */
    public byte[] toPngBytes(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new QrCodeException("Failed to encode image as PNG. DETAILS: " + ex.getMessage());
        }
    }

// -------------------------------------------------------------------------

    /**
     * Convenience facade: generate QR + add label + encode to PNG in one call.
     * This is the entry point most callers should use.
     */
    public byte[] generate(String content) {
        return generate(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public byte[] generate(String content, int width, int height) {
        BufferedImage qr = generateQrImage(content, width, height);
        return toPngBytes(qr);
    }

    public byte[] generate(String content, String label) {
        return generate(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, label);
    }

    public byte[] generate(String content, int width, int height, String label) {
        BufferedImage qr        = generateQrImage(content, width, height);
        BufferedImage composite = addLabel(qr, label);
        return toPngBytes(composite);
    }

    

    
}
