package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailVerificationException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification_codes")
public class EmailVerificationCode {
    
    @Id
    @GeneratedValue
    private UUID code;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private boolean used;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    protected EmailVerificationCode() {}
    
    public EmailVerificationCode(User user) {
        this.user = user;
        this.used = false;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getCode() {
        return code;
    }


    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }


    public boolean isUsed() {
        return used;
    }

    /**
     * Mark this email verification code as used.
     * 
     * @throws EmailVerificationException if the code was already marked as used
     */
    public void markAsUsed() throws EmailVerificationException {
        // you can only set the used state
        // from false to true, and no other
       
        // if the code was already used
        if(this.isUsed()) {
            throw new EmailVerificationException("Code was already marked as used, "
                                                +"therefore it cannot be marked as used again.");
        }
        
        this.used = true;
    }

    public User getUser() {
        return user;
    }

    
}
