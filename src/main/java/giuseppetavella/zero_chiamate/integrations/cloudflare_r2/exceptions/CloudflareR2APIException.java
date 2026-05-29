package giuseppetavella.zero_chiamate.integrations.cloudflare_r2.exceptions;

import giuseppetavella.zero_chiamate.exceptions.FileException;

public class CloudflareR2APIException extends FileException {
    public CloudflareR2APIException(String message) {
        super("Error working with Cloudflare R2 API. DETAILS: " + message);
    }
}
