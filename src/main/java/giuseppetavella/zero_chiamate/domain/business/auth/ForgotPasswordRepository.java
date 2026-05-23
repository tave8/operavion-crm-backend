package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordCode, UUID> {

    /**
     * Get the last forgot password code of this user.
     */
    @Query("SELECT code FROM ForgotPasswordCode code WHERE code.user = :user ORDER BY code.createdAt DESC LIMIT 1")
    Optional<ForgotPasswordCode> getLastCodeOfUser(@Param("user") User user);

    /**
     * Mark all codes of the given user, as unusable. 
     * Note: you should execute this query right BEFORE you add a new code.
     * If you execute this query after you add a new code, 
     * this newly added will be marked as unusable as well, 
     * which is not what we want.
     */
    @Modifying
    @Transactional
    @Query("UPDATE ForgotPasswordCode code SET code.usable = false WHERE code.user = :user")
    void markAllCodesAsUnusable(@Param("user") User user);

    
    /**
     * Mark all codes of this user as used, except one code.
     */
    @Modifying
    @Transactional
    @Query("UPDATE ForgotPasswordCode code SET code.usable = false WHERE code.user = :user AND code != :codeToExclude")
    void markAllCodesAsUnusableExcept(@Param("user") User user, 
                                      @Param("codeToExclude") ForgotPasswordCode codeToExclude);


    /**
     * Mark the given code as clicked.
     */
    @Modifying
    @Transactional
    @Query("UPDATE ForgotPasswordCode code SET code.clicked = true WHERE code = :code")
    void markCodeAsClicked(@Param("code") ForgotPasswordCode code);

    
    /**
     * Mark the given code as used.
     */
    @Modifying
    @Transactional
    @Query("UPDATE ForgotPasswordCode code SET code.used = true WHERE code = :code")
    void markCodeAsUsed(@Param("code") ForgotPasswordCode code);
    
}
