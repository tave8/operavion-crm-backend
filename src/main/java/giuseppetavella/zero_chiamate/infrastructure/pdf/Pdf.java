package giuseppetavella.zero_chiamate.infrastructure.pdf;

import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachable;

public class Pdf implements EmailAttachable {
    
    private final byte[] bytes;
    
    public Pdf(byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public String toAttachment() {
        return FileHelper.toBase64(this.getBytes());
    }
    
    public byte[] toBytes() {
        return getBytes();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
