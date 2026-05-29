package giuseppetavella.zero_chiamate.infrastructure.qr_code;

import java.util.UUID;

public record PublicGeneratedQrCode(
        byte[] bytes,
        String url,
        // the file ID in DB
        UUID fileId,
        // storage key in cloud
        String storageKey,
        // public url pointing to cloud
        String originalFilename
) {
}
