package giuseppetavella.demo_login_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giuseppetavella.demo_login_system.enums.UserRole;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.InvalidDataFormatException;
import jakarta.persistence.*;
import org.apache.tika.utils.AnnotationUtils;
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
    
    
    @Column(unique = true)
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

    @Column(name = "verified_email_required", nullable = false)
    private boolean verifiedEmailRequired;
    
    @Column(name = "verified_email", nullable = false)
    private boolean verifiedEmail;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "password_change_required", nullable = false)
    private boolean passwordChangeRequired;
    
    @Column(name = "password_changed", nullable = false)
    private boolean passwordChanged;
    
    protected User() {}
    
    public User(Company company,
                @Nullable String email, 
                String password, 
                String firstname, 
                String lastname, 
                UserRole role,
                String username) 
    {
        this.company = company;
        
        // if email is null or not
        if(email == null) {
            this.email = null;
        } else {
            this.email = email.toLowerCase().trim();
        }
        
        // only admin does not have to change password 
        // (we assume at login, however this field could be changed,
        // to force the user to change password)
        if(role.equals(UserRole.ADMIN)) {
            this.passwordChangeRequired = false;
        } 
        // all other roles must change their password,
        // when first created
        else {
            this.passwordChangeRequired = true;
        }
        
        this.passwordChanged = false;

        // all roles require email verification except for operator
        if(role.equals(UserRole.OPERATOR)) {
            this.verifiedEmailRequired = false;
        } else {
            this.verifiedEmailRequired = true;
        }
        
        this.verifiedEmail = false;
        
        this.password = password;
        this.role = role;
        this.username = username;
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

    /**
     * The user must change password right now if:
     * - password change is required
     * - password was not changed
     * @return
     */
    public boolean mustChangePasswordNow() {
        return this.isPasswordChangeRequired() && !this.isPasswordChanged();
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

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerifiedEmailRequired() {
        return verifiedEmailRequired;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public void setVerifiedEmailRequired(boolean verifiedEmailRequired) {
        this.verifiedEmailRequired = verifiedEmailRequired;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
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
     * Is the email of this user verified, 
     * only if it is required?
     */
    public boolean isVerifiedEmailIfRequired() 
    {
        // if the email is required to be verified,
        // the result depends on whather the email is actually verified
        if(this.isVerifiedEmailRequired()) {
            return this.isVerifiedEmail();
        }
        // if the email is not required to be verified,
        // it's no problem
        return true;
    }

    /**
     * Has the password of this user been changed, 
     * only if it is required?
     */
    public boolean isPasswordChangedIfRequired()
    {
        if(this.isPasswordChangeRequired()) {
            return this.isPasswordChanged();
        }
        
        return true;
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
        return username;
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
