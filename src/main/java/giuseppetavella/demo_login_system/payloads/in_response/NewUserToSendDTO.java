package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class NewUserToSendDTO {

    private final UUID userId;
    private final String email;
    private final String firstname;
    private final String lastname;
    private final String username;
    private final String tempPassword;

    public NewUserToSendDTO(User user, String tempPassword) 
    {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.username = user.getUsername();
        this.tempPassword = tempPassword;
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

    public String getTempPassword() {
        return tempPassword;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
