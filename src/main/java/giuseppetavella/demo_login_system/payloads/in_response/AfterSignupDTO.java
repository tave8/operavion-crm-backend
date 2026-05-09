package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.User;

import java.util.UUID;

public class AfterSignupDTO {
    private final UUID userId;
    private final String message;
    
    public AfterSignupDTO(User user, String message) {
        this.userId = user.getId();    
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
