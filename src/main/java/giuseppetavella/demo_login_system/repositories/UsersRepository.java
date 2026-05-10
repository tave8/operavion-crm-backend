package giuseppetavella.demo_login_system.repositories;

import giuseppetavella.demo_login_system.entities.Company;
import giuseppetavella.demo_login_system.entities.ForgotPasswordCode;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * A user with this role already exists in this company?
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.role = :role AND u.company = :company")
    boolean existsByRoleInCompany(UserRole role, Company company);


    /**
     * Get all users of the given company.
     */
    @Query("SELECT u FROM User u WHERE u.company = :company")
    Page<User> getUsersByCompany(Company company, Pageable pageable);


    /**
     * Get all users of the given company, except given role.
     */
    @Query("SELECT u FROM User u WHERE u.company = :company AND u.role != :roleToExclude")
    Page<User> getUsersByCompanyExceptRole(
            Company company, 
            UserRole roleToExclude,
            Pageable pageable
    );


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
