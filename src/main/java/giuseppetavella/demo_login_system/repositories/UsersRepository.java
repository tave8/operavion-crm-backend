package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.ForgotPasswordCode;
import giuseppetavella.demo_login_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {


    /**
     * Find a user by email.
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(String email);

    /**
     * Find a user by username.
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    User findByUsername(String username);
    
    /**
     * The user with the given email exists?
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(String email);

    /**
     * The user with this username exists?
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existsByUsername(String username);
    
    /**
     * Set new password.
     * This must be done with caution, only after all security steps
     * have been passed.
     */
    @Modifying
    @Transactional
    @Query("UPDATE User user SET user.password = :newPassword WHERE user = :user")
    void setNewPassword(@Param("user") User user,
                        @Param("newPassword") String newPassword);
    
    
}
