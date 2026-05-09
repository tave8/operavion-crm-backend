package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.User;

public class AfterLoginDTO {
    
    private final String accessToken;
    private final String message;
    private final boolean mustChangePasswordNow;
    
    public AfterLoginDTO(String accessToken, User user, String message) 
    {
        this.accessToken = accessToken;
        this.message = message;
        this.mustChangePasswordNow = user.mustChangePasswordNow();
    }
    
    public AfterLoginDTO(String accessToken, User user) {
        this(accessToken, user, "");
    }
    

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isMustChangePasswordNow() {
        return mustChangePasswordNow;
    }

    public String getMessage() {
        return message;
    }
}
