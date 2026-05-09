package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ProfileToSendDTO {
    
    private final UUID userId;
    private final String email;
    private final String firstname;
    private final String lastname;
    private final String avatarUrl;
    private final OffsetDateTime createdAt;
    
    public ProfileToSendDTO(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.avatarUrl = user.getAvatarUrl();
        this.createdAt = user.getCreatedAt();
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public UUID getUserId() {
        return userId;
    }
}
