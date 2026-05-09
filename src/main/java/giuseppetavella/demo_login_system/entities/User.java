package giuseppetavella.demo_login_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidDataFormatException;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
// this annotation allows us to never send these fields in a response
@JsonIgnoreProperties({"password", "accountNonExpired",
                        "accountNonLocked", "authorities",
                        "credentialsNonExpired", "enabled"})
public class User implements UserDetails {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @Column(name = "verified_email", nullable = false)
    private boolean verifiedEmail;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    protected User() {}
    
    public User(Company company,
                String email, 
                String password, 
                String firstname, 
                String lastname, 
                UserRole role,
                String username) 
    {
        this.company = company;
        this.email = email.toLowerCase().trim();
        this.password = password;
        this.role = role;
        this.username = username;
        this.verifiedEmail = false;
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setAvatarUrl(this.getDefaultAvatarUrl());
        this.createdAt = OffsetDateTime.now();
    }
    

    /**
     * Are the given users the same?
     */
    public static boolean isSameUser(User user1, User user2) throws InvalidDataFormatException 
    {
        
        if(user1 == null || user2 == null) {
            throw new InvalidDataFormatException(
                    "While validating whether two users are the same, "
                        +"either one or both of them were null. Are you sure both users exist " 
                        +"or have been passed correctly?"
            );
        }
        
        return user1.getId().equals(user2.getId());
    }
    

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    private String getDefaultAvatarUrl() {
        String fullname = this.getFirstname() + "+" + this.getLastname();
        return "https://ui-avatars.com/api/?name=" + fullname;
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

    public Company getCompany() {
        return company;
    }
    

    public void setFirstname(String firstname) throws InvalidDataException {
        if(firstname == null) {
            throw new InvalidDataException("Firstname cannot be null.");
        }
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) throws InvalidDataException {
        if(lastname == null) {
            throw new InvalidDataException("Lastname cannot be null.");
        }
        this.lastname = lastname;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    /**
     * Mark this user/account as a verified email.
     */
    public void markAsVerifiedEmail() throws EmailVerificationException {
        // you can only set the used state
        // from false to true, and no other

        // if the code was already used
        if(this.isVerifiedEmail()) {
            throw new EmailVerificationException("User email was already verified, "
                                                +"therefore it cannot be marked as verified again.");
        }

        this.verifiedEmail = true;
    }

    public UserRole getRole() {
        return role;
    }

    public UUID getId() {
        return id;
    }
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(this.role.name())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
