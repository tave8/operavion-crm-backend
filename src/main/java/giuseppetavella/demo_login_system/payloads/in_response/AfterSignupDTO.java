package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.User;

import java.util.UUID;

public class AfterSignupDTO {
    private final UUID userId;
    
    public AfterSignupDTO(User user) {
        this.userId = user.getId();    
    }

    public UUID getUserId() {
        return userId;
    }
}
