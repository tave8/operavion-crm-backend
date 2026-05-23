package giuseppetavella.demo_login_system.domain.business.auth;

import giuseppetavella.demo_login_system.domain.entities.users.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "forgot_password_codes")
public class ForgotPasswordCode {
    
    @Id
    @GeneratedValue
    private UUID code;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private boolean clicked;
    
    @Column(nullable = false)
    private boolean usable;
    
    @Column(nullable = false)
    private boolean used;
    
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    
    protected ForgotPasswordCode() {}
    
    public ForgotPasswordCode(User user) {
        this.user = user;
        this.clicked = false;
        this.usable = true;
        this.used = false;
        this.createdAt = OffsetDateTime.now();
    }

    public boolean isClicked() {
        return clicked;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getCode() {
        return code;
    }

    public boolean isUsable() {
        return usable;
    }

    public User getUser() {
        return user;
    }

    public boolean isUsed() {
        return used;
    }
}
