package giuseppetavella.zero_chiamate.domain.business.auth.params;

public record VerifyEmailEmailParams(
        String firstname,
        
        String verificationUrl
) {
}
