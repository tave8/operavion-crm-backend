package giuseppetavella.demo_login_system.domain.entities.users;

import giuseppetavella.demo_login_system.domain.entities.companies.Company;
import giuseppetavella.demo_login_system.exceptions.*;
import giuseppetavella.demo_login_system.domain.entities.users.dto.sent.NewUserSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.SignupSentDTO;
import giuseppetavella.demo_login_system.domain.entities.users.dto.sent.UpdatedProfileSentDTO;
import giuseppetavella.demo_login_system.domain.business.auth.dto.sent.reset_password.ResetPasswordOldPasswordSentDTO;
import giuseppetavella.demo_login_system.domain.entities.users.dto.to_send.ProfileToSendDTO;
import giuseppetavella.demo_login_system.infrastructure.email.EmailService;
import giuseppetavella.demo_login_system.infrastructure.storage.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class UsersService {
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private EmailService appEmailService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private ImageUploadService imageUploadService;


    
    /**
     * Find a user by ID.
     */
    public User findById(UUID userId) throws NotFoundException {
        return usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId, "user"));
    }

    public User findById(String userId) throws NotFoundException {
        try {
            return this.findById(UUID.fromString(userId));
        } catch(IllegalArgumentException ex) {
            throw new InvalidUUIDStringException(userId);
        }
    }
    
    // public User findAllWhoSignedupOnDay() {
    //    
    // }

    /**
     * Find a user by email.
     */
    public User findByEmail(String email) throws NotFoundException {
        if(email == null) {
            throw new NotFoundException("An email that was null was given.");
        }
        
        User userFound = this.usersRepository.findByEmail(email);
        
        if (userFound == null) {
            throw new NotFoundException("User with email '" + email + "' was not found.");
        }
        
        return userFound;
    }


    /**
     * Find a user by username.
     */
    public User findByUsername(String email) throws NotFoundException {
        if(email == null) {
            throw new NotFoundException("A username that was null was given.");
        }

        User userFound = this.usersRepository.findByUsername(email);

        if (userFound == null) {
            throw new NotFoundException("User with username '" + email + "' was not found.");
        }

        return userFound;
    }

    /**
     * A user with the given email exists?
     */
    public boolean existsByEmail(String email) {
        return this.usersRepository.existsByEmail(email);
    }

    
    
    /**
     * A user with this ID exists?
     */
    public boolean existsById(UUID userId) {
        if(userId == null) {
            return false;
        }
        return this.usersRepository.existsById(userId);
    }
    
    /**
     * Add a user.
     * Main method.
     * Make sure that all custom checks are done,
     * before calling this method.
     */
    private User addAnyUser(User user) throws UnauthorizedException  {
        
        // if email already exists
        if(this.existsByEmail(user.getEmail())) {
            throw new UnauthorizedException("This email already exists.");
        }
        
        return this.usersRepository.save(user);
    }

    
    
    /**
     * Add admin only once.
     */
    public User addAdminOnlyOnce(SignupSentDTO body, Company company) {
        
        boolean adminAlreadyExists = this.usersRepository.existsByRoleInCompany(
                UserRole.ADMIN, company
        ); 
        
        // there can only be one admin for this company 
        if (adminAlreadyExists) {
            throw new UnauthorizedException(
                    "While trying to add an admin, at least another admin for company with ID "
                            + company.getId() + " exists. A company can only have 1 admin "
                            +"(This could also be an internal API usage error)"
            );
        }
        
        String hashedPassword = this.bcrypt.encode(body.password());

        String uniqueUsername = this.generateUniqueUsernameFrom(body.firstname(), body.lastname());

        User newUser = new User(
                company,
                body.email(),
                hashedPassword,
                body.firstname(),
                body.lastname(),
                UserRole.ADMIN,
                uniqueUsername
        );
        
        return this.addAnyUser(newUser);
    }


    /**
     * Add user to company based on role.
     */
    @Transactional
    public User addNonAdminUserBasedOnRole(NewUserSentDTO body, 
                                           UserRole role, 
                                           Company company, 
                                           String tempPassword) 
    {
        // you can add only non-admin users
        if(role.equals(UserRole.ADMIN)) {
            throw new IncorrectInternalAPIUsage("Cannot add an admin user in a method that "
                                              +"specifically adds non-admin users.");
        }
        
        String hashedTempPassword = this.bcrypt.encode(tempPassword);

        String uniqueUsername = this.generateUniqueUsernameFrom(body.firstname(), body.lastname());
        
        // if role is coordinator:
        if(role.equals(UserRole.COORDINATOR)) {
            
            User newUser = new User(
                    company,
                    body.email(),
                    hashedTempPassword,
                    body.firstname(),
                    body.lastname(),
                    role,
                    uniqueUsername
            );
            
            User userFromDB = this.addAnyUser(newUser);
            
            // send email with verify email
            this.appEmailService.sendVerifyEmailWithVerificationUrl(userFromDB);
            
            return userFromDB;
            
        }
        
        // if role is operator
        if(role.equals(UserRole.OPERATOR)) {

            User newUser = new User(
                    company,
                    null,
                    hashedTempPassword,
                    body.firstname(),
                    body.lastname(),
                    role,
                    uniqueUsername
            );
            
            return this.addAnyUser(newUser);    
                
        }

        
        throw new UnauthorizedException("No logic was defined to add user role '"+role+"' based on role.");
        
    }
    

    /**
     * Update my profile, given the ID.
     */

    public User updateOwnProfile(User profile, 
                                 UpdatedProfileSentDTO profileBody)
    {
        profile.setFirstname(profileBody.firstname());
        profile.setLastname(profileBody.lastname());
        return this.usersRepository.save(profile);
    }

    
    
    /**
     * Upload my new avatar image.
     */
    public User uploadMyAvatarImage(User user, MultipartFile avatarImage) throws InvalidFileUploadedException, 
                                                                                 FileUploadException
    {
        
        // get URL of uploaded image
        String avatarUrl = this.imageUploadService.uploadAvatarImage(avatarImage);

        // update author
        user.setAvatarUrl(avatarUrl);

        // save user      
        return this.usersRepository.save(user);

    }


    /**
     * Get all users of the given company
     */
    public Page<User> getUsersByCompany(Company company) 
    {
        String sortBy = "createdAt";
        String sortOrder = "desc";
        int finalPage = 0;
        int finalSize = 1000;

        // int finalSize = Math.clamp(pageSize, 1, 10);

        // int finalPage = Math.max(0, page);

        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);
        
        return this.usersRepository.getUsersByCompany(company, pageable);
        
    }


    /**
     * Find users by role.
     * @return
     */
    public List<User> findUsersByRole(Company company, UserRole role)
    {
        return this.usersRepository.findUsersByRole(company, role);
    }

    public List<ProfileToSendDTO> findUsersByRoleDTO(Company company, UserRole role)
    {
        return this
                .findUsersByRole(company, role)
                .stream()
                .map(ProfileToSendDTO::new)
                .toList();
    }
    
    

    /**
     * Get all users of the given company, except admin
     */
    public Page<User> getNonAdminUsersByCompany(Company company)
    {
        String sortBy = "createdAt";
        String sortOrder = "desc";
        int finalPage = 0;
        int finalSize = 1000;

        // int finalSize = Math.clamp(pageSize, 1, 10);

        // int finalPage = Math.max(0, page);

        Sort sort = sortOrder.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);

        return this.usersRepository.getUsersByCompanyExceptRole(company, UserRole.ADMIN, pageable);

    }


    /**
     * Get admin of given company.
     * @return
     */
    public User getAdminByCompany(Company company)
    {
        return this
                .usersRepository
                .findAdminByCompany(company)
                .orElseThrow(() -> new NotFoundException("Admin of company with ID '"+company.getId()+"' was not found."));
    }


    /**
     * Reset password at first login.
     * The user must, of course, be logged in.
     * @return
     */
    @Transactional
    public ProfileToSendDTO resetPasswordAtFirstLogin(User currentUser,
                                                      ResetPasswordOldPasswordSentDTO body)
    {

        // check: is user allowed to reset password?
        if(!currentUser.mustChangePasswordNow()) {
            throw new UnauthorizedException("User is not authorized to change password now. "
                    +"Verify that the user has the relevant authorization or role.");
        }


        boolean isPasswordMatch = this.bcrypt.matches(body.oldPassword(), currentUser.getPassword());

        // check: do old password (just sent) and actual password (in DB) match?
        if(!isPasswordMatch) {
            throw new UnauthorizedException("Wrong credentials");
        }

        // all good, set the new password

        String newHashedPassword = this.bcrypt.encode(body.newPassword());
        
        // set the new password
        currentUser.setPassword(newHashedPassword);
        // mark as password changed
        currentUser.setPasswordChanged(true);
        // save user in DB
        User userFromDB = this.usersRepository.save(currentUser);
        // return the saved user
        return new ProfileToSendDTO(userFromDB);

    }

    


    /**
     * Generate a unique username.
     * 
     * @param firstname
     * @param lastname
     * @return
     */
    private String generateUniqueUsernameFrom(String firstname, String lastname) {

        final int NUMBER_LENGTH = 4;

        // base = "mario.rossi"
        String base = firstname.toLowerCase().trim().replace(" ", "")
                + "."
                + lastname.toLowerCase().trim().replace(" ", "");

        String username;

        do {
            // Generate a random number with NUMBER_LENGTH digits
            // e.g. Math.pow(10, 6) = 1000000
            // Math.random() * 1000000 = 324234.123
            // (int) 324234.123 = 324234
            int randomNumber = (int) (Math.random() * Math.pow(10, NUMBER_LENGTH));

            // Concatenate base + number
            // e.g. "mario.rossi" + 324234 = "mario.rossi324234"
            username = base + randomNumber;

        } while (this.usersRepository.existsByUsername(username));
        // Keep looping if the username already exists in the DB
        // Exit when a unique one is found

        return username;
    }
    
}
