package giuseppetavella.zero_chiamate.infrastructure.qr_code.dto;

import java.util.UUID;

public record PrivateGeneratedQrCode(
        byte[] bytes,
        // the file ID in DB
        UUID fileId,
        // public url pointing to cloud
        String originalFilename
) {
}
