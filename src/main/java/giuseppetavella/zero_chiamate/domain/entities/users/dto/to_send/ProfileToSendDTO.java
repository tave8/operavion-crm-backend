package giuseppetavella.zero_chiamate.domain.entities.users.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.companies.dto.to_send.CompanyToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ProfileToSendDTO {
    
    private final UUID userId;
    private final String email;
    private final String firstname;
    private final String lastname;
    private final String avatarUrl;
    private final OffsetDateTime createdAt;
    private final String role;
    private final String username;
    private final boolean mustChangePasswordNow;
    private final CompanyToSendDTO company;
    
    public ProfileToSendDTO(User user, CompanyToSendDTO companyDTO) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.avatarUrl = user.getAvatarUrl();
        this.createdAt = user.getCreatedAt();
        this.role = user.getRole().name();
        this.username = user.getUsername();
        this.mustChangePasswordNow = user.mustChangePasswordNow();
        this.company = companyDTO;
    }

    public CompanyToSendDTO getCompany() {
        return company;
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

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean isMustChangePasswordNow() {
        return mustChangePasswordNow;
    }
}
