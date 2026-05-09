package giuseppetavella.demo_login_system.payloads.in_response;

public class AfterLoginDTO {
    
    private final String accessToken;
    private final String message;
    
    public AfterLoginDTO(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getMessage() {
        return message;
    }
}
