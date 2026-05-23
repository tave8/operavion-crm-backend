package giuseppetavella.demo_login_system.domain.business.auth.dto.to_send.forgot_password;

public class ForgotPasswordToSendDTO {
    
    private final String message;
    
    public ForgotPasswordToSendDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
